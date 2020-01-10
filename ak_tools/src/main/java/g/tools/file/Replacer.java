package g.tools.file;



import org.apache.commons.io.IOUtils;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * HOWTO use it.
 * 1. Specify directory with code base: String dir = "D:\\Projects\\tco-new-pipe\\tco-mvs-portal"
 * 2. Set up old version and new version string: files.forEach(f -> replaceText(f, "1.1.0-SNAPSHOT", "1.1.0-RC"));
 * 3. Profit.
 *
 * Also it can be used not only for increasing project version but also as global string replacer.
 * Please be accurate and check a result always before merge.
 */
public final class Replacer {

    private Replacer() {
    }

    public static void main(String[] args) {
        String dir = "D:\\Projects\\tco-new-pipe\\tco-mvs-portal";
        List<File> files = getAllFiles(new File(dir));
        files.forEach(f -> replaceText(f, "1.5.0-SNAPSHOT", "1.5.0-SNAPSHOT"));
//        files.forEach(f -> replaceText(f, "cat /etc/spark2/conf/spark-env.sh | grep HADOOP_HOME | cut -d '=' -f 2 | cut -d '/' -f 4", "ls /usr/hdp | grep -o '[0-9].*'"));
    }


    public static void replaceFirstText(File file, String oldString, String newString) {
        try {
            String content = IOUtils.toString(new FileInputStream(file), StandardCharsets.UTF_8.displayName());
            if (content.contains(oldString)) {
                System.out.println(file.getAbsolutePath());
                content = content.replace(oldString, newString);
                IOUtils.write(content, new FileOutputStream(file), StandardCharsets.UTF_8.displayName());
            }
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }

    }


    public static void replaceText(File file, String oldString, String newString) {
        try {
            String content = IOUtils.toString(new FileInputStream(file), StandardCharsets.UTF_8.displayName());
            if (content.contains(oldString)) {
                System.out.println(file.getAbsolutePath());
                content = content.replaceAll(oldString, newString);
                IOUtils.write(content, new FileOutputStream(file), StandardCharsets.UTF_8.displayName());
            }
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }

    }

    public static List<File> getAllFiles(File dir) {
        String dirName = dir.getName();
        if (dirName.equals(".git") || dirName.equals("target")) {
            return Collections.emptyList();
        }
        if (dir.isFile()) {
            return Arrays.asList(dir);
        }

        final List<File> result = new ArrayList();
        Arrays.asList(dir.listFiles()).forEach(file -> {
            if (file.isDirectory()) {
                result.addAll(getAllFiles(file));
            } else {
                result.add(file);
            }
        });

        return result;
    }

}
