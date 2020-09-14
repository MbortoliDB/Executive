package general;

import encoding.CodedProblem;
import encoding.Encoder;
import parser.Domain;
import parser.Parser;

import java.io.FileNotFoundException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import parser.Problem;


public class MyMain {

    private static final Logger LOGGER = LogManager.getLogger(MyMain.class);

    public static void main(String[] args) {
        String domainS = args[0];
        String problemS = args[1];

        final StringBuilder strb = new StringBuilder();

        Parser parser = new Parser();


        parser.parserMain(domainS, problemS);

        LOGGER.trace("parsed\n");


        Problem p = parser.getProblem();

        LOGGER.trace(p.toString()+"\n");

        //Encoder.setLogLevel(2);

        final Domain domain = parser.getDomain();
        final Problem problem = parser.getProblem();
        CodedProblem cp;
        try {
            cp = Encoder.encode(domain, problem);
        } catch (IllegalArgumentException ilException) {
            LOGGER.error("the problem to encode is not ADL, \":requirements\" not supported at this time\n");
        }

    }

}