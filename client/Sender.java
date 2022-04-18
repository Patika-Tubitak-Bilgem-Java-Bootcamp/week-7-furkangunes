package client;

import java.io.DataOutputStream;
import java.net.Socket;
import java.util.concurrent.BlockingQueue;

public class Sender implements Runnable {

    private final Socket socket;
    private final BlockingQueue<String> queue;

    public Sender(final Socket socket, final BlockingQueue<String> queue) {
        this.socket = socket;
        this.queue = queue;
    }

    @Override
    public void run() {
        try {
            DataOutputStream oStream = new DataOutputStream(socket.getOutputStream());
            while (true) {
                String message = queue.take();
                oStream.writeUTF(message);
                oStream.flush();
            }
        } catch (Exception e) {

        }
    }
}
