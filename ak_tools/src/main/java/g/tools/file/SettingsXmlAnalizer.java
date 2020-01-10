package g.tools.file;


import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.XMLConfiguration;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.util.*;

/**
 * HOWTO use it.
 *
 * 1. Specify settings.xml file path: String filePath = "D:\\Projects\\tco-new-pipe\\tco-mvs-portal\\settings.xml";
 * 2. Run it;
 * 3. Console will show all difference between all profiles by pairs.
 *      Ideally it means there should not be any difference between parameters sets of twi different profiles.
 */
public class SettingsXmlAnalizer {

    public static void main(String[] args) throws ConfigurationException {
        String filePath = "D:\\Projects\\tco-new-pipe\\tco-mvs-portal\\settings.xml";

        XMLConfiguration config = new XMLConfiguration(filePath);
        NodeList nodeProfiles = config.getDocument().getElementsByTagName("profile");
        System.out.println("Profiles: " + nodeProfiles.getLength());

        List<MvnProfile> profileList = new ArrayList<>();
        for (int i = 0; i < nodeProfiles.getLength(); ++i) {
            Node current = nodeProfiles.item(i);
            MvnProfile profile = createProfile(current);
            profileList.add(profile);
        }
        System.out.println("------------");
        for (int i = 1; i < profileList.size(); ++i) {
            MvnProfile mvnProfile1 = profileList.get(i - 1);
            MvnProfile mvnProfile2 = profileList.get(i);
            checkDiffBetween(mvnProfile1.getId(), mvnProfile2.getId(), profileList);
        }
//        checkDiffBetween("env-dev", "env-prod",  profileList);

    }

    public static void checkDiffBetween(String prof1, String prof2, List<MvnProfile> profiles) {
        MvnProfile profile1 = null;
        MvnProfile profile2 = null;
        for (MvnProfile p: profiles) {
            if (prof1.equals(p.getId())) {
                profile1 = p;
            } else if (prof2.equals(p.getId())) {
                profile2 = p;
            }
        }

        checkDifferrence(profile1, profile2);
        checkDifferrence(profile2, profile1);
    }


    public static void checkDifferrence(MvnProfile profile1, MvnProfile profile2) {
        Set<String> keys1 = profile1.getProperties().keySet();
        Set<String> keys2 = profile2.getProperties().keySet();

        Set<String> diff = new HashSet<>();
        keys1.forEach(k -> {
            if (!keys2.contains(k)) {
                diff.add(k);
            }
        });
        if (!diff.isEmpty()) {
            System.out.println("-------------");
            System.out.println("Difference between " + profile1.getId() + " and " + profile2.getId());
            diff.forEach(k -> System.out.println(k));
        }
    }

    public static MvnProfile createProfile(Node node) {
        Node idNode = getNodeWithName(node, "id");
        String sId = idNode.getTextContent();
        Map<String, String> props = getProperties(getNodeWithName(node, "properties"));
        return new MvnProfile(sId, props);
    }

    public static Map<String, String> getProperties(Node node) {
        Map<String, String> props = new HashMap<>();
        NodeList childNodes = node.getChildNodes();
        for (int i = 0; i < childNodes.getLength(); ++i) {
            Node prop = childNodes.item(i);
            props.put(prop.getNodeName(), prop.getTextContent());
        }
        return props;
    }

    public static Node getNodeWithName(Node node, String name) {
        NodeList childNodes = node.getChildNodes();
        for (int i = 0; i < childNodes.getLength(); ++i) {
            if (name.equals(childNodes.item(i).getNodeName())) {
                return childNodes.item(i);
            }
        }
        return null;
    }


    public static class MvnProfile {
        private final String id;
        private final Map<String, String> properties;

        public MvnProfile(String id, Map<String, String> properites) {
            this.id = id;
            this.properties = properites;
        }

        public String getId() {
            return id;
        }

        public Map<String, String> getProperties() {
            return properties;
        }
    }

}
