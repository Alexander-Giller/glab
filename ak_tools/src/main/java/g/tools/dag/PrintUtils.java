package g.tools.dag;


import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;


public class PrintUtils {

    public static final String TAB = "\t";
    public static final String ROOT = "root";


    public static void printFiles(List<File> files) {
        // sout all files
        System.out.println("---------_Files: " + files.size() + "_-----------");
        files.stream().forEach(file -> {
            System.out.println(file.getAbsolutePath());
        });
        System.out.println("-----------------------------------------");
    }

    public static void showAggregatesGraphFromEnds(Map<String, AggregateModel> aggregateModels) {
        aggregateModels.entrySet().forEach(entry -> showAggregatesGraphFromEnds(entry.getValue(), aggregateModels, ""));
    }

    private static void showAggregatesGraphFromEnds(AggregateModel root, Map<String, AggregateModel> aggregateModels, String spaces) {
        System.out.println(spaces + root);
        root.getAggregateDependencies().stream()
                .map(dependency -> aggregateModels.get(dependency))
                .forEach(aggregateModel -> showAggregatesGraphFromEnds(aggregateModel, aggregateModels, spaces + TAB));
    }

    public static void showAggregatesGraphFromStarts(Map<String, AggregateModel> aggregateModels) {
        List<AggregateModel> commonAggregates = aggregateModels.entrySet().stream()
                .filter(entry -> Consts.COMMON.equals(entry.getValue().getGroup()))
                .map(entry -> entry.getValue())
                .collect(Collectors.toList());

        commonAggregates.forEach(aggregateModel -> showAggregatesGraphFromStarts(aggregateModel, aggregateModels, ""));
    }

    private static void showAggregatesGraphFromStarts(AggregateModel root, Map<String, AggregateModel> aggregateModels, String spaces) {
        System.out.println(spaces + root);
        getDependenciesOn(root, aggregateModels).forEach(aggregateDependency ->
            showAggregatesGraphFromStarts(aggregateModels.get(aggregateDependency.getName()), aggregateModels, spaces + TAB));
    }

    private static List<AggregateModel> getDependenciesOn(AggregateModel aggregate, Map<String, AggregateModel> aggregateModels) {
        String aggregateName = aggregate.getName();

        List<AggregateModel> result = aggregateModels.entrySet().stream()
                .filter(entry -> entry.getValue().getAggregateDependencies().contains(aggregateName))
                .map(entry -> entry.getValue())
                .collect(Collectors.toList());

        return result;
    }

    public static void showAggregatesOneByOne(Map<String, AggregateModel> aggregateModels) {
        Stream<Map.Entry<String, AggregateModel>> entrySet = aggregateModels.entrySet().stream()
//                .filter(entry -> Consts.COMMON.equals(entry.getValue().getGroup()))
                .filter(entry -> !Consts.IMPORT.equals(entry.getValue().getType()));

        entrySet.forEach(entry -> {
            AggregateModel aggregateModel = entry.getValue();
            System.out.println(aggregateModel);
            printDependency(TAB, Consts.AGGREGATE_DEPENDENCIES, aggregateModel.getAggregateDependencies());
//            printDependency(tab, Consts.RDM_DEPENDENCIES, aggregateModel.getRdmDependencies());
        });
    }

    private static void printDependency(String tab, String title, List<String> aggregateDependencies) {
        if (!aggregateDependencies.isEmpty()) {
            System.out.println(tab + title);
            aggregateDependencies.stream().forEach(dependency -> System.out.println(tab + tab + dependency));
        }
    }

}
