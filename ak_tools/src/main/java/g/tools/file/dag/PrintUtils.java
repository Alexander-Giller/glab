package g.tools.file.dag;


import java.io.File;
import java.util.List;


public class PrintUtils {

    public static void printFiles(List<File> files) {
        // sout all files
        System.out.println("---------_Files: " + files.size() + "_-----------");
        files.stream().forEach(file -> {
            System.out.println(file.getAbsolutePath());
        });
        System.out.println("-----------------------------------------");
    }

    public static void showAggregates(List<AggregateModel> aggregateModels) {
        for (AggregateModel aggregateModel: aggregateModels) {
            System.out.println(aggregateModel.getName() + " ["
                    + aggregateModel.getGroup() + ", "
                    + aggregateModel.getType() + "]");
            final String tab = "\t";
            printDependency(tab, Consts.AGGREGATE_DEPENDENCIES, aggregateModel.getAggregateDependencies());
//            printDependency(tab, Consts.RDM_DEPENDENCIES, aggregateModel.getRdmDependencies());
        }
    }

    private static void printDependency(String tab, String title, List<String> aggregateDependencies) {
        if (!aggregateDependencies.isEmpty()) {
            System.out.println(tab + title);
            aggregateDependencies.stream().forEach(dependency -> System.out.println(tab + tab + dependency));
        }
    }

}
