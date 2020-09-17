package planner;


import util.TemporalPlan;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class Planner {

    private String domain;
    private String problem;
    private TemporalPlan plan;
    private final String plannerBinary = "/home/mbortoli/Executive/src/main/resources/optic-cplex";

    public Planner (String domain, String problem){
        this.domain = domain;
        this.problem = problem;
        plan = new TemporalPlan();
    }

    public void plan (){
        String s;
        Process p;
        boolean parseAction = false;
        try {
            p = Runtime.getRuntime().exec(plannerBinary + " " + domain + " " + problem);
            BufferedReader br = new BufferedReader(
                    new InputStreamReader(p.getInputStream()));
            while ((s = br.readLine()) != null) {
                String[] tokens = s.split(" ");
                if (tokens.length > 3)
                    if (tokens[1].equals("Plan") && tokens[2].equals("found") )
                        System.out.println("plan found");
                if (parseAction) {
                    if (s.equals(""))
                        parseAction = false;
                    else {
                        String [] parsed = s.split(":");
                        double time = Double.parseDouble(parsed[0]);
                        int actionIndex = parsed[1].indexOf(")") + 1;
                        String action = parsed[1].substring(1,actionIndex);
                        double duration = Double.parseDouble( parsed[1].substring(actionIndex + 3, parsed[1].length() - 1) );
                        System.out.println("duration " + duration);

                    }

                }


                if (tokens.length > 2)
                    if (tokens[1].equals("Dummy") && tokens[2].equals("steps:") )
                        parseAction = true;

                System.out.println("line: " + s);
            }
            p.waitFor();
            System.out.println ("exit: " + p.exitValue());
            p.destroy();
        } catch (Exception e) {}
    }


}
