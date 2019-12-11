import java.io.IOException;
import java.io.*;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Vector;

public class BroadcastThread implements Runnable {
    public Vector<String> messages;
    public ArrayList<OutputStream> outputStreams;

    public BroadcastThread(Vector vector, ArrayList arrayList) {
        this.messages = vector;
        this.outputStreams = arrayList;
    }

    public void process() {
        PrintWriter pw = null;
        while (true) {
                if (!messages.isEmpty()){
                    String message = messages.remove(0);

                    for(OutputStream stream : outputStreams) {
                        pw = new PrintWriter(stream);
                        pw.println(message);
                        pw.flush();
                    }
                }
        }
    }

    public void run() {
        while (true) {
            // sleep for 1/10th of a second
            try { Thread.sleep(100); } catch (InterruptedException ignore) { }
            this.process();
        }
    }
} 