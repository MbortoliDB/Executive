package planner;


import java.io.BufferedReader;
import java.io.InputStreamReader;

public class Planner {

    String domain;
    String problem;
    final String plannerBinary = "/home/mbortoli/Executive/src/main/resources/optic-cplex";

    public Planner (String domain, String problem){
        this.domain = domain;
        this.problem = problem;
    }

    public void plan (){
        String s;
        Process p;
        try {
            p = Runtime.getRuntime().exec(plannerBinary + " " + domain + " " + problem);
            BufferedReader br = new BufferedReader(
                    new InputStreamReader(p.getInputStream()));
            while ((s = br.readLine()) != null)
                System.out.println("line: " + s);
            p.waitFor();
            System.out.println ("exit: " + p.exitValue());
            p.destroy();
        } catch (Exception e) {}
    }


}
