import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ActionsForMaster extends Thread {

    private Socket clientSocket;
    Master master;
    ObjectInputStream in;
    ObjectOutputStream out;

    public ActionsForMaster(Socket clientSocket, Master master) {
        try {
            this.clientSocket = clientSocket;
            this.master = master;
            this.out = new ObjectOutputStream(clientSocket.getOutputStream());
            this.in = new ObjectInputStream(clientSocket.getInputStream());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void run() {
        try {
            Object message;
            while ((message = in.readObject()) != null) {

                ArrayList<Object> requestList = ((ArrayList<Object>) message);
                List<Object> workerRequest = new ArrayList<>();
                String answer = "";
                if (requestList.get(0).equals("NEW_WORKER")) {

                    int port = (int) requestList.get(1);
                    master.workers.add(port);

                } else if (requestList.get(0).equals("Manager")){

                    if (requestList.get(1).equals("1")){
                        //Add rooms
                        String path = (String) requestList.get(2);
                        JsonHandler jsonHandler = new JsonHandler();
                        List<Room> Roomlist = jsonHandler.readRooms(path);
                        String requestId = UUID.randomUUID().toString();
                        synchronized (master.responseMap) {
                            master.responseMap.put(requestId, out);
                        }
                        for (Room room : Roomlist) {
                            workerRequest.add("1");
                            workerRequest.add(requestId);
                            workerRequest.add(Roomlist.size());
                            workerRequest.add(room);
                            String name = room.getRoomName();
                            int port = selectWorkerNode(name);
                            sendToWorker(workerRequest, port);
                            workerRequest.clear();
                        }

                    }else if (requestList.get(1).equals("2")){
                        //Add dates
                        workerRequest.add("2");
                        String requestId = UUID.randomUUID().toString();
                        synchronized (master.responseMap) {
                            master.responseMap.put(requestId, out);
                        }
                        workerRequest.add(requestId);

                        workerRequest.add(requestList.get(2));
                        workerRequest.add(requestList.get(3));
                        workerRequest.add(requestList.get(4));

                        int port = selectWorkerNode((String) requestList.get(2));
                        sendToWorker(workerRequest, port);

                    }else if (requestList.get(1).equals("3")){
                        //Show reservations
                        workerRequest.add("3");
                        String requestId = UUID.randomUUID().toString();
                        synchronized (master.responseMap) {
                            master.responseMap.put(requestId, out);
                        }
                        workerRequest.add(requestId);

                        for (int port : master.workers){
                            sendToWorker(workerRequest, port);
                        }

                    } else if (requestList.get(1).equals("4")){
                        //show reservations per area
                        workerRequest.add("7");
                        String requestId = UUID.randomUUID().toString();
                        synchronized (master.responseMap) {
                            master.responseMap.put(requestId, out);
                        }
                        workerRequest.add(requestId);

                        workerRequest.add(requestList.get(2));
                        workerRequest.add(requestList.get(3));

                        for (int port : master.workers){
                            sendToWorker(workerRequest, port);
                        }

                    }
                }else if (requestList.get(0).equals("Client")){

                    String requestId = UUID.randomUUID().toString();
                    synchronized (master.responseMap) {
                        master.responseMap.put(requestId, out);
                    }

                    if (requestList.get(1).equals("1")){
                        //filters
                        workerRequest.add("4");
                        workerRequest.add(requestId);

                        for(int i=2; i<requestList.size(); i++) {
                            workerRequest.add(requestList.get(i));
                        }

                        for (int port : master.workers){
                            sendToWorker(workerRequest, port);
                        }

                    }else if (requestList.get(1).equals("2")){
                        //book room
                        workerRequest.add("5");
                        workerRequest.add(requestId);

                        workerRequest.add(requestList.get(2));
                        workerRequest.add(requestList.get(3));
                        workerRequest.add(requestList.get(4));

                        int port = selectWorkerNode((String) requestList.get(2));
                        sendToWorker(workerRequest, port);

                    }else if (requestList.get(1).equals("3")){
                        //rate room
                        workerRequest.add("6");
                        workerRequest.add(requestId);

                        workerRequest.add(requestList.get(2));
                        workerRequest.add(requestList.get(3));

                        int port = selectWorkerNode((String) requestList.get(2));
                        sendToWorker(workerRequest, port);
                    }
                } else if (requestList.get(0).equals("INSERTED_DATES")){                            //handling results
                    answer = answer + requestList.get(2) + "\n";
                    ObjectOutputStream outStream = master.responseMap.get(requestList.get(1));
                    System.out.println(answer);
                    sendToClient(answer, outStream);
                } else if (requestList.get(0).equals("RESERVATION")){
                    answer = answer + requestList.get(2);
                    ObjectOutputStream outStream = master.responseMap.get(requestList.get(1));
                    System.out.println(answer + "\n");
                    sendToClient(answer, outStream);
                } else if (requestList.get(0).equals("RATE")){
                    answer = answer + requestList.get(2) + "\n";
                    ObjectOutputStream outStream = master.responseMap.get(requestList.get(1));
                    System.out.println(answer);
                    sendToClient(answer, outStream);
                } else if (requestList.get(0).equals("REDUCER")){
                    if (requestList.get(1).equals("SHOW_RESERVATIONS")){
                        for (int i = 3; i < requestList.size(); i++) {
                            answer = answer + requestList.get(i) + "\n";
                        }
                        ObjectOutputStream outStream = master.responseMap.get(requestList.get(2));
                        System.out.println(answer);
                        sendToClient(answer, outStream);
                    } else if (requestList.get(1).equals("SHOW_ROOMS")){
                        for (int i = 3; i < requestList.size(); i++) {
                            answer = answer + requestList.get(i) + ";";
                        }
                        if (answer.equals("")){
                            answer = "No rooms found!";
                        }
                        ObjectOutputStream outStream = master.responseMap.get(requestList.get(2));
                        System.out.println(answer + "\n");
                        sendToClient(answer, outStream);
                    } else if (requestList.get(1).equals("DATE_RESERVATIONS")){
                        for (int i = 3; i < requestList.size(); i++) {
                            answer = answer + requestList.get(i) + "\n";
                        }
                        ObjectOutputStream outStream = master.responseMap.get(requestList.get(2));
                        System.out.println(answer);
                        sendToClient(answer, outStream);
                    } else if (requestList.get(1).equals("INSERTED_ROOMS")) {
                        synchronized (master.responseMap) {
                            for (int i = 3; i < requestList.size(); i++) {
                                answer = answer + requestList.get(i) + "\n";
                            }
                            ObjectOutputStream outStream = master.responseMap.remove(requestList.get(2));
                            System.out.println(answer);
                            sendToClient(answer, outStream);
                        }
                    }
                }
            }
        } catch (EOFException e) {
            //System.out.println("Reached end of stream.");
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void sendToWorker(List<Object> request, int port) {
        try {
            Socket masterSocket = new Socket("localhost", port);

            ObjectOutputStream out = new ObjectOutputStream(masterSocket.getOutputStream());

            out.writeObject(request);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public int selectWorkerNode(String roomName) {
        int hashCode = H(roomName);
        int nodeId = (Math.abs(hashCode) % master.workers.size());
        return master.workers.get(nodeId);
    }

    public int H(String roomName){
        return roomName.hashCode();
    }

    private void sendToClient(String answer, ObjectOutputStream out) {
        try {
            if (out != null) {
                out.writeObject(answer);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}