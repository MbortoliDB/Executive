package planparser;

import org.jgrapht.graph.DefaultEdge;

public class TemporalEdge extends DefaultEdge {

    private double lb;
    private double ub;
    private int type;   //1 -> conditional edge   2 -> start_end  3 -> interference

    //creating conditional or start-end edge
    public TemporalEdge (double lb,double ub,int type) {
        this.lb = lb;
        this.ub = ub;
        this.type = type;
    }

    //creating interference edge
    public TemporalEdge () {
        this.type = 3;
    }

    public double getLb() {
        return lb;
    }

    public double getUb() {
        return ub;
    }

    public int getType() {
        return type;
    }

    public void setLb(double lb) {
        this.lb = lb;
    }

    public void setUb(double ub) {
        this.ub = ub;
    }

    public void setType(int type) {
        this.type = type;
    }
}
