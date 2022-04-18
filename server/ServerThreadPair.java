package server;

import java.net.Socket;
import java.util.concurrent.BlockingQueue;
import java.util.List;

public class ServerThreadPair implements Runnable {

    private String username;
    private final Server server;
    private final BlockingQueue<String> queue;
    private final Thread senderThread;
    private final Thread receiverThread;

    public ServerThreadPair(final Server server, final Socket socket, final BlockingQueue<String> queue) {
        Sender sender = new Sender(socket, queue);
        Receiver receiver = new Receiver(this, socket, queue);
        this.server = server;
        senderThread = new Thread(sender);
        receiverThread = new Thread(receiver);
        this.queue = queue;
    }

    @Override
    public void run() {
        senderThread.start();
        receiverThread.start();

        try {
            receiverThread.join();
            senderThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            server.removeThreadPair(this);
        }
    }

    public void requestBroadcast(final String message) {
        server.broadcast(message, this);
    }

    public List<String> requestOtherUsers() {
        return server.getOtherUsers(username);
    }

    public BlockingQueue<String> getQueue() {
        return queue;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(final String username) {
        this.username = username;
    }
}
