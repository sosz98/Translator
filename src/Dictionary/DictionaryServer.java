package Dictionary;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class DictionaryServer extends Thread {
    private ServerSocket serverSocket;
    private BufferedReader in;
    private PrintWriter out;

    private String serverId;
    Translator translator;

    public static volatile boolean isServerRunning = true;

    public DictionaryServer(String serverId, ServerSocket serverSocket) {
        setServerId(serverId);
        this.serverSocket = serverSocket;
        start();
    }

    public void run() {
        while (isServerRunning) {
            try {
                System.out.printf("Server started at port %d. Bind address: %s%n", serverSocket.getLocalPort(), serverSocket.getInetAddress());
                serviceConnections();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        try {
            serverSocket.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    private void serviceConnections() {
        while (isServerRunning) {
            try {
                Socket conn = serverSocket.accept();
                System.out.println("Connection established with " + this.serverId);
                serviceRequests(conn);
            } catch (Exception exc) {
                exc.printStackTrace();
            }
        }
        try {
            serverSocket.close();
        } catch (Exception exc) {
            exc.printStackTrace();
        }
    }

    private void serviceRequests(Socket connection) throws IOException {
        try (connection) {
            in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            out = new PrintWriter(connection.getOutputStream(), true);
            String resp = in.readLine();
            String[] elements = resp.split(" ");
            String languageCode = elements[1];
            String wordToTranslate = elements[0];
            translator = new Translator(languageCode);
            sendResponseToClient(translator.translate(wordToTranslate));


        } catch (Exception exc) {
            exc.printStackTrace();
        } finally {
            in.close();
            out.close();
        }
    }

    public void setServerId(String serverId) {
        Utils.checkIfStringIsValid(serverId);
        this.serverId = serverId;
    }

    private void sendResponseToClient(String response) {
        if (response != null && !response.isBlank())
            out.println(response);
    }

    public static void main(String[] args) {
        final int SERVERS_NUM = 4;
        ServerSocket ss = null;
        try {
            String host = "localhost";
            int port = 12345;
            InetSocketAddress isa = new InetSocketAddress(host, port);
            ss = new ServerSocket();
            ss.bind(isa);
        } catch (Exception exc) {
            exc.printStackTrace();
            System.exit(1);
        }


        for (int i = 1; i <= SERVERS_NUM; i++) {
            new DictionaryServer("Server number: " + i, ss);
        }
    }
}
