import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class Reducer {
    private ServerSocket serverSocket;
    int workers = 0;
    int count = 0;
    ArrayList<Object> buffer = new ArrayList<>();
    private final Object reducerlock = new Object();

    public Reducer(ServerSocket serverSocket){
        this.serverSocket = serverSocket;
    }

    public void startServer() {
        try{

            while (!serverSocket.isClosed()) {

                Socket socket = serverSocket.accept();
                System.out.println("Connected ");
                ActionsForReducer actions = new ActionsForReducer(socket, this);

                Thread thread = new Thread(actions);
                synchronized (reducerlock) {
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
        ServerSocket serverSocket = new ServerSocket(12346);
        Reducer reducer = new Reducer(serverSocket);
        reducer.startServer();
    }

}
