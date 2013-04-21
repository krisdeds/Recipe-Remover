package samrg472.reciperemover;

import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ConfigurationHandler {

    private static ArrayList<String> lines = new ArrayList<String>();
    private File path;

    public ConfigurationHandler(File path) {
        this.path = path;
    }

    public void readConfig() {
        BufferedReader reader = getReader();
        if (reader == null) {
            writeConfig();
            return;
        }

        try {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.trim().startsWith("#") || line.isEmpty()) continue;
                lines.add(line.trim());

                if (!line.contains(":")) {
                    RecipeHandler.addID(Integer.parseInt(line.trim()));
                    continue;
                }

                String[] split = line.split(":");
                int id = Integer.parseInt(split[0]);
                int metadata = Integer.parseInt(split[1]);
                RecipeHandler.addID(id, metadata);
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace(System.err);
        }
    }

    public static List<String> getLines() {
        return Collections.unmodifiableList(lines);
    }

    private BufferedReader getReader() {
        try {
            FileInputStream fis = new FileInputStream(path);
            DataInputStream dis = new DataInputStream(fis);
            return new BufferedReader(new InputStreamReader(dis));
        } catch (FileNotFoundException e) {
            return null;
        }
    }

    private void writeConfig() {
        try {
            FileWriter fw = new FileWriter(path, true);
            BufferedWriter writer = new BufferedWriter(fw);

            writer.write("# Add recipes to be removed in the following format: <id>:<metadata>\n");
            writer.write("# If no metadata is specified, all recipes pertaining to the ID will be removed");

            writer.close();
        } catch (Exception e) {
            e.printStackTrace(System.err);
        }
    }
}
