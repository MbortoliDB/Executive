package temporalgraph;

import encoding.IntOp;

public class ExtIntOp {

    private IntOp action;
    private int type;
    private int id;

    public ExtIntOp(IntOp action, int type, int id) {
        this.action = action;
        this.type = type;
        this.id = id;
    }

    public IntOp getAction() {
        return action;
    }

    public void setAction(IntOp action) {
        this.action = action;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
