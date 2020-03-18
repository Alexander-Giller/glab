package g.tools.file.dag;


import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.xml.internal.bind.v2.runtime.reflect.opt.Const;
import org.apache.commons.io.IOUtils;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;


public final class DAGAnalyser {

    private DAGAnalyser() {
    }

    public static void main(String[] args) {
        String dir = "C:\\Work\\AS_W\\big-data-analytics-automation-config\\dags\\";
        List<File> files = FileUtilsDag.getAllDagFiles(new File(dir));
        PrintUtils.printFiles(files);

        Map<String, AggregateModel> result = new HashMap<>();

        files.stream().forEach(file -> {
            try {
                String filePath = file.getAbsolutePath();
                Map<String, AggregateModel> aggregates = parseJSON(filePath, dir);
                aggregates.entrySet().forEach(entry -> {
                    if (!Consts.IMPORT.equals(entry.getValue().getType().toLowerCase())) {
                        result.put(entry.getKey(), entry.getValue());
                    }
                });
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });

        List<AggregateModel> aggregateModels = result.entrySet().stream()
                .map(entry -> entry.getValue())
                .collect(Collectors.toList());
        PrintUtils.showAggregates(aggregateModels);

        // General info
        System.out.println("Total aggregates: " + result.size());
        System.out.println("Total DAGs: " + files.size());
    }

    private static Map<String, AggregateModel> parseJSON(String filePath, String baseDir) throws IOException {
        // Get file content as string.
        String content = IOUtils.toString(new FileInputStream(filePath), StandardCharsets.UTF_8.displayName());

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
