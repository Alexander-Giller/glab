package g.tools.dag;


import java.util.ArrayList;
import java.util.List;


public class AggregateModel {

    // Just a name of aggregate.
    private String name;
    // A group of aggregate based on it's directory.
    private String group;
    // A type of aggregate (for ex. "import" or "query").
    private String type;
    // Dependency on others aggregates.
    private List<String> aggregateDependencies;
    // Just information about used tables from rdm.
    private List<String> rdmDependencies;


    private AggregateModel() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getAggregateDependencies() {
        return aggregateDependencies;
    }

    public void setAggregateDependencies(List<String> aggregateDependencies) {
        this.aggregateDependencies = aggregateDependencies;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public List<String> getRdmDependencies() {
        return rdmDependencies;
    }

    public void setRdmDependencies(List<String> rdmDependencies) {
        this.rdmDependencies = rdmDependencies;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String toString() {
        return this.name + " ["
                + this.group + ", "
                + this.type + "]";
    }


    public static final class AggregateModelBuilder {
        private String name;
        private String group;
        private String type;
        private List<String> aggregateDependencies = new ArrayList<>();
        private List<String> rdmDependencies = new ArrayList<>();

        public AggregateModelBuilder() {
        }

        public AggregateModelBuilder setName(String name) {
            this.name = name;
            return this;
        }

        public AggregateModelBuilder setAggregateDependencies(List<String> aggregateDependencies) {
            this.aggregateDependencies = aggregateDependencies == null ? new ArrayList<>() : aggregateDependencies;
            return this;
        }

        public AggregateModelBuilder setRdmDependencies(List<String> rdmDependencies) {
            this.rdmDependencies = rdmDependencies == null ? new ArrayList<>() : rdmDependencies;
            return this;
        }

        public AggregateModelBuilder setGroup(String group) {
            this.group = group;
            return this;
        }

        public AggregateModelBuilder setType(String type) {
            this.type = type;
            return this;
        }

        public AggregateModel build() {
            AggregateModel aggregateModel = new AggregateModel();
            aggregateModel.setName(name);
            aggregateModel.setAggregateDependencies(aggregateDependencies);
            aggregateModel.setRdmDependencies(rdmDependencies);
            aggregateModel.setGroup(group);
            aggregateModel.setType(type);
            return aggregateModel;
        }

    }
}
