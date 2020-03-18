package g.tools.dag;


import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.io.IOUtils;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;


public final class DAGAnalyser {

    private DAGAnalyser() {
    }

    public static void main(String[] args) {
        // Defining input parameters.
        String baseDir = "C:\\Work\\AS_W\\big-data-analytics-automation-config\\dags\\";
//        String dir = baseDir + "common\\";
        String dir = baseDir;
        List<File> files = FileUtilsDag.getAllDagFiles(new File(dir));
        PrintUtils.printFiles(files);

        // Reading input data.
        Map<String, AggregateModel> aggregatesMap = readAggregates(files, baseDir);

        // Print info
        System.out.println("----- One by One ------");
        PrintUtils.showAggregatesOneByOne(aggregatesMap);
        System.out.println();

        System.out.println("------ Tree (From Starts To Ends) --------");
        PrintUtils.showAggregatesGraphFromStarts(aggregatesMap);
        System.out.println();

        System.out.println("------ Tree (From Ends To Starts) --------");
        PrintUtils.showAggregatesGraphFromEnds(aggregatesMap);
        System.out.println();

        System.out.println("Total aggregates: " + aggregatesMap.size());
        System.out.println("Total DAGs: " + files.size());
    }

    private static Map<String, AggregateModel> readAggregates(List<File> files, String baseDir) {
        Map<String, AggregateModel> result = new HashMap<>();

        files.stream().forEach(file -> {
            try {
                String filePath = file.getAbsolutePath();
                Map<String, AggregateModel> map = readFromJSON(filePath, baseDir);
                map.entrySet().stream()
                        .filter(entry -> !Consts.IMPORT.equals(entry.getValue().getType()))
                        .forEach(entry -> result.put(entry.getKey(), entry.getValue()));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });

        return result;
    }

    private static Map<String, AggregateModel> readFromJSON(String filePath, String baseDir) throws IOException {
        // Get file content as string.
        String content = IOUtils.toString(new FileInputStream(filePath), StandardCharsets.UTF_8);

        // Create specified mapper. It will provide ability to parse a json as tree.
        ObjectMapper mapper = new ObjectMapper(new JsonFactory());

        // Read as tree.
        JsonNode rootNode = mapper.readTree(content);

        // Get all aggregates rootNode. In general it should be one file - one 'aggregates' node.
        List<JsonNode> aggregatesRootNode = rootNode.findValues(Consts.AGGREGATES);

        // Creating models and accumulating result.
        Map<String, AggregateModel> result = new HashMap<>();

        String group = FileUtilsDag.getGroupFromFilePath(baseDir, filePath); // based on directory.
        AggregateModel.AggregateModelBuilder modelBuilder = new AggregateModel.AggregateModelBuilder();
        modelBuilder.setGroup(group);
        aggregatesRootNode.stream().forEach(aggregates -> {
            List<AggregateModel> aggregateModels = getAggregates(aggregates, modelBuilder);
            aggregateModels.forEach(aggregateModel -> result.put(aggregateModel.getName(), aggregateModel));
        });

        return result;
    }

    private static List<AggregateModel> getAggregates(JsonNode aggregates, AggregateModel.AggregateModelBuilder modelBuilder) {
        List<AggregateModel> aggregateModels = new ArrayList<>();

        for (JsonNode aggregate : aggregates) {
            // Reading info from json tree.
            String aggregateName = getFirstFieldKey(aggregate);
            String type = getType(aggregate);
            List<String> aggregateDependencies = getDependencies(aggregate, Consts.AGGREGATE_DEPENDENCIES);
            List<String> rdmDependencies = getDependencies(aggregate, Consts.RDM_DEPENDENCIES);

            // Create model.
            AggregateModel aggregateModel = modelBuilder
                    .setName(aggregateName)
                    .setType(type)
                    .setAggregateDependencies(aggregateDependencies)
                    .setRdmDependencies(rdmDependencies)
                    .build();
            aggregateModels.add(aggregateModel);
        }

        return aggregateModels;
    }

    private static String getType(JsonNode aggregate) {
        JsonNode taskNode = aggregate.findValue(Consts.TASK_TYPE);
        return taskNode.textValue();
    }

    private static String getFirstFieldKey(JsonNode aggregate) {
        Iterator<Map.Entry<String, JsonNode>> it = aggregate.fields();
        if (it.hasNext()) {
            return it.next().getKey();
        } else {
            return null;
        }
    }

    private static List<String> getDependencies(JsonNode aggregate, String nodeName) {
        List<String> result = new ArrayList<>();

        List<JsonNode> aggregateDependencies = aggregate.findValues(nodeName);
        if (aggregateDependencies.size() > 1) {
            throw new IllegalArgumentException("Aggregate contains more than one node 'dependencies': " + aggregate);
        }

        Optional<JsonNode> first = aggregateDependencies.stream().findFirst();
        if (first.isPresent()) {
            JsonNode dependencies = first.get();
            for (Iterator<JsonNode> iter = dependencies.elements(); iter.hasNext(); ) {
                String dependency = iter.next().textValue();
                result.add(dependency);
            }
        }

        return result;
    }

}
