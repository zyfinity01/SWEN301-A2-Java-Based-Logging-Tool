package nz.ac.wgtn.swen301.assignment2;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.log4j.pattern.LogEvent;
import org.apache.log4j.spi.LoggingEvent;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MemAppender {

    private List<LoggingEvent> events = new ArrayList<>();

    String name;
    Long maxSize = 1000L;
    Integer discardedLogs = 0;


    public MemAppender(String name) {
        this.name = name;
    }


    public MemAppender(String name, Long maxSize) {
        this.name = name;
        this.maxSize = maxSize;
    }

    public void append(LoggingEvent event){
        if (events.size() < maxSize){
            events.add(event);
        } else {
            events.remove(0);
            discardedLogs++;
        }
    }

    public List<LoggingEvent> getCurrentLogs(){
        return Collections.unmodifiableList(events);
    }

    public void exportToJson(String filename) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        List<JsonNode> jsonTree = new ArrayList<>();

        for(LoggingEvent event : events) {
            String json = new JsonLayout().format(event);
            JsonNode jNode = new ObjectMapper().readTree(json);
            jsonTree.add(jNode);
        }

        try {
            objectMapper.writeValue(new File(this.name + ".json"), jsonTree);
            System.out.println("JSON file created successfully");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
