import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Master {

    private ServerSocket serverSocket;
    ArrayList<Integer> workers = new ArrayList<>();
    Map<String, ObjectOutputStream> responseMap = new HashMap<>();
    private final Object masterlock = new Object();

    public Master(ServerSocket serverSocket){
        this.serverSocket = serverSocket;
    }

    public void startServer() {
        try{
            while (!serverSocket.isClosed()) {
                Socket socket = serverSocket.accept();
                System.out.println("Connected!");
                ActionsForMaster actions = new ActionsForMaster(socket, this);

                Thread thread = new Thread(actions);
                synchronized (masterlock) {
                    thread.start();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void closeServer(){
        try{
            if (serverSocket != null){
                serverSocket.close();
            }
        } catch (IOException e){
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(12345);
        Master master = new Master(serverSocket);
        master.startServer();
    }

}
