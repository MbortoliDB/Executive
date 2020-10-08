package planparser;

public class TemporalNode {

    private int type; //1->action start 2->action end 3->plan start
    private String name;
    private int id;
    private int [] instantiation;

    public TemporalNode(int type, String name, int id) {
        this.type = type;
        this.name = name;
        this.id = id;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public int[] getInstantiation() {
        return instantiation;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String toString () {
        return name;
    }

    public void setInstantiation(int[] instantiation) {
        this.instantiation = instantiation;
    }
//TODO create custom equals/hashcode to speed up search
}
