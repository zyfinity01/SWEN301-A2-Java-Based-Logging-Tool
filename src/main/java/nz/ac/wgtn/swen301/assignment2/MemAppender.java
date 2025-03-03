package nz.ac.wgtn.swen301.assignment2;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.apache.log4j.AppenderSkeleton;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;
import org.apache.log4j.spi.LoggingEvent;

import javax.management.*;
import java.io.File;
import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MemAppender extends AppenderSkeleton implements MemAppenderMBean {

    private final List<LoggingEvent> events = new ArrayList<>();

    String name;
    Long maxSize;
    Integer discardedLogs = 0;



    public MemAppender() {
        this.maxSize = 1000L;
        this.setLayout(new PatternLayout("%d{yyyy-MM-dd HH:mm:ss} %-5p %c{1}:%L - %m%n"));
    }


    public void setMaxSize(Long maxSize) {
        this.maxSize = maxSize;
    }

    public Long getMaxSize() {
        return maxSize;
    }

    @Override
    public void setName(String name) {
        super.setName(name);
        MBeanServer mbs = ManagementFactory.getPlatformMBeanServer();
        ObjectName objectName = null;
        try {
            objectName = new ObjectName("nz.ac.wgtn.swen301.assignment2:type=MemAppender,name=" + this.getName());
        } catch (MalformedObjectNameException e) {
            e.printStackTrace();
        }
        try {
            mbs.registerMBean(this, objectName);
        } catch (InstanceAlreadyExistsException | MBeanRegistrationException | NotCompliantMBeanException e) {
            e.printStackTrace();
        }
    }


    public String[] getLogs() {
        String[] logs = new String[events.size()];
        for (int i = 0; i < events.size(); i++) {
            logs[i] = new JsonLayout().format(events.get(i));
        }
        return logs;
    }

    public long getLogCount() {
        return events.size();
    }

    public long getDiscardedLogCount() {
        return discardedLogs;
    }

    public void append(LoggingEvent event){
        System.out.println(new JsonLayout().format(event));
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

    public void exportToJSON(String filename) {
        filename = System.getProperty("user.dir") + filename;
        System.out.println(filename);
        ObjectMapper objectMapper = new ObjectMapper();
        List<JsonNode> jsonTree = new ArrayList<>();

        for(LoggingEvent event : events) {
            String json = new JsonLayout().format(event);
            JsonNode jNode = null;
            try {
                jNode = new ObjectMapper().readTree(json);
                System.out.println(jNode);
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
            jsonTree.add(jNode);
        }

        try {
            objectMapper.writeValue(new File(filename), jsonTree);
            System.out.println("JSON file created successfully");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void close() {
        MBeanServer mbs = ManagementFactory.getPlatformMBeanServer();
        ObjectName objectName;
        try {
            objectName = new ObjectName("nz.ac.wgtn.swen301.assignment2:type=MemAppender,name=" + this.getName());
            mbs.unregisterMBean(objectName);
        } catch (MalformedObjectNameException | InstanceNotFoundException | MBeanRegistrationException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean requiresLayout() {
        return false;
    }
}
