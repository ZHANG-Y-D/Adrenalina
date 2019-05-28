package adrenaline.client;

import adrenaline.client.controller.Controller;
import com.google.gson.Gson;

import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Scanner;

public class SocketHandler implements ConnectionHandler {

    private String clientID;
    private java.net.Socket myServer;
    private SocketClientCommands thisClient;
    private PrintWriter outputToServer;
    private HashMap<String, Object> methodsMap;
    private Gson gson;
    private Controller controller;

    public SocketHandler(String serverIp, int port, Controller controller) throws IOException {
        myServer = new java.net.Socket(serverIp, port);
        System.out.println("Connection through socket was successful!");
        thisClient = new SocketClientCommands();
        Scanner inputFromServer = new Scanner(myServer.getInputStream());
        outputToServer = new PrintWriter(myServer.getOutputStream());
        clientID = inputFromServer.nextLine();
        gson = new Gson();
        createServerListener(inputFromServer);
        this.controller = controller;
    }

    private void createServerListener(Scanner inputFromServer) {
        new Thread(() -> {
            String readFromServer;
            String[] readSplit;
            Method requestedMethod;

            while (true) {
                try {
                    readFromServer = inputFromServer.nextLine();
                    readSplit = readFromServer.split(";");
                    if (readSplit[0].equals("RETURN")) {
                        controller.handleReturn(readSplit[1]);
                    }else {
                        String methodName = readSplit[0];
                        int argSize = Integer.parseInt(readSplit[1].substring(readSplit[1].indexOf("=") + 1).trim());
                        Class[] argClasses = new Class[argSize];
                        Object[] argObjects = new Object[argSize];
                        for (int i = 0; i < argSize; i++) {
                            argClasses[i] = Class.forName(readSplit[2 + 2 * i]);
                            argObjects[i] = gson.fromJson(readSplit[3 + 2 * i], argClasses[i]);
                        }
                        requestedMethod = methodsMap.get(methodName).getClass().getMethod(methodName, argClasses);
                        requestedMethod.invoke(methodsMap.get(methodName), argObjects).toString();
                    }
                } catch (Exception e) {
                }
            }
        }
        ).start();
    }

    private synchronized void sendMessage(String msg){
        outputToServer.println(msg);
        outputToServer.flush();
    }

    public void unregister() {
        String unregisterMsg = "unregisterClient;ARGSIZE=1;java.lang.String;";
        unregisterMsg += gson.toJson(clientID);
        sendMessage(unregisterMsg);
    }

    public void setNickname(String nickname) {
        String nicknameMsg = "setNickname;ARGSIZE=2;java.lang.String;";
        nicknameMsg += gson.toJson(clientID)+";";
        nicknameMsg += "java.lang.String;";
        nicknameMsg += gson.toJson(nickname);
        sendMessage(nicknameMsg);
    }



}
