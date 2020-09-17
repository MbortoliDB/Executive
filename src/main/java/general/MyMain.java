package general;

import encoding.CodedProblem;
import encoding.Encoder;
import parser.*;

import java.io.FileNotFoundException;
import java.util.Iterator;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import planner.Planner;
import util.BitOp;


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
            List<Op> op = domain.getOperators();
            Iterator<Op> it = op.iterator();
            while (it.hasNext())
               LOGGER.trace("name " + it.next().getParameters() + "\n");



        } catch (IllegalArgumentException ilException) {
            LOGGER.error("the problem to encode is not ADL, \":requirements\" not supported at this time\n");
        }



        Planner pl = new Planner(domainS,problemS);
        //pl.plan();

    }

}