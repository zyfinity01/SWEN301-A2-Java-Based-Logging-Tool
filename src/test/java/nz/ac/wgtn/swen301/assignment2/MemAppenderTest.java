package nz.ac.wgtn.swen301.assignment2;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.apache.log4j.spi.LoggingEvent;
import org.junit.jupiter.api.Test;
import org.apache.log4j.Logger;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;


public class MemAppenderTest {


    private static final Logger logger = Logger.getLogger(MemAppenderTest.class);

    @Test
    public void testExportToJson(){
        MemAppender memAppender = new MemAppender();
        memAppender.setName("test1");
        logger.addAppender(memAppender);

        logger.info("This is an info message.");
        String fileName = "/test1.json";
        Path path = Paths.get(System.getProperty("user.dir") + fileName);
        System.out.println(path);

        memAppender.exportToJSON(fileName);
        assertTrue(Files.exists(path));
    }

    @Test
    public void testGetCurrentLogs(){
        MemAppender memAppender = new MemAppender();
        Logger logger = Logger.getLogger(MemAppenderTest.class);
        logger.addAppender(memAppender);

        logger.info("This is an info message.");
        logger.error("This is an error message.");

        List<LoggingEvent> logs = memAppender.getCurrentLogs();
        assertEquals(2, logs.size());
        assertEquals("This is an info message.", logs.get(0).getMessage());
        assertEquals("This is an error message.", logs.get(1).getMessage());
    }


    @Test
    public void testGetLogs(){
        MemAppender memAppender = new MemAppender();
        Logger logger = Logger.getLogger(MemAppenderTest.class);
        logger.addAppender(memAppender);

        logger.info("This is an info message.");
        logger.error("This is an error message.");

        String[] logs = memAppender.getLogs();
        assertEquals(2, logs.length);
    }

}
