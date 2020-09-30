package temporalgraph;

import encoding.CodedProblem;
import encoding.IntOp;
import org.jgrapht.graph.DirectedMultigraph;
import util.TemporalPlan;

import java.util.*;

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

        List<String> constants = cp.getConstants();

        //adding all the nodes to the graph + the start-end edges
        TreeMap<Double, Set<IntOp>> actions = plan.actions();

        for (Map.Entry<Double, Set<IntOp>> entry : actions.entrySet()) {
            Set<IntOp> seti = entry.getValue();
            Iterator<IntOp> setIt = seti.iterator();
            while(setIt.hasNext()){
                IntOp i = setIt.next();
                int [] instantiations = i.getInstantiations();
                String nodeName = i.getName() + "(" + constants.get(instantiations[0]);
                for(int j=1; j < instantiations.length; j++)
                    nodeName = nodeName + "," + constants.get(instantiations[j]);
                nodeName = nodeName + ")";
                TemporalNode actionStart = new TemporalNode(1 , nodeName + "_start", graphSize);
                actionStart.setInstantiation(i.getInstantiations());
                addNode(actionStart);
                TemporalNode actionEnd = new TemporalNode(2 , nodeName + "_end", graphSize);
                addNode(actionEnd);
                tgraph.addEdge(actionStart,actionEnd, new TemporalEdge(i.getDuration(),i.getDuration(),2) );
            }
        }


        //System.out.println(tgraph.toString());

    }

    public void addNode (TemporalNode node) {
        tgraph.addVertex(node);
        graphSize++;
    }

}
