package temporalgraph;

import encoding.CodedProblem;
import org.jgrapht.graph.DirectedMultigraph;
import util.TemporalPlan;

public class TemporalGraph {

    private CodedProblem cp;
    private TemporalPlan plan;
    private int graphSize;
    private DirectedMultigraph<TemporalNode,TemporalEdge> tgraph;

    public TemporalGraph(CodedProblem cp, TemporalPlan plan) {
        this.cp = cp;
        this.plan = plan;
        tgraph = new DirectedMultigraph<>(TemporalEdge.class);
        graphSize = 0;
    }

    public void createTemporalGraph () {
        TemporalNode start = new TemporalNode(3, "plan_start", graphSize);
        addNode(start);
        System.out.println("generated");
    }

    public void addNode (TemporalNode node) {
        tgraph.addVertex(node);
        graphSize++;
    }

}
