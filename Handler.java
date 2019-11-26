
import java.io.IOException;
import java.util.ArrayList;

public class Handler {
    ArrayList currentConnections;

    public Handler() {
        currentConnections = new ArrayList<Connection>();
    }
    public void addConnection(Connection connection) {
        currentConnections.add(connection);
    }

    public void printEverything() {
        for (Object runnable: currentConnections) {
            System.out.println(runnable.toString());
        }
    }

    public void broadcastMessage(String message) throws IOException {
        try {
            for (Object runnable: currentConnections) {
                Connection connection = (Connection) runnable;
                connection.sendMessage(message); 
            }
            
        } catch (IOException ioe) {
            System.out.println(ioe);
        }

    }
}