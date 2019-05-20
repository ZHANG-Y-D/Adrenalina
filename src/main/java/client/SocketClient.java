package client;

import java.io.IOException;
import java.net.Socket;

public class SocketClient {

    private Socket myServer;
    private SocketClientCommands thisClient;

    public SocketClient(String serverIp, int port, String nickname) throws IOException {
        myServer = new Socket(serverIp, port);
        System.out.println("Connection through socket was successful!");
        thisClient = new SocketClientCommands();
    }

}
