package temporalgraph;

public class TemporalNode {

    private int type; //1->action start 2->action end 3->plan start
    private String name;
    private int id;

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

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    //TODO create custom equals/hashcode to speed up search
}
