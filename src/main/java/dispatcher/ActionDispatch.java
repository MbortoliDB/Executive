package dispatcher;

import encoding.IntOp;
import util.IntExp;

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
        }
    }

    @Override
    public void run() {
        if (type == 1) {
            //checking pos atstart precondition
            List<IntExp> pos_start = action.getPos_pred_atstart();
            boolean satisfied = true;
            if(!Dispatcher.kb.containsAll(pos_start))
                satisfied = false;
            //checking overall pos precondition
            List<IntExp> pos_overall = action.getPos_pred_overall();
            if(!Dispatcher.kb.containsAll(pos_overall))
                satisfied = false;
            //checking neg atstart precondition
            List<IntExp> neg_start = action.getNeg_pred_atstart();
            Iterator<IntExp> kb_iterator = Dispatcher.kb.iterator();
            Iterator<IntExp> pre_iterator = neg_start.iterator();
            while(pre_iterator.hasNext() && satisfied){
                IntExp prec_exp = pre_iterator.next();
                IntExp kb_exp = null;
                boolean prec_satisfied = true;
                while(kb_iterator.hasNext() && !prec_satisfied){
                    kb_exp = kb_iterator.next();
                    prec_satisfied = kb_exp.equals(prec_exp);
                }
                if (!prec_satisfied) {
                    satisfied = false;
                }
            }
            //checking neg overall precondition
            List<IntExp> neg_overall = action.getNeg_pred_overall();
            kb_iterator = Dispatcher.kb.iterator();
            pre_iterator = neg_overall.iterator();
            while(pre_iterator.hasNext() && satisfied){
                IntExp prec_exp = pre_iterator.next();
                IntExp kb_exp = null;
                boolean prec_satisfied = true;
                while(kb_iterator.hasNext() && !prec_satisfied){
                    kb_exp = kb_iterator.next();
                    prec_satisfied = kb_exp.equals(prec_exp);
                }
                if (!prec_satisfied) {
                    satisfied = false;
                }
            }
            if (satisfied)
                System.out.println("Time: " + time + " - Dispacting action " + action);
            else
                System.out.println("Time: " + time + " - action " + action + " does not satisfy precondition");
        } else if (type == 2) { //end action
            //checking pos atend precondition
            List<IntExp> pos_end = action.getPos_pred_atend();
            boolean satisfied = true;
            if(!Dispatcher.kb.containsAll(pos_end))
                satisfied = false;
            //checking overall pos precondition
            List<IntExp> pos_overall = action.getPos_pred_overall();
            if(!Dispatcher.kb.containsAll(pos_overall))
                satisfied = false;
            //checking neg end precondition
            List<IntExp> neg_end = action.getNeg_pred_atend();
            Iterator<IntExp> kb_iterator = Dispatcher.kb.iterator();
            Iterator<IntExp> pre_iterator = neg_end.iterator();
            while(pre_iterator.hasNext() && satisfied){
                IntExp prec_exp = pre_iterator.next();
                IntExp kb_exp = null;
                boolean prec_satisfied = true;
                while(kb_iterator.hasNext() && !prec_satisfied){
                    kb_exp = kb_iterator.next();
                    prec_satisfied = kb_exp.equals(prec_exp);
                }
                if (!prec_satisfied) {
                    satisfied = false;
                }
            }
            //checking neg overall precondition
            List<IntExp> neg_overall = action.getNeg_pred_overall();
            kb_iterator = Dispatcher.kb.iterator();
            pre_iterator = neg_overall.iterator();
            while(pre_iterator.hasNext() && satisfied){
                IntExp prec_exp = pre_iterator.next();
                IntExp kb_exp = null;
                boolean prec_satisfied = true;
                while(kb_iterator.hasNext() && !prec_satisfied){
                    kb_exp = kb_iterator.next();
                    prec_satisfied = kb_exp.equals(prec_exp);
                }
                if (!prec_satisfied) {
                    satisfied = false;
                }
            }
            if (satisfied)
                System.out.println("Time: " + time + " - Finishing action " + action);
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
