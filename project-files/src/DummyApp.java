import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class DummyApp extends Thread{

    ObjectInputStream in;
    ObjectOutputStream out;
    Socket socket;

    public DummyApp(Socket socket){
        this.socket = socket;
        try {
            out = new ObjectOutputStream(socket.getOutputStream());
            in = new ObjectInputStream(socket.getInputStream());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void run() {
        try {
            List<Object> request = new ArrayList<>();

            Scanner userInput = new Scanner(System.in);
            System.out.print("1. Manager\n2. Client\n3. Close\nSelect mode: ");

            String mode = userInput.nextLine();

            while (true) {
                switch (mode) {
                    case "1":
                        request = ManagerMenu();
                        break;
                    case "2":
                        request = ClientMenu();
                        break;
                    case "3":
                        closeAll();
                        break;
                    default:
                        System.out.print("Wrong input! You should choose a number between 1-3. Try again: ");
                        mode = userInput.nextLine();
                        break;
                }

                out.writeObject(request);
                out.flush();

                try {
                    Object message;
                    while ((message = in.readObject()) != null) {
                        System.out.println(message);
                        break;
                    }
                } catch (EOFException e) {
                    System.out.println("Reached end of stream.");
                } catch (IOException | ClassNotFoundException e) {
                    e.printStackTrace();
                }

            }

        } catch(IOException e) {
            e.printStackTrace();
        }
    }

    public void closeAll() throws IOException {
        out.close();
        in.close();
        socket.close();
    }
    public static ArrayList ManagerMenu() throws IOException {
        ArrayList userOptions = new ArrayList();
        userOptions.add("Manager");
        Scanner userInput = new Scanner(System.in);

        System.out.print("1. Add accommodation.\n2. Add available dates for your accommodation.\n3. Print your accommodation.\n4. Print total reservation number for each area for a specific date range.\nChoose your option: ");
        String option = userInput.nextLine();

        boolean flag = true;

        while (flag) {
            switch (option) {
                case "1":
                    userOptions.add(option);
                    System.out.print("Great! Now enter the json file path: ");
                    String path = userInput.nextLine();
                    userOptions.add(path);
                    flag = false;
                    break;
                case "2":
                    userOptions.add(option);
                    System.out.print("Great! Now enter the room's name: ");
                    String name = userInput.nextLine();
                    userOptions.add(name);
                    System.out.print("The first date you want it to be available: ");
                    String date1 = userInput.nextLine();
                    userOptions.add(date1);
                    System.out.print("The last date you want it to be available: ");
                    String date2 = userInput.nextLine();
                    userOptions.add(date2);
                    flag = false;
                    break;
                case "3":
                    userOptions.add(option);
                    System.out.println("Great! Your accommodations will show up soon...");
                    flag = false;
                    break;
                case "4":
                    userOptions.add(option);
                    System.out.print("Great! Now enter the first date: ");
                    String fdate = userInput.nextLine();
                    userOptions.add(fdate);
                    System.out.print("Great! Now enter the last date: ");
                    String sdate = userInput.nextLine();
                    userOptions.add(sdate);
                    System.out.println("Great! Your total number of reservations for each area will show up soon...");
                    flag = false;
                    break;
                default:
                    System.out.print("Wrong input! You should choose a number between 1-4. Try again: ");
                    option = userInput.nextLine();
                    break;
            }
        }
        return userOptions;
    }

    public static ArrayList ClientMenu(){
        ArrayList userOptions = new ArrayList();
        userOptions.add("Client");
        Scanner userInput = new Scanner(System.in);

        System.out.print("1. Search by filters\n2. Make a reservation\n3. Rate a room\nChoose your option: ");
        String option = userInput.nextLine();

        boolean flag = true;

        while (flag) {
            switch (option) {
                case "1":
                    userOptions.add("1");

                    boolean filter_flag = true;
                    int i = 0;
                    while (filter_flag && i <= 5) {

                        System.out.print("Great! Now choose one of the following filters according to your preferences:\n1. " +
                                "Area\n2. Dates\n3. Number of people\n4. Price\n5. Stars\n6. Return to the menu\nGive a number: ");
                        String number_of_filter = userInput.nextLine();

                        if (number_of_filter.equals("6")){
                            filter_flag = false;

                        }else{
                            System.out.print("Great! Now give your preference of the number you chose: ");
                            String details = userInput.nextLine();

                            userOptions.add(number_of_filter);
                            userOptions.add(details);
                            i++;
                            System.out.print("Do you wish to continue adding filters? (Y/N) ");

                            boolean f = false;
                            while (!f) {
                                String answer = userInput.nextLine();
                                if (answer.equals("N")) {
                                    filter_flag = false;
                                    f = true;
                                } else if (answer.equals("Y")) {
                                    f = true;
                                } else {
                                    System.out.print("Please type a valid answer: ");
                                }
                            }

                        }
                    }
                    flag = false;
                    break;
                case "2":
                    userOptions.add("2");
                    System.out.print("Great! Now enter room's name: ");
                    String name = userInput.nextLine();
                    userOptions.add(name);
                    System.out.print("Great! Now enter the first date: ");
                    String firstdate = userInput.nextLine();
                    userOptions.add(firstdate);
                    System.out.print("Great! Now enter the last date: ");
                    String seconddate = userInput.nextLine();
                    userOptions.add(seconddate);
                    flag = false;
                    break;
                case "3":
                    userOptions.add("3");

                    System.out.print("Please enter the room's name: ");
                    String name2 = userInput.nextLine();

                    System.out.print("Great! Now enter your rating: ");
                    String rate = userInput.nextLine();

                    userOptions.add(name2);
                    userOptions.add(rate);
                    flag = false;
                    break;
                default:
                    System.out.print("Wrong input! You should choose a number between 1-3. Try again: ");
                    option = userInput.nextLine();
            }
        }
        return userOptions;
    }
}