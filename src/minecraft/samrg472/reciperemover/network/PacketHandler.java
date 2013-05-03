package samrg472.reciperemover.network;

import cpw.mods.fml.common.network.IPacketHandler;
import cpw.mods.fml.common.network.Player;
import net.minecraft.network.INetworkManager;
import net.minecraft.network.packet.Packet250CustomPayload;
import samrg472.reciperemover.RecipeHandler;
import samrg472.reciperemover.RecipeRemover;

import java.io.*;

public class PacketHandler implements IPacketHandler {

    @Override
    public void onPacketData(INetworkManager manager, Packet250CustomPayload packet, Player player) {
        if (!packet.channel.equals(RecipeRemover.CHANNEL)) return;
        if (packet.data == null) return;
        DataInputStream dis = new DataInputStream(new ByteArrayInputStream(packet.data));
        try {
            while (dis.available() != 0)
                RecipeHandler.addID(dis.readUTF().trim());
        } catch (Exception e) {
            throw new RuntimeException(e); // Catch a major error
        }
        RecipeHandler.removeRecipes();
    }

    public static Packet250CustomPayload buildPacket(String... data) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DataOutputStream out = new DataOutputStream(baos);

        try {
            for (String i : data) {
                out.writeUTF(i);
            }
        } catch (IOException e) {
            e.printStackTrace(System.err);
        }
        return new Packet250CustomPayload(RecipeRemover.CHANNEL, baos.toByteArray());
    }
}
