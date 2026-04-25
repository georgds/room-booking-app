import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class ActionsForWorker extends Thread {

    Socket socket;
    Worker worker;
    ObjectInputStream in;
    ObjectOutputStream out;

    public ActionsForWorker(Socket socket, Worker worker) throws IOException {
        this.socket = socket;
        this.worker = worker;
        in = new ObjectInputStream(socket.getInputStream());
        out = new ObjectOutputStream(socket.getOutputStream());

    }

    public void run() {
        //synchronized (this) {
            Object request;
            try {
                if ((request = in.readObject()) != null) {

                    ArrayList<Object> requestList = (ArrayList<Object>) request;
                    ArrayList<Object> answerList = new ArrayList<>();

                    if (requestList.get(0).equals("1")) {
                        //add accommodation
                        answerList.add("INSERTED_ROOMS");
                        answerList.add(requestList.get(1));
                        answerList.add(requestList.get(2));
                        Room room = (Room) requestList.get(3);
                        worker.rooms.add(room);
                        answerList.add("Room " + room.getRoomName() + " has been added!");
                        sendToReducer(answerList);

                    } else if (requestList.get(0).equals("2")) {
                        //add dates
                        answerList.add("INSERTED_DATES");
                        answerList.add(requestList.get(1));
                        for (Room room : worker.rooms) {
                            if (room.getRoomName().equals((String) requestList.get(2))) {
                                room.InitializeDateRange((String) requestList.get(3), (String) requestList.get(4));
                                answerList.add("Room's " + room.getRoomName() + " dates have been saved!");
                            }
                        }
                        sendToMaster(answerList);

                    } else if (requestList.get(0).equals("3")) {
                        //show reservations
                        answerList.add("SHOW_RESERVATIONS");
                        answerList.add(requestList.get(1));
                        for (Room room : worker.rooms) {
                            answerList.add("Room " + room.getRoomName() + " is booked for the following days: " + room.getReservations());
                        }
                        sendToReducer(answerList);

                    } else if (requestList.get(0).equals("4")) {
                        //filters
                        answerList.add("SHOW_ROOMS");
                        answerList.add(requestList.get(1));
                        boolean flag = true;
                        for (Room room : worker.rooms) {
                            int i = 2;
                            while (i < requestList.size() && flag) {
                                if (requestList.get(i).equals("1")) {
                                    if (!(requestList.get(i + 1).equals(room.getArea()))) {
                                        flag = false;
                                    }
                                } else if (requestList.get(i).equals("2")) {
                                    String[] dates = ((String) requestList.get(i + 1)).split(",");
                                    if (!room.available(dates[0], dates[1])) {
                                        flag = false;
                                    }
                                } else if (requestList.get(i).equals("3")) {
                                    int value = Integer.parseInt((String) requestList.get(i + 1));
                                    if (value != room.getNoOfPersons()) {
                                        flag = false;
                                    }
                                } else if (requestList.get(i).equals("4")) {
                                    int value = Integer.parseInt((String) requestList.get(i + 1));
                                    if (value != room.getPrice()) {
                                        flag = false;
                                    }
                                } else if (requestList.get(i).equals("5")) {
                                    int value = Integer.parseInt((String) requestList.get(i + 1));
                                    if (value != room.getStars()) {
                                        flag = false;
                                    }
                                }
                                i = i + 2;
                            }
                            if (flag) {
                                answerList.add(room.getRoomName() + "," + room.getArea() + "," + room.getNoOfPersons() + "," + room.getPrice() + "," + room.getStars() + "," + room.getNoOfReviews() + "," + room.getRoomImage());
                            }
                        }
                        sendToReducer(answerList);


                    } else if (requestList.get(0).equals("5")) {
                        //make reservation
                        answerList.add("RESERVATION");
                        answerList.add(requestList.get(1));
                        Boolean done;
                        for (Room room : worker.rooms) {
                            if (room.getRoomName().equals((String) requestList.get(2))) {
                                done = room.reserve((String) requestList.get(3), (String) requestList.get(4));
                                if (done) {
                                    answerList.add("Room's " + room.getRoomName() + " reservation is done!");
                                } else {
                                    answerList.add("Room " + room.getRoomName() + " is not available");
                                }
                            }
                        }
                        sendToMaster(answerList);

                    } else if (requestList.get(0).equals("6")) {
                        //rate room
                        answerList.add("RATE");
                        answerList.add(requestList.get(1));
                        for (Room room : worker.rooms) {
                            if (room.getRoomName().equals((String) requestList.get(2))) {
                                room.setStars((room.getStars() + Double.parseDouble( (String) requestList.get(3))) / (room.getNoOfReviews() + 1));  ///
                                answerList.add("Room's " + room.getRoomName() + " rate has been saved!");
                            }
                        }
                        sendToMaster(answerList);

                    } else if (requestList.get(0).equals("7")) {
                        //show reservations for a date range
                        answerList.add("DATE_RESERVATIONS");
                        answerList.add(requestList.get(1));
                        HashMap<String, Integer> areas = new HashMap<String, Integer>();
                        for (Room room : worker.rooms) {
                            if (!areas.containsKey(room.getArea())) {
                                areas.put(room.getArea(), 0);
                            }
                            if (!room.available((String) requestList.get(2), (String) requestList.get(3))) {
                                areas.put(room.getArea(), areas.get(room.getArea()) + 1);
                            }
                        }
                        for (Map.Entry<String, Integer> result : areas.entrySet()) {
                            answerList.add(result.getKey());
                            answerList.add(result.getValue());
                        }
                        sendToReducer(answerList);

                    }
                }
            } catch (IOException | ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        //}
    }

    public static void sendToReducer(ArrayList<Object> request) throws IOException {
        Socket socket = new Socket("localhost", 12346);
        try {
            ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream in = new ObjectInputStream(socket.getInputStream());

            out.writeObject(request);
            in.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void sendToMaster(ArrayList<Object> request) throws IOException {
        Socket socket = new Socket("localhost", 12345);

        try {
            ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream in = new ObjectInputStream(socket.getInputStream());

            out.writeObject(request);
            out.close();
            in.close();
            socket.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
