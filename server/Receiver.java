package server;

import java.io.DataInputStream;
import java.net.Socket;
import java.util.concurrent.BlockingQueue;

public class Receiver implements Runnable {
    
    private final ServerThreadPair ownerThreadPair;
    private final Socket socket;
    private final BlockingQueue<String> queue;
    
    public Receiver(final ServerThreadPair serverThreadPair, final Socket socket, final BlockingQueue<String> queue) {
        this.ownerThreadPair = serverThreadPair;
        this.socket = socket;
        this.queue = queue;
    }

    @Override
    public void run() {
        // Try to read from socket and push to the queue
        try {
            DataInputStream dis = new DataInputStream(socket.getInputStream());
            while (true) {
                if (ownerThreadPair.getUsername() == null) {
                    ownerThreadPair.setUsername(dis.readUTF());
                    queue.offer("Welcome, " + ownerThreadPair.getUsername() + "!");
                    ownerThreadPair.requestBroadcast(ownerThreadPair.getUsername() + " has joined the chat!");
                    queue.offer("Users in chat:");
                    ownerThreadPair.requestOtherUsers().forEach(queue::offer);
                } else {
                    String message = dis.readUTF();
                    ownerThreadPair.requestBroadcast(String.format("%s: %s", ownerThreadPair.getUsername(), message));   
                }
            }
        } catch (Exception e) {

        }
    }
}
