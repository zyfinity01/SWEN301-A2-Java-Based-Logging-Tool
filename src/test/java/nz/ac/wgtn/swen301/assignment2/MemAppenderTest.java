package nz.ac.wgtn.swen301.assignment2;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.jupiter.api.Test;
import org.apache.log4j.Logger;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.assertTrue;


public class MemAppenderTest {


    private static final Logger logger = Logger.getLogger(MemAppenderTest.class);

    @Test
    public void test1(){
        MemAppender memAppender = new MemAppender();
        memAppender.setName("test1");
        logger.addAppender(memAppender);

        logger.info("This is an info message.");
        String fileName = "/test1.json";
        Path path = Paths.get(System.getProperty("user.dir") + fileName);
        System.out.println(path);

        try {
            memAppender.exportToJson(fileName);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        assertTrue(Files.exists(path));
        }

}
