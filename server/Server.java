package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;
import java.util.Vector;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.stream.Collectors;

public class Server {

    private static final int DEFAULT_PORT = 5555;
    private final List<ServerThreadPair> threadPairs = new Vector<>();
    private final ServerSocket serverSocket;

    public Server() {
        this(DEFAULT_PORT);
    }

    public Server(int port) {
        try {
            this.serverSocket = new ServerSocket(port);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void run() throws IOException {
        while (true) {
            System.out.println("Waiting for connection...");
            Socket socket = serverSocket.accept();
            BlockingQueue<String> queue = new LinkedBlockingQueue<>();
            ServerThreadPair threadPair = new ServerThreadPair(this, socket, queue);
            threadPairs.add(threadPair);
            new Thread(threadPair).start();
            System.out.println("Serving " + socket.getInetAddress());
        }
    }

    public void broadcast(final String message, final ServerThreadPair sender) {
        threadPairs.stream()
            .filter(tp -> tp != sender)
            .forEach(tp -> tp.getQueue().offer(message));
    }

    public void removeThreadPair(final ServerThreadPair threadPair) {
        threadPairs.remove(threadPair);
    }

    public List<String> getOtherUsers(final String username) {
        return threadPairs.stream()
            .map(tp -> tp.getUsername())
            .filter(u -> u != null && !u.equals(username))
            .collect(Collectors.toList());
    }
}
