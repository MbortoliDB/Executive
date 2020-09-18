package planner;


import encoding.CodedProblem;
import encoding.IntOp;
import util.TemporalPlan;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.*;

public class Planner {

    private String domain;
    private String problem;
    private TemporalPlan plan;
    private CodedProblem cp;
    private final String plannerBinary = "/home/mbortoli/Executive/src/main/resources/optic-cplex";

    public Planner (String domain, String problem, CodedProblem cp){
        this.domain = domain;
        this.problem = problem;
        this.cp = cp;
        plan = new TemporalPlan();
    }

    public void plan (){
        String s;
        Process p;
        boolean parseAction = false;
        boolean finish = false;

        try {
            p = Runtime.getRuntime().exec(plannerBinary + " " + domain + " " + problem);
            BufferedReader br = new BufferedReader(
                    new InputStreamReader(p.getInputStream()));

            List<IntOp> op = cp.getOperators();
            List<String> constants = cp.getConstants();

            while ((s = br.readLine()) != null && !finish ) {
                String[] tokens = s.split(" ");
                if (tokens.length > 3)
                    if (tokens[1].equals("Plan") && tokens[2].equals("found") )
                        System.out.println("plan found");
                if (parseAction) {
                    if (s.equals("")) {
                        parseAction = false;
                        finish = true;
                    }
                    else {
                        String [] parsed = s.split(":");
                        double time = Double.parseDouble(parsed[0]);
                        int actionIndex = parsed[1].indexOf(")") + 1;
                        String action = parsed[1].substring(1,actionIndex);
                        double duration = Double.parseDouble( parsed[1].substring(actionIndex + 3, parsed[1].length() - 1) );
                        String actionName = action.split(" ")[0].substring(1);
                        //adding grounded actions with time and duration
                        Iterator<IntOp> it = op.iterator();
                        IntOp grAction = null;
                        while (it.hasNext()) {
                            IntOp i = it.next();
                            if (i.getName().equals(actionName)) {
                                grAction = new IntOp(i);
                                String [] paramParsing = action.substring(0,action.length() - 1).split(" ");
                                String [] parameters = Arrays.copyOfRange(paramParsing, 1, paramParsing.length);
                                for (int j = 0; j < grAction.getInstantiations().length; j++) {
                                    grAction.setValueOfParameter(j, constants.indexOf(parameters[j]));
                                }
                                grAction.setDuration(duration);
                                plan.add(time, grAction);
                            }
                        }
                    }
                }
                if (tokens.length > 2)
                    if (tokens[1].equals("Dummy") && tokens[2].equals("steps:") )
                        parseAction = true;

                System.out.println("line: " + s);
            }
            //p.waitFor();
            System.out.println ("exit: " + p.exitValue());
            p.destroy();
        } catch (Exception e) {}




    }

    public void printPlan () {
        TreeMap<Double, Set<IntOp>> actions = plan.actions();
        for (Map.Entry<Double, Set<IntOp>> entry : actions.entrySet()) {
            System.out.print("Time: " + entry.getKey() + " ");
            Set<IntOp> seti = entry.getValue();
            Iterator<IntOp> setIt = seti.iterator();
            while(setIt.hasNext()){
                IntOp i = setIt.next();
                System.out.print("name " + i.getName() + " ");
                for(int j=0; j < i.getInstantiations().length; j++)
                    System.out.print(i.getInstantiations()[j] + " ");
                System.out.print(" duration " + i.getDuration() + "\n");
            }
        }
    }

    public TemporalPlan getPlan () {
        return this.plan;
    }


}
