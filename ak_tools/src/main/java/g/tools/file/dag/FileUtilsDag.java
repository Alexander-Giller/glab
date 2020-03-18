package g.tools.file.dag;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class FileUtilsDag {

    public static List<File> getAllDagFiles(File dir) {
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
                result.addAll(getAllDagFiles(file));
            } else if ("dag.json".equals(file.getName().toLowerCase())) {
                result.add(file);
            }
        });

        return result;
    }

    public static String getGroupFromFilePath(String baseDirPath, String filePath) {
        String localFilePath = filePath.replace(baseDirPath, "");
        int groupEndIndex = localFilePath.indexOf(File.separator);
        if (groupEndIndex < 0) {
            throw new IllegalArgumentException("Can not determine group for the file. " +
                    "BaseDir: " + baseDirPath + " File: " + filePath);
        }
        return localFilePath.substring(0, groupEndIndex);
    }

}
