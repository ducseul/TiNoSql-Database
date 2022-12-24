/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tinosqlshell;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ConnectException;
import java.net.Socket;
import java.net.SocketException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Ducnm62
 */
public class ServerResponse{

    private int PORT;
    private String IP;
    private Socket clientSocket;

    public ServerResponse() {
    }

    public ServerResponse(String IP, int PORT) {
        this.PORT = PORT;
        this.IP = IP;
    }

    public int getPORT() {
        return PORT;
    }

    public ServerResponse setPORT(int PORT) {
        this.PORT = PORT;
        return this;
    }

    public String getIP() {
        return IP;
    }

    public ServerResponse setIP(String IP) {
        this.IP = IP;
        return this;
    }

    public Socket getClientSocket() {
        return clientSocket;
    }

    public ServerResponse setClientSocket(Socket clientSocket) {
        this.clientSocket = clientSocket;
        return this;
    }

    public void getResponse() {
        String sentence_from_server;
        try {
            if (clientSocket == null) {
                clientSocket = new Socket(IP, PORT);
            }
            BufferedReader inFromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            sentence_from_server = inFromServer.readLine();
            if(sentence_from_server.contains("%s")){ //Only ping then output contain %s
                sentence_from_server = String.format(sentence_from_server, (System.currentTimeMillis() - TNsqlShell.startPingTime));
            }
            System.out.println(">> Response: " + sentence_from_server);
        } catch (ConnectException e) {
            System.out.println(String.format("Can't connect to server at %s::%s", IP, PORT + ""));
        } catch (IOException ex) {
            System.out.println(String.format("Can't connect to server at %s::%s", IP, PORT + ""));
        }
    }

}

