package nz.ac.wgtn.swen301.assignment2;
import org.apache.log4j.pattern.LogEvent;
import java.util.ArrayList;
import java.util.List;

public class MemAppender {

    private List<LogEvent> events = new ArrayList<>();

    String name;



    public MemAppender(String name) {
        this.name = name;
    }

}
