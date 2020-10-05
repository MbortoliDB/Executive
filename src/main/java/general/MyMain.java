package general;

import encoding.CodedProblem;
import encoding.Encoder;
import encoding.IntOp;
import parser.*;

import java.io.FileNotFoundException;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import planner.Planner;
import sun.awt.geom.AreaOp;
import temporalgraph.TemporalGraph;
import util.BitOp;
import util.TemporalPlan;


public class MyMain {

    private static final Logger LOGGER = LogManager.getLogger(MyMain.class);

    public static void main(String[] args) {
        String domainS = args[0];
        String problemS = args[1];

        final StringBuilder strb = new StringBuilder();

        Parser parser = new Parser();


        //parser.parserMain(domainS, problemS);

        try {
            parser.parse(domainS,problemS);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        LOGGER.trace("parsed\n");


        Problem p = parser.getProblem();

        Exp cons = p.getConstraints();

        //LOGGER.trace("constr" + cons.toString() +"\n");

        LOGGER.trace(p.toString()+"\n");

        //Encoder.setLogLevel(2);

        final Domain domain = parser.getDomain();
        final Problem problem = parser.getProblem();
        CodedProblem cp;
        try {
           cp = Encoder.encode(domain, problem);
            List<IntOp> op = cp.getOperators();
            Iterator<IntOp> it = op.iterator();
            IntOp n = null;
            while (it.hasNext()) {
                IntOp i = it.next();
                if (i.getName().equals("toy1"));
                    n = new IntOp(i);
                //LOGGER.trace("name " + i.getName() + " ");
                //for(int j=0; j < i.getInstantiations().length; j++)
                  //  LOGGER.trace(i.getInstantiations()[j] + " ");
                //LOGGER.trace("\n");
            }

            Planner pl = new Planner(domainS,problemS,cp);
            pl.plan();

            StringBuilder b = new StringBuilder();
            Encoder.printTableOfPredicates(b);
            LOGGER.trace("+++++" + b);

            Set<Double> keys = pl.getPlan().actions().keySet();
            Iterator<Double> itk = keys.iterator();
            while(itk.hasNext()) {
                Set<IntOp> ita = pl.getPlan().getActionSet(itk.next());
                Iterator<IntOp> acs = ita.iterator();
                while(acs.hasNext()){
                    //LOGGER.trace("preconditions " + acs.next().getEffects().getConnective().toString() + "\n");
                    acs.next().generateLists();
                }
            }



            LOGGER.trace("Generating temporal graph \n");

            //pl.printPlan();


            TemporalGraph tg = new TemporalGraph(cp,pl.getPlan());
            tg.createTemporalGraph();

            //plan.add(10,n);

            //pl.printPlan();



            /*
            StringBuilder stringBuilder = new StringBuilder();
            Encoder.printTableOfConstants(stringBuilder);
            LOGGER.trace("predicates table " + stringBuilder);
            */



            //LOGGER.trace("plan " + s);

            //LOGGER.trace("objects" + cp.getConstants() + "\n");



        } catch (IllegalArgumentException ilException) {
            LOGGER.error("the problem to encode is not ADL, \":requirements\" not supported at this time\n");
        }





    }

}