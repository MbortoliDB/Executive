package general;

import lineardispatcher.Dispatcher;
import encoding.CodedProblem;
import encoding.Encoder;
import encoding.IntOp;
import parser.*;

import java.io.FileNotFoundException;
import java.util.Iterator;
import java.util.Set;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import planner.Planner;
import planparser.TemporalGraph;
import util.IntExp;


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


            Planner pl = new Planner(domainS,problemS,cp);
            pl.plan();

            StringBuilder b = new StringBuilder();
            Encoder.printTableOfPredicates(b);
            //LOGGER.trace("+++++" + b);

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


            Set<IntExp> init = cp.getInit();
            Iterator i = init.iterator();
            //while(i.hasNext()){
            //}


            //LOGGER.trace(init);

            //do not cancel, linear dispatcher
            Dispatcher d = new Dispatcher(0.0, tg, pl.getPlan().actions(), cp.getInit());
            d.dispatch();
            //d.generatorUnexpEvents();




        } catch (IllegalArgumentException ilException) {
            LOGGER.error("the problem to encode is not ADL, \":requirements\" not supported at this time\n");
        }





    }

}