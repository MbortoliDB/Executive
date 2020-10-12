package dispatcher;

import encoding.IntOp;
import planparser.TemporalGraph;
import util.IntExp;

import java.util.*;

public class Dispatcher {

    private double currentTime;
    private Timer timer;
    private TreeMap<Double, Set<ActionDispatch>> actionStarter;
    private TreeMap<Double, Set<ActionDispatch>> actionFinisher;
    private TemporalGraph tgraph;
    static Set<IntExp> kb;   //knowledge base, to be implemented in future with spring db to ensure synchronization

    public Dispatcher(Double currentTime,  TemporalGraph tgraph, TreeMap<Double, Set<IntOp>> plan, Set<IntExp> kb) {
        this.currentTime = currentTime;
        this.tgraph = tgraph;
        actionStarter = new TreeMap<>();
        actionFinisher = new TreeMap<>();
        this.kb = kb;

        for (Map.Entry<Double, Set<IntOp>> entry : plan.entrySet()) {
            Set<IntOp> seti = entry.getValue();
            Iterator<IntOp> setIt = seti.iterator();
            Set<ActionDispatch> toAdd = new HashSet<>();
            while (setIt.hasNext()) {
                IntOp i = setIt.next();
                toAdd.add(new ActionDispatch(entry.getKey(), i, 1));
                Set<ActionDispatch> toAddF;
                if(actionFinisher.containsKey(entry.getKey() + i.getDuration() )) {
                    toAddF = actionFinisher.get(entry.getKey() + i.getDuration());
                } else {
                    toAddF = new HashSet<>();
                }
                toAddF.add(new ActionDispatch(entry.getKey() + i.getDuration() , i, 2));
                actionStarter.put(entry.getKey() + i.getDuration() , toAddF);
            }
            actionStarter.put(entry.getKey(), toAdd);
        }



    }

    public void dispatch () {
        timer = new Timer();
        for (Map.Entry<Double, Set<ActionDispatch>> entry : actionStarter.entrySet()) {
            Set<ActionDispatch> seti = entry.getValue();
            Iterator<ActionDispatch> setIt = seti.iterator();
            while (setIt.hasNext()) {
                ActionDispatch i = setIt.next();
                timer.schedule(i,  Math.round(entry.getKey()*1000)) ;   //from seconds in double to ms in long
            }
        }

        /*
        for (Map.Entry<Double, Set<ActionDispatch>> entry : actionFinisher.entrySet()) {
            Set<ActionDispatch> seti = entry.getValue();
            Iterator<ActionDispatch> setIt = seti.iterator();
            while (setIt.hasNext()) {
                ActionDispatch i = setIt.next();
                timer.schedule(i,  Math.round(entry.getKey()*1000)) ;   //from seconds in double to ms in long
            }
        }*/
    }

    public void setCurrentTime(double currentTime) {
        this.currentTime = currentTime;
    }


    public void infoFromExecution () {
        //this method is called from the actual environment to communicate the result of an action dispatching or an unexpected event
        //it can be used to modify the actionDispatch object linked to the time listener, in order to cancel or delay the execution of an action
    }


    public void timerStop () {
        timer.cancel();
    }
}
