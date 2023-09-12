package nz.ac.wgtn.swen301.assignment2;

import org.apache.log4j.pattern.LogEvent;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MemAppender {

    private List<LogEvent> events = new ArrayList<>();

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

    public void append(LogEvent event){
        if (events.size() < maxSize){
            events.add(event);
        } else {
            events.remove(0);
            discardedLogs++;
        }
    }

    public List<LogEvent> getCurrentLogs(){
        return Collections.unmodifiableList(events);
    }


}
