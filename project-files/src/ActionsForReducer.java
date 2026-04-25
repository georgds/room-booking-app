import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;

public class ActionsForReducer extends Thread{

    Socket socket;
    Reducer reducer;
    ObjectInputStream in;
    ObjectOutputStream out;

    public ActionsForReducer(Socket socket, Reducer reducer) throws IOException {
        this.socket = socket;
        this.reducer = reducer;
        out = new ObjectOutputStream(socket.getOutputStream());
        in = new ObjectInputStream(socket.getInputStream());
    }

    public void run() {
        synchronized (this) {
            Object request;
            try {
                if ((request = in.readObject()) != null) {
                    ArrayList<Object> requestList = (ArrayList<Object>) request;
                    ArrayList<Object> answerList = new ArrayList<>();
                    answerList.add("REDUCER");

                    if (requestList.get(0).equals("NEW_WORKER")) {
                        reducer.workers = reducer.workers + 1;
                    } else if (requestList.get(0).equals("INSERTED_ROOMS")) {
                        //collect added rooms
                        reducer.buffer.add(requestList.get(3));

                        if (reducer.buffer.size() == (Integer) requestList.get(2)){
                            answerList.add("INSERTED_ROOMS");
                            answerList.add(requestList.get(1));
                            answerList.addAll(reducer.buffer);
                            reducer.buffer.clear();
                            sendToMaster(answerList);
                        }

                    }else if (requestList.get(0).equals("SHOW_RESERVATIONS")) {
                        //collect reservations
                        reducer.count = reducer.count + 1;
                        for (int i = 2; i < requestList.size(); i++) {
                            reducer.buffer.add(requestList.get(i));
                        }
                        if (reducer.count == reducer.workers){
                            reducer.count = 0;
                            answerList.add("SHOW_RESERVATIONS");
                            answerList.add(requestList.get(1));
                            answerList.addAll(reducer.buffer);
                            reducer.buffer.clear();
                            sendToMaster(answerList);
                        }

                    } else if (requestList.get(0).equals("SHOW_ROOMS")) {
                        //collect rooms from filters
                        reducer.count = reducer.count + 1;
                        for (int i = 2; i < requestList.size(); i++) {
                            reducer.buffer.add(requestList.get(i));
                        }
                        if (reducer.count == reducer.workers){
                            reducer.count = 0;
                            answerList.add("SHOW_ROOMS");
                            answerList.add(requestList.get(1));
                            answerList.addAll(reducer.buffer);
                            reducer.buffer.clear();
                            sendToMaster(answerList);
                        }
                    } else if (requestList.get(0).equals("DATE_RESERVATIONS")) {
                        //collect reservations per area
                        reducer.count = reducer.count + 1;
                        for (int i = 2; i < requestList.size(); i++) {
                            reducer.buffer.add(requestList.get(i));
                        }
                        if (reducer.count == reducer.workers){
                            reducer.count = 0;
                            ArrayList middle = new ArrayList();
                            answerList.add("DATE_RESERVATIONS");
                            answerList.add(requestList.get(1));
                            for (int i = 0; i < reducer.buffer.size(); i += 2) {
                                if (!middle.contains(reducer.buffer.get(i))) {
                                    int count = (Integer) reducer.buffer.get(i + 1);
                                    for (int j = i + 2; j < reducer.buffer.size(); j += 2) {
                                        if (reducer.buffer.get(i).equals(reducer.buffer.get(j))) {
                                            count = count + (Integer) reducer.buffer.get(j + 1);
                                        }
                                    }
                                    middle.add(reducer.buffer.get(i));
                                    middle.add(count);
                                }
                            }
                            for (int i = 0; i < middle.size(); i += 2){
                                answerList.add(middle.get(i) + ": " + middle.get(i + 1));
                            }
                            reducer.buffer.clear();
                            sendToMaster(answerList);
                        }
                    }
                }
            } catch (IOException | ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public static void sendToMaster(ArrayList<Object> request) throws IOException {
        Socket socket = new Socket("localhost", 12345);

        try {
            ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());

            out.writeObject(request);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}