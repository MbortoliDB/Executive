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


        if (type==1) {
            List<IntExp> pos_start = action.getPos_pred_atstart();
            Iterator<IntExp> pre_iterator = pos_start.iterator();
            while (pre_iterator.hasNext()) {
                IntExp e = pre_iterator.next();
                int[] arg = new int[e.getArguments().length];
                int x;
                for (int i = 0; i < arg.length; i++) {
                    arg[i] = action.getInstantiations()[Math.abs(e.getArguments()[i]) - 1];
                }
                e.setArguments(arg);
            }

            List<IntExp> neg_start = action.getNeg_pred_atstart();
            pre_iterator = neg_start.iterator();
            while (pre_iterator.hasNext()) {
                IntExp e = pre_iterator.next();
                int[] arg = new int[e.getArguments().length];
                int x;
                for (int i = 0; i < arg.length; i++) {
                    arg[i] = action.getInstantiations()[Math.abs(e.getArguments()[i]) - 1];
                }
                e.setArguments(arg);
            }

            List<IntExp> pos_start_eff = action.getPos_eff_atstart();
            pre_iterator = pos_start_eff.iterator();
            while (pre_iterator.hasNext()) {
                IntExp e = pre_iterator.next();
                int[] arg = new int[e.getArguments().length];
                int x;
                for (int i = 0; i < arg.length; i++) {
                    arg[i] = action.getInstantiations()[Math.abs(e.getArguments()[i]) - 1];
                }
                e.setArguments(arg);
            }

            List<IntExp> neg_start_eff = action.getNeg_eff_atstart();
            pre_iterator = neg_start_eff.iterator();
            while (pre_iterator.hasNext()) {
                IntExp e = pre_iterator.next();
                int[] arg = new int[e.getArguments().length];
                int x;
                for (int i = 0; i < arg.length; i++) {
                    arg[i] = action.getInstantiations()[Math.abs(e.getArguments()[i]) - 1];
                }
                e.setArguments(arg);
            }

            List<IntExp> pos_overall = action.getPos_pred_overall();
            pre_iterator = pos_overall.iterator();
            while (pre_iterator.hasNext()) {
                IntExp e = pre_iterator.next();
                int[] arg = new int[e.getArguments().length];
                int x;
                for (int i = 0; i < arg.length; i++) {
                    arg[i] = action.getInstantiations()[Math.abs(e.getArguments()[i]) - 1];
                }
                e.setArguments(arg);
            }



            List<IntExp> neg_overall = action.getNeg_pred_overall();
            pre_iterator = neg_overall.iterator();
            while (pre_iterator.hasNext()) {
                IntExp e = pre_iterator.next();
                int[] arg = new int[e.getArguments().length];
                int x;
                for (int i = 0; i < arg.length; i++) {
                    arg[i] = action.getInstantiations()[Math.abs(e.getArguments()[i]) - 1];
                }
                e.setArguments(arg);
            }




        } else if (type == 2) {

            List<IntExp> pos_end = action.getPos_pred_atend();
            Iterator<IntExp> pre_iterator = pos_end.iterator();
            while (pre_iterator.hasNext()) {
                IntExp e = pre_iterator.next();
                int[] arg = new int[e.getArguments().length];
                int x;
                for (int i = 0; i < arg.length; i++) {
                    arg[i] = action.getInstantiations()[Math.abs(e.getArguments()[i]) - 1];
                }
                e.setArguments(arg);
            }

            List<IntExp> neg_end = action.getNeg_pred_atend();
            pre_iterator = neg_end.iterator();
            while (pre_iterator.hasNext()) {
                IntExp e = pre_iterator.next();
                int[] arg = new int[e.getArguments().length];
                int x;
                for (int i = 0; i < arg.length; i++) {
                    arg[i] = action.getInstantiations()[Math.abs(e.getArguments()[i]) - 1];
                }
                e.setArguments(arg);
            }

            List<IntExp> pos_end_eff = action.getPos_eff_atend();
            pre_iterator = pos_end_eff.iterator();
            while (pre_iterator.hasNext()) {
                IntExp e = pre_iterator.next();
                int[] arg = new int[e.getArguments().length];
                int x;
                for (int i = 0; i < arg.length; i++) {
                    arg[i] = action.getInstantiations()[Math.abs(e.getArguments()[i]) - 1];
                }
                e.setArguments(arg);
            }

            List<IntExp> neg_end_eff = action.getNeg_eff_atend();
            pre_iterator = neg_end_eff.iterator();
            while (pre_iterator.hasNext()) {
                IntExp e = pre_iterator.next();
                int[] arg = new int[e.getArguments().length];
                int x;
                for (int i = 0; i < arg.length; i++) {
                    arg[i] = action.getInstantiations()[Math.abs(e.getArguments()[i]) - 1];
                }
                e.setArguments(arg);
            }
        }

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
