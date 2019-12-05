import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Vector;

public class BroadcastThread implements Runnable {
    public Vector<String> messages;
    public ArrayList<OutputStream> outputStreams;

    public BroadcastThread(Vector vector, ArrayList arrayList) {
        this.messages = vector;
        this.outputStreams = arrayList;
    }

    public void run() {
        while (true) {
            // sleep for 1/10th of a second
            try {
                Thread.sleep(100);
                if (!messages.isEmpty()){
                    String message = messages.remove(0);
                    // System.out.println(message);
                }

            } catch (InterruptedException ignore) {
                System.out.println(ignore);
            }

            /**
             * check if there are any messages in the Vector. If so, remove them
             * and broadcast the messages to the chatroom
             */
        }
    }
} 