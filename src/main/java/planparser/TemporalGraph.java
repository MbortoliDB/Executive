package planparser;

import encoding.CodedProblem;
import encoding.IntOp;
import org.apache.commons.collections4.iterators.ReverseListIterator;
import org.jgrapht.graph.SimpleDirectedGraph;
import org.jgrapht.nio.Attribute;
import org.jgrapht.nio.DefaultAttribute;
import util.IntExp;
import util.TemporalPlan;
import org.jgrapht.nio.dot.*;

import java.io.StringWriter;
import java.io.Writer;
import java.util.*;

public class TemporalGraph {

    private CodedProblem cp;
    private TemporalPlan plan;
    private int graphSize;
    private TreeMap<Double, Set<ExtIntOp>> orderedActions;
    private SimpleDirectedGraph<TemporalNode,TemporalEdge> tgraph;

    public TemporalGraph(CodedProblem cp, TemporalPlan plan) {
        this.cp = cp;
        this.plan = plan;
        tgraph = new SimpleDirectedGraph<>(TemporalEdge.class);
        orderedActions = new TreeMap<Double, Set<ExtIntOp>>();
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
                //System.out.println("adding action " + i.getName() + " with ids " + graphSize);
                tgraph.addEdge(actionStart,actionEnd, new TemporalEdge(i.getDuration(),i.getDuration(),2) );


                Set<ExtIntOp> buff = orderedActions.get(entry.getKey());
                if (buff == null) {
                    buff = new HashSet<ExtIntOp>();
                    this.orderedActions.put(entry.getKey(), buff);
                }
                buff.add(new ExtIntOp(i,1, graphSize - 2));

                buff = orderedActions.get(entry.getKey() + i.getDuration());
                if (buff == null) {
                    buff = new HashSet<ExtIntOp>();
                    this.orderedActions.put(entry.getKey() + i.getDuration(), buff);
                }
                buff.add(new ExtIntOp(i,2, graphSize - 1));

  
            }
        }

        addEdges(start);

        //System.out.println(tgraph.toString());

        //System.out.println(" nodes: " + tgraph.vertexSet().size() + " edges: " + tgraph.edgeSet().size());


        DOTExporter<TemporalNode, TemporalEdge> exporter =
                new DOTExporter<>(v -> v.getName().replace('(', '_').replace(')', '_').replace(',', '_') + "_id_" + v.getId());
        exporter.setVertexAttributeProvider((v) -> {
            Map<String, Attribute> map = new LinkedHashMap<>();
            map.put("label", DefaultAttribute.createAttribute(v.toString()));
            return map;
        });
        Writer writer = new StringWriter();
        exporter.exportGraph(tgraph, writer);
        //System.out.println(writer.toString());


    }

    public void addEdges (TemporalNode start) {

        Set<Double> keys = orderedActions.keySet();
        Iterator<Double> itk = keys.iterator();
        while(itk.hasNext()) {
            Double k = itk.next();
            Set<ExtIntOp> ita = orderedActions.get(k);
            Iterator<ExtIntOp> acs = ita.iterator();
            while (acs.hasNext()) {
                ExtIntOp action = acs.next();
                TemporalNode node = getNode(action.getId());
                boolean edge_created = false;
                switch (node.getType()) {

                    case 1:
                        Iterator<IntExp> cit = action.getAction().getPos_pred_atstart().iterator();
                        while (cit.hasNext()) {
                            IntExp prec = cit.next();
                            if (addConditionEdge(node, k, prec, action.getAction().getInstantiations(), false, false)) edge_created = true;
                        }
                        cit = action.getAction().getPos_pred_overall().iterator();
                        while (cit.hasNext()) {
                            IntExp prec = cit.next();
                            if (addConditionEdge(node, k, prec, action.getAction().getInstantiations(), false, true)) edge_created = true;
                        }
                        cit = action.getAction().getNeg_pred_atstart().iterator();
                        while (cit.hasNext()) {
                            IntExp prec = cit.next();
                            if (addConditionEdge(node, k, prec, action.getAction().getInstantiations(), true, false)) edge_created = true;
                        }
                        cit = action.getAction().getNeg_pred_overall().iterator();
                        while (cit.hasNext()) {
                            IntExp prec = cit.next();
                            if (addConditionEdge(node, k, prec, action.getAction().getInstantiations(), true, true)) edge_created = true;
                        }


                        break;

                    case 2:
                        Iterator<IntExp> cite = action.getAction().getPos_pred_atend().iterator();
                        while (cite.hasNext()) {
                            IntExp prec = cite.next();
                            if (addConditionEdge(node, k, prec, action.getAction().getInstantiations(), false, false)) edge_created = true;
                        }
                        cite = action.getAction().getNeg_pred_atend().iterator();
                        while (cite.hasNext()) {
                            IntExp prec = cite.next();
                            if (addConditionEdge(node, k, prec, action.getAction().getInstantiations(), true, false)) edge_created = true;
                        }
                        break;

                    default:

                        break;
                }

                addInterferenceEdges(action, node, k);


                if(!edge_created && node.getType() == 1) {
                    tgraph.addEdge(start, node, new TemporalEdge(Double.MIN_VALUE, Double.MAX_VALUE, 1) );
                }

            }
        }
    }

    public void addNode (TemporalNode node) {
        tgraph.addVertex(node);
        graphSize++;
    }

    public TemporalNode getNode (int id) {
        Set<TemporalNode> nodes = tgraph.vertexSet();
        Iterator<TemporalNode> itn = nodes.iterator();
        while(itn.hasNext()) {
            TemporalNode node = itn.next();
            if(node.getId() == id)
                return node;
        }
        return null;
    }

    public boolean addConditionEdge (TemporalNode node, Double time, IntExp cond, int [] instantiation, boolean negative, boolean overall) {
        //WARNING: NOT CLEAR IF I HAVE TO CHECK ALSO THE ACTION STARTING AT THE SAME TIME
        Set<Double> ks = orderedActions.keySet();
        List<Double> keys = new ArrayList<Double>(ks);
        ReverseListIterator itk = new ReverseListIterator(keys);
        while(itk.hasNext()) {
            Double k = (Double) itk.next();
            if (k <= time) {
                Set<ExtIntOp> ita = orderedActions.get(k);
                Iterator<ExtIntOp> acs = ita.iterator();
                while (acs.hasNext()) {
                    ExtIntOp action2 = acs.next();
                    if (action2.getId() != node.getId()) {
                        TemporalNode node2 = getNode(action2.getId());
                        if (satisfyPrecondition(cond, instantiation, action2.getAction(), node2.getType(), negative)) {
                            tgraph.addEdge(node2, node, new TemporalEdge(Double.MIN_VALUE, Double.MAX_VALUE, 1));
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }

    public boolean addInterferenceEdges (ExtIntOp action, TemporalNode node, Double time) {
        boolean edge_added = false;
        Set<Double> ks = orderedActions.keySet();
        List<Double> keys = new ArrayList<Double>(ks);
        ReverseListIterator itk = new ReverseListIterator(keys);
        while (itk.hasNext()) {
            Double k = (Double) itk.next();
            if ( k <= time) {
                Set<ExtIntOp> ita = orderedActions.get(k);
                Iterator<ExtIntOp> acs = ita.iterator();
                while (acs.hasNext()) {
                    ExtIntOp action2 = acs.next();
                    boolean interferes = false;
                    // for this pair of nodes check if current node interferes with previous node
                    TemporalNode node2 = null;
                    if (action2.getId() != node.getId()) {
                        node2 = getNode(action2.getId());
                        if(node2.getType() == 1) {
                            // determine if current_node negates at start condition of action start node
                            Iterator<IntExp> cit = action2.getAction().getPos_pred_atstart().iterator();
                            while (cit.hasNext()){
                                IntExp cond2 = cit.next();
                                interferes = interferes || satisfyPrecondition(cond2, action2.getAction().getInstantiations(), action.getAction(), action.getType(), true);
                            }
                            cit = action2.getAction().getNeg_pred_atstart().iterator();
                            while (cit.hasNext()){
                                IntExp cond2 = cit.next();
                                interferes = interferes || satisfyPrecondition(cond2, action2.getAction().getInstantiations(), action.getAction(), action.getType(), false);
                            }

                            // determine if previous node start effect interferes with current node effects
                            cit = action2.getAction().getPos_eff_atstart().iterator();
                            while (cit.hasNext()){
                                IntExp cond2 = cit.next();
                                interferes = interferes || satisfyPrecondition(cond2, action2.getAction().getInstantiations(), action.getAction(), action.getType(), true);
                            }
                            cit = action2.getAction().getNeg_eff_atstart().iterator();
                            while (cit.hasNext()){
                                IntExp cond2 = cit.next();
                                interferes = interferes || satisfyPrecondition(cond2, action2.getAction().getInstantiations(), action.getAction(), action.getType(), false);
                            }

                        } else if (node2.getType() == 2) {

                            // determine if current_node negates at end condition of action end node
                            Iterator<IntExp> cit = action2.getAction().getPos_pred_atend().iterator();
                            while (cit.hasNext()){
                                IntExp cond2 = cit.next();
                                interferes = interferes || satisfyPrecondition(cond2, action2.getAction().getInstantiations(), action.getAction(), action.getType(), true);
                            }
                            cit = action2.getAction().getNeg_pred_atend().iterator();
                            while (cit.hasNext()){
                                IntExp cond2 = cit.next();
                                interferes = interferes || satisfyPrecondition(cond2, action2.getAction().getInstantiations(), action.getAction(), action.getType(), false);
                            }

                            // determine if current_node negates over all condition of action end node
                            cit = action2.getAction().getPos_pred_overall().iterator();
                            while (cit.hasNext()){
                                IntExp cond2 = cit.next();
                                interferes = interferes || satisfyPrecondition(cond2, action2.getAction().getInstantiations(), action.getAction(), action.getType(), true);
                            }
                            cit = action2.getAction().getNeg_pred_overall().iterator();
                            while (cit.hasNext()){
                                IntExp cond2 = cit.next();
                                interferes = interferes || satisfyPrecondition(cond2, action2.getAction().getInstantiations(), action.getAction(), action.getType(), false);
                            }

                            // determine if previous node end effect interferes with current node effects
                            cit = action2.getAction().getPos_eff_atend().iterator();
                            while (cit.hasNext()){
                                IntExp cond2 = cit.next();
                                interferes = interferes || satisfyPrecondition(cond2, action2.getAction().getInstantiations(), action.getAction(), action.getType(), true);
                            }
                            cit = action2.getAction().getNeg_eff_atend().iterator();
                            while (cit.hasNext()){
                                IntExp cond2 = cit.next();
                                interferes = interferes || satisfyPrecondition(cond2, action2.getAction().getInstantiations(), action.getAction(), action.getType(), false);
                            }
                        }
                    }

                    if (interferes) {
                        tgraph.addEdge(node2, node, new TemporalEdge(Double.MIN_VALUE, Double.MAX_VALUE, 3));
                        edge_added = true;
                    }

                }
            }
        }
        return edge_added;
    }


    public boolean satisfyPrecondition(IntExp cond, int [] instantiation,  IntOp action, int type, boolean negative) {
        if (!negative) {
            if (type == 1) {                        //action start
                Iterator<IntExp> cit = action.getPos_eff_atstart().iterator();
                while(cit.hasNext()){
                    if (domainFormulaMatches(cond, cit.next(), instantiation, action.getInstantiations())) return true;
                }
            } else if (type == 2) {                 //action end
                Iterator<IntExp> cit = action.getPos_eff_atend().iterator();
                while(cit.hasNext()){
                    if (domainFormulaMatches(cond, cit.next(), instantiation, action.getInstantiations())) return true;
                }
            }

        } else {
            if (type == 1) {                        //action start
                Iterator<IntExp> cit = action.getNeg_eff_atstart().iterator();
                while(cit.hasNext()){
                    if (domainFormulaMatches(cond, cit.next(), instantiation, action.getInstantiations())) return true;
                }
            } else if (type == 2) {                 //action end
                Iterator<IntExp> cit = action.getNeg_eff_atend().iterator();
                while(cit.hasNext()){
                    if (domainFormulaMatches(cond, cit.next(), instantiation, action.getInstantiations())) return true;
                }
            }
        }


        return false;
    }



    public boolean domainFormulaMatches (IntExp cond1, IntExp cond2, int [] inst1, int [] inst2) {
        if (cond1.getPredicate() == cond2.getPredicate()) {  //same predicate
            int number_params = cond1.getArguments().length;
            int counter = 0;
            while (counter < number_params) {
                if (inst1[(Math.abs(cond1.getArguments()[counter]) - 1)] != inst2[(Math.abs(cond2.getArguments()[counter]) - 1)]  )
                    return false;                       //some of the grounded parameters are different
                counter++;
            }
            return true;
        }
        return false;
    }




    public void printOrderedActions () {
        Set<Double> keys = orderedActions.keySet();
        Iterator<Double> itk = keys.iterator();
        while(itk.hasNext()) {
            Double k = itk.next();
            Set<ExtIntOp> ita = orderedActions.get(k);
            Iterator<ExtIntOp> acs = ita.iterator();
            while(acs.hasNext()){
                ExtIntOp buff = acs.next();
                System.out.println("Time: " + k + " action: " + buff.getAction().getName() + " type: " + buff.getType() + " id: " + buff.getId());
            }
        }
    }

}
