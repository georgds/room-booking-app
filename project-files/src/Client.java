import java.io.IOException;
import java.net.Socket;

public class Client {

    public static void main(String[] args) {

        try {
            Socket socket = new Socket("localhost", 12345);

            DummyApp app = new DummyApp(socket);
            app.run();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
