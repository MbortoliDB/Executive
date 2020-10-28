package lineardispatcher;

import encoding.IntOp;
import util.IntExp;
import util.KnowledgeBase;

import java.util.Iterator;
import java.util.List;
import java.util.TimerTask;

public class ActionDispatch extends TimerTask {

    Double time;
    IntOp action;
    int type; //1=start 2=end

    public ActionDispatch(Double time, IntOp action, int type) {
        this.time = time;
        this.action = action;
        this.type = type;

        //this.action = KnowledgeBase.normalizeAction(action, type);

        KnowledgeBase.normalizeAction(action, type);
    }

    @Override
    public void run() {
        if (type == 1) {
            boolean satisfied = KnowledgeBase.checkPrecondStart(action);
            if (satisfied) {
                System.out.println("Time: " + time + " Timer: " + (System.currentTimeMillis() - Dispatcher.timerStarting) +  " - Dispacting action " + action);
                KnowledgeBase.updateKB(1, action);
            } else
                System.out.println("Time: " + time + " - action " + action + " does not satisfy starting precondition");

        } else if (type == 2) { //end action
            boolean satisfied = KnowledgeBase.checkPrecondEnd(action);
            if (satisfied) {
                KnowledgeBase.updateKB(2, action);
                System.out.println("Time: " + time + " Timer: " + (System.currentTimeMillis() - Dispatcher.timerStarting) + " - Finishing action " + action);
            } else
                System.out.println("Time: " + time + " - action " + action + " does not satisfy ending precondition");
        }
    }



    public Double getTime() {
        return time;
    }

    public void setTime(Double time) {
        this.time = time;
    }

    public IntOp getAction() {
        return action;
    }

    public void setAction(IntOp action) {
        this.action = action;
    }
}
