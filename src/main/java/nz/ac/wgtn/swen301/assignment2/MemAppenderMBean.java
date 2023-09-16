package nz.ac.wgtn.swen301.assignment2;

public interface MemAppenderMBean {
    String[] getLogs();
    long getLogCount();
    long getDiscardedLogCount();
    void exportToJSON(String fileName);
}
