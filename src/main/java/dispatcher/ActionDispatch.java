package dispatcher;

import encoding.IntOp;

import java.util.TimerTask;

public class ActionDispatch extends TimerTask {

    Double time;
    IntOp action;
    int type; //1=start 2=end

    public ActionDispatch(Double time, IntOp action, int type) {
        this.time = time;
        this.action = action;
        this.type = type;
    }

    @Override
    public void run() {
        if (type == 1)
            System.out.println("Time: " + time + " - Dispacting action " + action);
        else
            System.out.println("Time: " + time + " - Finishing action " + action);
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
