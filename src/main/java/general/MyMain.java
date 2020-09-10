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

        Parser parser = new Parser();
        try {
            parser.parse(domainS, problemS);
        } catch (FileNotFoundException fnfException) {
            LOGGER.error("parsing problem error", fnfException);
        }

        LOGGER.trace("parsed\n");

        Encoder.setLogLevel(2);

        final Domain domain = parser.getDomain();
        final Problem problem = parser.getProblem();
        CodedProblem cp;
        try {
            cp = Encoder.encode(domain, problem);
        } catch (IllegalArgumentException ilException) {
            LOGGER.error("the problem to encode is not ADL, \":requirements\" not supported at this time\n");
        }

        System.out.println("working");



        LOGGER.trace("working");

    }

}