package server;

import java.io.IOException;

public class Main {
    
        public static void main(String[] args) {
            Server server = new Server();
            try {
                server.run();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
}