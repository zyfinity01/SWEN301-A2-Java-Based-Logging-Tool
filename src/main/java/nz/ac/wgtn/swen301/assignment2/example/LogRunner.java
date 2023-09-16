package nz.ac.wgtn.swen301.assignment2.example;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import java.util.Random;

public class LogRunner {
    private static final Logger logger = Logger.getLogger(LogRunner.class);

    public static void main(String[] args) {
        // run for 2 minutes (120 seconds)
        for (int i = 0; i < 120; i++) {
            // generate a random log level and message
            Level level = getRandomLogLevel();
            String message = "This is a " + level.toString() + " message";

            // log the message
            logger.log(level, message);

            // wait for 1 second
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private static Level getRandomLogLevel() {
        Level[] levels = {Level.DEBUG, Level.INFO, Level.WARN, Level.ERROR, Level.FATAL};
        Random random = new Random();
        int index = random.nextInt(levels.length);
        return levels[index];
    }
}
