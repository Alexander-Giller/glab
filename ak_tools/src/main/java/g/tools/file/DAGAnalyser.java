package g.tools.file;


import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.io.IOUtils;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;


public final class DAGAnalyser {

    private static final String AGGREGATES = "aggregates";
    private static final String AGGREGATE_DEPENDENCIES = "aggregate_dependencies";
    private static final String RDM_DEPENDENCIES = "rdm_dependencies";

    private static final Map<String, AggregateModel> AGGREGATES_MAP = new HashMap<>();


    private DAGAnalyser() {
    }

    public static void main(String[] args) {
        String dir = "C:\\Work\\AS_W\\big-data-analytics-automation-config\\dags\\";
        List<File> files = getAllDagFiles(new File(dir));
//        printFiles(files);

        files.stream().forEach(file -> {
            try {
                String filePath = file.getAbsolutePath();
                System.out.println(filePath);
                parseJSON(filePath);
                System.out.println();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });

        // General info
        System.out.println("Total aggregates: " + AGGREGATES_MAP.size());
        System.out.println("Total DAGs: " + files.size());
    }

    private static void parseJSON(String filePath) throws IOException {
        // Get file content as string.
        String content = IOUtils.toString(new FileInputStream(filePath), StandardCharsets.UTF_8.displayName());

        // Create specified mapper. It will provide ability to parse json as tree.
        ObjectMapper mapper = new ObjectMapper(new JsonFactory());

        // Read as tree.
        JsonNode rootNode = mapper.readTree(content);

        // Get aggregates rootNode.
        List<JsonNode> aggregatesRootNode = rootNode.findValues(AGGREGATES);

        // Print rootNodes.
        aggregatesRootNode.stream().forEach(aggregates -> {
            List<AggregateModel> aggregateModels = showAggregates(aggregates);
            aggregateModels.forEach(aggregateModel -> AGGREGATES_MAP.put(aggregateModel.getName(), aggregateModel));
        });
    }

    private static List<AggregateModel> showAggregates(JsonNode aggregates) {
        List<AggregateModel> aggregateModels = new ArrayList<>();

        for (JsonNode aggregate : aggregates) {
            AggregateModel aggregateModel = new AggregateModel();

            for (Iterator<Map.Entry<String, JsonNode>> it = aggregate.fields(); it.hasNext(); ) {
                Map.Entry<String, JsonNode> entry = it.next();
                String aggregateName = entry.getKey();
                aggregateModel.setName(aggregateName);
                System.out.println(aggregateName);
            }

            final String tab = "\t";

            System.out.println(tab + AGGREGATE_DEPENDENCIES);
            showAggregateDependencies(aggregate, AGGREGATE_DEPENDENCIES, tab + tab);

//            System.out.println(tab + RDM_DEPENDENCIES);
//            showAggregateDependencies(aggregate, RDM_DEPENDENCIES, tab + tab);

            aggregateModels.add(aggregateModel);
        }

        return aggregateModels;
    }

    private static void showAggregateDependencies(JsonNode aggregate, String nodeName) {
        showAggregateDependencies(aggregate, nodeName, "\t");
    }

    private static void showAggregateDependencies(JsonNode aggregate, String nodeName, String tabSpace) {
        List<JsonNode> aggregateDependencies = aggregate.findValues(nodeName);
        Optional<JsonNode> first = aggregateDependencies.stream().findFirst();
        if (first.isPresent()) {
            JsonNode dependencies = first.get();
            for (Iterator<JsonNode> iter = dependencies.elements(); iter.hasNext(); ) {
                JsonNode it = iter.next();
                System.out.println(tabSpace + it.textValue());
            }
        }
    }

    private static void printFiles(List<File> files) {
        // Sout all files
        System.out.println("---------_Files: " + files.size() + "_-----------");
        files.stream().forEach(file -> {
            System.out.println(file.getAbsolutePath());
        });
        System.out.println("-----------------------------------------");
    }

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


    public static class AggregateModel {
        private String name;

        public AggregateModel() {
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }

}
