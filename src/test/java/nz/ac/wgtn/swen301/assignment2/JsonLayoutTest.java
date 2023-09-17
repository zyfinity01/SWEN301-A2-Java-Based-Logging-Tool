package nz.ac.wgtn.swen301.assignment2;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.log4j.Category;
import org.apache.log4j.Priority;
import org.apache.log4j.spi.LocationInfo;
import org.apache.log4j.spi.LoggingEvent;
import org.apache.log4j.spi.ThrowableInformation;
import org.junit.jupiter.api.Test;
import org.apache.log4j.Level;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class JsonLayoutTest {

    @Test
    public void test1(){
        Category logger = Category.getInstance("foo");
        Instant instant = Instant.parse("2011-12-03T10:15:30Z");
        long timeStamp = instant.toEpochMilli();
        Level level = Level.WARN;
        Object message = "something went wrong";
        String threadName = "main";
        ThrowableInformation throwable = new ThrowableInformation(new Exception("Test exception"), logger);
        String ndc = "NDC";
        LocationInfo info = new LocationInfo(new Throwable("Test throwable"), "FQN");
        Map properties = new HashMap();
        LoggingEvent le = new LoggingEvent("FQN", logger, timeStamp, level, message, threadName, throwable, ndc, info, properties);
        String json = new JsonLayout().format(le);
        String expected = "{" +
                "\"name\":\"foo\"," +
                "\"level\":\"WARN\"," +
                "\"timestamp\":\"2011-12-03T10:15:30Z\"," +
                "\"thread\":\"main\"," +
                "\"message\":\"something went wrong\"" +
                "},";
        //System.out.println(json);
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode expectedNode = null;
        JsonNode actualNode = null;
        try {
            expectedNode = objectMapper.readTree(expected);
            actualNode = objectMapper.readTree(json);

        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        assertEquals(expectedNode, actualNode);
    }




    @Test
    public void test2(){
        Category logger = Category.getInstance("foo");
        Instant instant = Instant.parse("2011-12-03T10:15:30Z");
        long timeStamp = instant.toEpochMilli();
        Level level = Level.WARN;
        Object message = "something went wrong";
        String threadName = "main";
        ThrowableInformation throwable = new ThrowableInformation(new Exception("Test exception"), logger);
        String ndc = "NDC";
        LocationInfo info = new LocationInfo(new Throwable("Test throwable"), "FQN");
        Map properties = new HashMap();
        LoggingEvent le = new LoggingEvent("FQN", logger, timeStamp, level, message, threadName, throwable, ndc, info, properties);
        String json = new JsonLayout().format(le);
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode actualNode = null;
        try {
            actualNode = objectMapper.readTree(json);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        assertTrue(actualNode.has("name"));
        assertTrue(actualNode.has("level"));
        assertTrue(actualNode.has("timestamp"));
        assertTrue(actualNode.has("thread"));
        assertTrue(actualNode.has("message"));
        assertEquals("foo", actualNode.get("name").asText());
        assertEquals("WARN", actualNode.get("level").asText());
        assertEquals("2011-12-03T10:15:30Z", actualNode.get("timestamp").asText());
        assertEquals("main", actualNode.get("thread").asText());
        assertEquals("something went wrong", actualNode.get("message").asText());
    }

    @Test
    public void testEmptyValues(){
        Category logger = Category.getInstance("");
        long timeStamp = System.currentTimeMillis();
        Level level = Level.INFO;
        Object message = "";
        String threadName = "";
        LoggingEvent le = new LoggingEvent("FQN", logger, timeStamp, level, message, threadName, null, null, null, null);
        String json = new JsonLayout().format(le);
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode actualNode = null;
        try {
            actualNode = objectMapper.readTree(json);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        assertEquals("", actualNode.get("name").asText());
        assertEquals("INFO", actualNode.get("level").asText());
        assertTrue(actualNode.has("timestamp"));
        assertEquals("", actualNode.get("thread").asText());
        assertEquals("", actualNode.get("message").asText());
    }

    @Test
    public void testDifferentLogLevels(){
        for (Level level : new Level[] {Level.DEBUG, Level.INFO, Level.WARN, Level.ERROR, Level.FATAL}) {
            Category logger = Category.getInstance("foo");
            long timeStamp = System.currentTimeMillis();
            Object message = "something went wrong";
            String threadName = "main";
            LoggingEvent le = new LoggingEvent("FQN", logger, timeStamp, level, message, threadName, null, null, null, null);
            String json = new JsonLayout().format(le);
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode actualNode = null;
            try {
                actualNode = objectMapper.readTree(json);
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
            assertEquals("foo", actualNode.get("name").asText());
            assertEquals(level.toString(), actualNode.get("level").asText());
            assertTrue(actualNode.has("timestamp"));
            assertEquals("main", actualNode.get("thread").asText());
            assertEquals("something went wrong", actualNode.get("message").asText());
        }
    }


    @Test
    public void testLongMessage(){
        Category logger = Category.getInstance("foo");
        long timeStamp = System.currentTimeMillis();
        Level level = Level.WARN;
        Object message = "a".repeat(1000);
        String threadName = "main";
        LoggingEvent le = new LoggingEvent("FQN", logger, timeStamp, level, message, threadName, null, null, null, null);
        String json = new JsonLayout().format(le);
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode actualNode = null;
        try {
            actualNode = objectMapper.readTree(json);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        assertEquals("foo", actualNode.get("name").asText());
        assertEquals("WARN", actualNode.get("level").asText());
        assertTrue(actualNode.has("timestamp"));
        assertEquals("main", actualNode.get("thread").asText());
        assertEquals("a".repeat(1000), actualNode.get("message").asText());
    }


}

