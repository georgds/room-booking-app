import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Worker {

    ServerSocket serverSocket;
    ArrayList<Room> rooms = new ArrayList<>();
    private final Object workerlock = new Object();
    int port;

    public Worker(ServerSocket serverSocket, int port){
        this.serverSocket = serverSocket;
        this.port = port;
    }

    public void startServer() {
        try{
            while (true) {

                Socket socket = serverSocket.accept();
                System.out.println("Connected");
                ActionsForWorker actions = new ActionsForWorker(socket, this);

                Thread thread = new Thread(actions);
                synchronized (workerlock) {
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

    public static void sayHiMaster(int port) throws IOException {
        Socket socket = new Socket("localhost", 12345);
        List<Object> hiMessage = new ArrayList<>();
        hiMessage.add("NEW_WORKER");
        hiMessage.add(port);
        try {
            ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream in = new ObjectInputStream(socket.getInputStream());

            out.writeObject(hiMessage);
            out.close();
            in.close();
            socket.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void sayHiReducer(int port) throws IOException {
        Socket socket = new Socket("localhost", 12346);
        List<Object> hiMessage = new ArrayList<>();
        hiMessage.add("NEW_WORKER");
        hiMessage.add(port);
        try {
            ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream in = new ObjectInputStream(socket.getInputStream());

            out.writeObject(hiMessage);
            out.close();
            in.close();
            socket.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws IOException {
        Random random = new Random();
        int port = random.nextInt(1000) + 10000;
        sayHiMaster(port);
        sayHiReducer(port);
        ServerSocket serverSocket = new ServerSocket(port);
        Worker worker = new Worker(serverSocket, port);
        worker.startServer();
    }
}
