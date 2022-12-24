/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tinosqlshell;

import java.awt.GraphicsEnvironment;
import java.io.BufferedReader;
import java.io.Console;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.SocketException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Ducnm62
 */
public class TNsqlShell {

    private final static String env = "dev";
//    private final static String env = "deploy";
    private final static String keywordInternal = "connectclearclsexitquitping";
    private static final String userDefault = "unconnect";
    private static String userConnect = "unconnect";
    private static String IPAddress;
    private static Integer PORT;

    public static long startPingTime;

    public static void main(String argv[]) throws Exception {
        Console console = System.console();
        if (env.equals("deploy") && console == null && !GraphicsEnvironment.isHeadless()) {
            String filename = TNsqlShell.class.getProtectionDomain().getCodeSource().getLocation().toString().substring(6);
            try {
                File batch = new File("Launcher.bat");
                if (!batch.exists()) {
                    batch.createNewFile();
                    PrintWriter writer = new PrintWriter(batch);
                    writer.println("@echo off");
                    writer.println("java -jar " + filename);
                    writer.println("exit");
                    writer.flush();
                }
                Runtime.getRuntime().exec("cmd /c start \"\" " + batch.getPath());
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            while (true) {
                String sentence_to_server;
                System.out.print(Color.YELLOW);
                System.out.print(userConnect == null ? ""
                        : userConnect + " ");
                System.out.print(Color.RESET);
                System.out.print("<< ");
                BufferedReader inFromUser
                        = new BufferedReader(new InputStreamReader(System.in));
                sentence_to_server = inFromUser.readLine().trim().toLowerCase();
                if (keywordInternal.contains(sentence_to_server)
                        || keywordInternal.contains(sentence_to_server.split(" ")[0])) {
                    if (sentence_to_server.equals("quit")) {
                        return;
                    }
                    if (sentence_to_server.equals("clear") || sentence_to_server.equals("cls")) {
                        new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
                        System.out.print("\033[H\033[2J");
                        System.out.flush();
                    }
                    if (sentence_to_server.equals("ping")) {
                        TNsqlShell.startPingTime = System.currentTimeMillis();
                        sendRequest(sentence_to_server + " " + TNsqlShell.startPingTime);
                    }
                    if (sentence_to_server.startsWith("connect")) {
                        String[] keywords = sentence_to_server.split(" ");
                        if (keywords.length == 3) {
                            userConnect = keywords[1];
                            IPAddress = keywords[1];
                            PORT = Integer.parseInt(keywords[2]);
                            TNsqlShell.startPingTime = System.currentTimeMillis();
                            sendRequest("ping " + TNsqlShell.startPingTime);
                        } else if (keywords.length == 5) {
                            IPAddress = keywords[1];
                            PORT = Integer.parseInt(keywords[2]);
                            TNsqlShell.startPingTime = System.currentTimeMillis();
                            if (keywords[3].equals("as")) {
                                userConnect = keywords[4];
                            } else {
                                userConnect = IPAddress;
                            }
                            sendRequest("ping " + TNsqlShell.startPingTime);
                        } else {
                            System.out.println(">> Syntax Erorr");
                        }
                    }
                } else {
                    sendRequest(sentence_to_server);
                }
            }

        }
    }

    private static void sendRequest(String sentence_to_server) {
        if (IPAddress == null || PORT == null) {
            System.out.println(">> Please init the connection first");
        } else {
            try (Socket clientSocket = new Socket(IPAddress, PORT)) {
                DataOutputStream outToServer = new DataOutputStream(clientSocket.getOutputStream());
                outToServer.writeBytes(sentence_to_server + '\n');
                new ServerResponse().setClientSocket(clientSocket)
                        .setPORT(PORT)
                        .setIP(IPAddress)
                        .getResponse();
            } catch (SocketException ex) {
                userConnect = null;
                System.out.println(">> Can't connect to server. Please check server status");
            } catch (IOException ex) {
                userConnect = null;
                System.out.println(">> Can't connect to server. Please check server status");
            }
        }
    }
}
