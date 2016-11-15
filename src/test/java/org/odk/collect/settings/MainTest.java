package org.odk.collect.settings;

import org.junit.Before;

import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class MainTest {

    private Main main;
    private String anAdminKey = "change_language";
    private String aUserKey = "default_completed";

    @Before
    public void setUp() throws Exception {
        ClassLoader classLoader = getClass().getClassLoader();
        String fixture = classLoader.getResource("collect.properties").getFile();
        String[] args = {fixture};
        this.main = new Main(args);
    }

    @org.junit.Test
    public void testAdminPropsHasAdminKeyNotUserKey() throws Exception {
        assertTrue(main.adminProperties.containsKey(this.anAdminKey));
        assertFalse(main.adminProperties.containsKey(this.aUserKey));
    }

    @org.junit.Test
    public void testUserPropsHasUserKeyNotAdminKey() throws Exception {
        assertTrue(main.userProperties.containsKey(this.aUserKey));
        assertFalse(main.userProperties.containsKey(this.anAdminKey));
    }

    @org.junit.Test
    public void testBooleanPropsAreCastToBool() throws Exception {
        assertEquals(main.adminProperties.get(this.anAdminKey
        ).getClass().getCanonicalName(), "java.lang.Boolean");
        assertEquals(main.userProperties.get(this.aUserKey
        ).getClass().getCanonicalName(), "java.lang.Boolean");
    }

    public void printSettingsContents() {
        // For debug / test inspection of a collect.settings object file.
        // Add @org.junit.Test decorator to run as test.
        // Make sure fileName exists in test/resources or you'll get an NPE :)
        String fileName = "collect.settings";
        try {
            ClassLoader classLoader = getClass().getClassLoader();
            FileInputStream fin = new FileInputStream(
                    classLoader.getResource(fileName).getFile());
            ObjectInputStream ois = new ObjectInputStream(fin);
            Map<?, ?> user_entries = (Map<?, ?>) ois.readObject();
            for (Map.Entry<?, ?> entry : user_entries.entrySet()) {
                Object v = entry.getValue();
                Object key = entry.getKey();
                System.out.println("user." + key.toString() + "=" + v.toString());
            }
            Map<?, ?> admin_entries = (Map<?, ?>) ois.readObject();
            for (Map.Entry<?, ?> entry : admin_entries.entrySet()) {
                Object v = entry.getValue();
                Object key = entry.getKey();
                System.out.println("admin." + key.toString() + "=" + v.toString());
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}