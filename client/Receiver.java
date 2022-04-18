package client;

import java.io.DataInputStream;
import java.net.Socket;

public class Receiver implements Runnable {
    
    private final Socket socket;

    public Receiver(final Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        try {
            DataInputStream iStream = new DataInputStream(socket.getInputStream());
            while (true) {
                String message = iStream.readUTF();
                System.out.println(message);
            }
        } catch (Exception e) {

        }
    }
}
