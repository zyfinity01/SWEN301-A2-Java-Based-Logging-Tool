package nz.ac.wgtn.swen301.assignment2;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.apache.log4j.spi.LoggingEvent;
import org.junit.jupiter.api.Test;
import org.apache.log4j.Logger;

import javax.management.MBeanServer;
import javax.management.MalformedObjectNameException;
import javax.management.ObjectName;
import java.lang.management.ManagementFactory;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


public class MemAppenderTest {


    private static final Logger logger = Logger.getLogger(MemAppenderTest.class);

    @Test
    public void testExportToJson(){
        MemAppender memAppender = new MemAppender();
        memAppender.setName("test1");
        logger.addAppender(memAppender);

        logger.info("This is an info message.");
        logger.info("This is another info message.");
        String fileName = "/test1.json";
        Path path = Paths.get(System.getProperty("user.dir") + fileName);

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


    @Test
    public void testGetLogCount(){
        MemAppender memAppender = new MemAppender();
        Logger logger = Logger.getLogger(MemAppenderTest.class);
        logger.addAppender(memAppender);

        logger.info("This is an info message.");
        logger.error("This is an error message.");

        assertEquals(2, memAppender.getLogCount());
    }

    @Test
    public void testGetDiscardedLogCount(){
        MemAppender memAppender = new MemAppender();
        memAppender.setMaxSize(1L);
        Logger logger = Logger.getLogger(MemAppenderTest.class);
        logger.addAppender(memAppender);

        logger.info("This is an info message.");
        logger.error("This is an error message.");

        assertEquals(1, memAppender.getDiscardedLogCount());
    }


    @Test
    public void testAppend(){
        MemAppender memAppender = new MemAppender();
        Logger logger = Logger.getLogger(MemAppenderTest.class);
        logger.addAppender(memAppender);

        logger.info("This is an info message.");

        List<LoggingEvent> logs = memAppender.getCurrentLogs();
        assertEquals(1, logs.size());
        assertEquals("This is an info message.", logs.get(0).getMessage());
    }


    @Test
    public void testRequiresLayout(){
        MemAppender memAppender = new MemAppender();
        assertFalse(memAppender.requiresLayout());
    }

    @Test
    public void testSetName(){
        MemAppender memAppender = new MemAppender();
        memAppender.setName("testName");
        assertEquals("testName", memAppender.getName());
    }


    @Test
    public void testSetMaxSize(){
        MemAppender memAppender = new MemAppender();
        memAppender.setMaxSize(500L);
        assertEquals(500L, memAppender.getMaxSize());
    }

    @Test
    public void testExportToJSONWithDiscardedLogs(){
        MemAppender memAppender = new MemAppender();
        memAppender.setName("test4");
        memAppender.setMaxSize(2L);
        Logger logger = Logger.getLogger(MemAppenderTest.class);
        logger.addAppender(memAppender);

        logger.info("This is an info message.");
        logger.error("This is an error message.");
        logger.info("This is another info message.");
        String fileName = "/test4.json";
        Path path = Paths.get(System.getProperty("user.dir") + fileName);

        memAppender.exportToJSON(fileName);
        assertTrue(Files.exists(path));
    }


    @Test
    public void testClose() {
        MemAppender memAppender = new MemAppender();
        memAppender.setName("testClose");
        Logger logger = Logger.getLogger(MemAppenderTest.class);
        logger.addAppender(memAppender);
        MBeanServer mbs = ManagementFactory.getPlatformMBeanServer();
        ObjectName objectName = null;
        try {
            objectName = new ObjectName("nz.ac.wgtn.swen301.assignment2:type=MemAppender,name=testClose");
        } catch (MalformedObjectNameException e) {
            e.printStackTrace();
        }

        // Verify that the MemAppender MBean is registered
        assertTrue(mbs.isRegistered(objectName));

        // Close the MemAppender
        memAppender.close();

        // Verify that the MemAppender MBean is unregistered
        assertFalse(mbs.isRegistered(objectName));

        logger.removeAppender(memAppender);
    }



}
