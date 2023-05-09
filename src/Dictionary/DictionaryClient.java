package Dictionary;

import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;

public class DictionaryClient {
    private final static int PORT = 12345;
    private Socket socket;
    private PrintWriter out;
    private BufferedReader in;

    public DictionaryClient() {
    }

    public void connect(String word, String languageCode) throws UnknownHostException, IOException {
        socket = new Socket("localhost", PORT);
        out = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()), true);
        System.out.println("Connected with host: " + socket.getInetAddress());
        sendData(word, languageCode);
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        System.out.println(in.readLine());
        disconnect();

    }

    private void sendData(String word, String languageCode) throws IOException {
        Utils.checkIfStringIsValid(word);
        Utils.checkIfStringIsValid(languageCode);
        String request = String.format("%s %s\n", word, languageCode);
        System.out.println("Sending word: " + request);
        out.println(request);
        System.out.println("Waiting for response");
    }

    public void disconnect() throws IOException {
        socket.close();
        out.close();
        in.close();
    }

    public static void main(String[] args) throws IOException {
        new DictionaryClient().connect("agfdsadfds", "EN");
    }

}
