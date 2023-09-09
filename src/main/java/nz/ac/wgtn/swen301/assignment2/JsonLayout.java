package nz.ac.wgtn.swen301.assignment2;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.log4j.Layout;
import org.apache.log4j.spi.LoggingEvent;

import java.io.IOException;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

public class JsonLayout extends Layout {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public String format(LoggingEvent event) {
        Map<String, String> jsonMap = new HashMap<>();
        jsonMap.put("name", event.getLoggerName());
        jsonMap.put("level", event.getLevel().toString());
        jsonMap.put("timestamp", convertTimestamp(event.getTimeStamp()));
        jsonMap.put("thread", event.getThreadName());
        jsonMap.put("message", event.getMessage().toString());

        try {
            return objectMapper.writeValueAsString(jsonMap) + ",";
        } catch (IOException e) {
            throw new RuntimeException("Could not convert log event to JSON", e);
        }
    }

    private String convertTimestamp(long timestamp) {
        Instant instant = Instant.ofEpochMilli(timestamp);
        return DateTimeFormatter.ISO_INSTANT
                .withZone(ZoneId.of("UTC"))
                .format(instant);
    }

    @Override
    public boolean ignoresThrowable() {
        return false;
    }

    @Override
    public void activateOptions() {
        // no options to activate
    }
}
