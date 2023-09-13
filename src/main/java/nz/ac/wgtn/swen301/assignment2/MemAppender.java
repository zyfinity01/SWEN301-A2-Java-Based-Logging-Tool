package nz.ac.wgtn.swen301.assignment2;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.apache.log4j.AppenderSkeleton;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;
import org.apache.log4j.spi.LoggingEvent;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MemAppender extends AppenderSkeleton {

    private List<LoggingEvent> events = new ArrayList<>();

    String name;
    Long maxSize = 1000L;
    Integer discardedLogs = 0;



    public MemAppender() {
        this.setLayout(new PatternLayout("%d{yyyy-MM-dd HH:mm:ss} %-5p %c{1}:%L - %m%n"));
    }


    public void setMaxSize(Long maxSize) {
        this.maxSize = maxSize;
    }

    @Override
    public void setName(String name) {
        this.name = name;
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
        Path path = Paths.get(filename);
        if (path.isAbsolute()) {
            System.out.println("The input is an absolute path.");
        } else if (path.getNameCount() > 1) {
            System.out.println("The input is a relative path.");
        } else {
            System.out.println("The input is a plain filename");
            filename = System.getProperty("user.dir") + filename;
        }
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

    @Override
    public void close() {

    }

    @Override
    public boolean requiresLayout() {
        return false;
    }
}
