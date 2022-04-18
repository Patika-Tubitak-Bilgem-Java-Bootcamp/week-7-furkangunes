package client;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class Client {

    private static final int DEFAULT_PORT = 5555;
    private final Socket socket;
    private final Sender sender;
    private final Receiver receiver;
    private final BlockingQueue<String> queue = new LinkedBlockingQueue<>();


    public Client() throws IOException {
        socket = new Socket(InetAddress.getLocalHost(), DEFAULT_PORT);       
        sender = new Sender(socket, queue);
        receiver = new Receiver(socket);
    }
    
    public void run() {
        try {
            new Thread(sender).start();
            new Thread(receiver).start();
            
            System.out.println("Connected to " + socket.getInetAddress());
            System.out.println("Provide your username: ");
            while (true) {
                String message = System.console().readLine();
                queue.offer(message);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
