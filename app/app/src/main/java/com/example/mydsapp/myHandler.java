package com.example.mydsapp;

import android.os.Handler;
import android.os.Message;
import android.util.Log;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;

public class myHandler extends Thread{

    private static final String TAG = "myHandler";
    private Handler responseHandler;

    public myHandler(Handler responseHandler) {
        this.responseHandler = responseHandler;
    }

    public void sendToServer(ArrayList request){

        Thread thread = new Thread(() -> {
            try {
                Log.v(TAG, "Starting connection to server...");
                Socket socket = new Socket("10.26.45.79", 12345);
                Log.v(TAG, "Connection established.");

                ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
                ObjectInputStream in = new ObjectInputStream(socket.getInputStream());

                Log.v(TAG, "Sending data to server: " + request.toString());
                out.writeObject(request);
                out.flush();
                Log.v(TAG, "Data sent to server.");

                String result = (String) in.readObject();
                Log.v(TAG, "Response from server: " + result);

                in.close();
                out.close();
                socket.close();

                Message message = responseHandler.obtainMessage();
                message.obj = result;
                responseHandler.sendMessage(message);

            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        });
        thread.start();
    }
}
