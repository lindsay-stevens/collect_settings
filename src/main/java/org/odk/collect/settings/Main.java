package org.odk.collect.settings;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class Main {

    private String settingsPath;
    private File outputFile;
    Properties userProperties;
    Properties adminProperties;

    public static void main(String[] args) {
        Main obj = new Main(args);
        obj.writePropsObject();
    }

    Main(String[] args) {

        if (args.length != 1) {
            throw new IllegalArgumentException("Need only one string arg");
        }
        File f = new File(args[0]);
        if (f.exists() && !f.isDirectory()){
            this.settingsPath = args[0];
        }
        else {
            throw new IllegalArgumentException("Path not a valid file");
        }
        this.userProperties = getProps("user");
        this.adminProperties = getProps("admin");
        this.outputFile = new File(f.getParent(), "collect.settings");
    }

    public void writePropsObject(){
        if (this.outputFile.exists() && !this.outputFile.isDirectory()){
            this.outputFile.delete();
        }
        this.savePropsToFile(
                this.outputFile, this.userProperties, this.adminProperties);
    }

    private Properties getProps(String propsGroup) {

        Properties props = new Properties();
        Properties propCopy = new Properties();
        try {
            props.load(new FileInputStream(this.settingsPath));
            for (Map.Entry<Object, Object> entry : props.entrySet()) {
                String k = entry.getKey().toString();
                String v = entry.getValue().toString();

                if (k.startsWith(propsGroup)){
                    String trimKey = k.split("\\.")[1];

                    // Collect expects bool to be type cast but not numbers etc.
                    switch (v) {
                        case "true":
                            propCopy.put(trimKey, true);
                            break;
                        case "false":
                            propCopy.put(trimKey, false);
                            break;
                        default:
                            propCopy.put(trimKey, v);
                            break;
                    }
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return propCopy;
    }

    private void savePropsToFile(
            File outputFile, Properties props_user, Properties props_admin) {
        ObjectOutputStream output;
        try {
            output = new ObjectOutputStream(
                    new FileOutputStream(outputFile, true));
            HashMap<?, ?> map_user = new HashMap<>((Map<?, ?>) props_user);
            HashMap<?, ?> map_admin = new HashMap<>((Map<?, ?>) props_admin);
            output.writeObject(map_user);
            output.writeObject(map_admin);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

}
