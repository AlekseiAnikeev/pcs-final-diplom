import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * @author Aleksey Anikeev aka AgentChe
 * Date of creation: 01.11.2022
 */
public class Client {
    private static final int PORT = 8989;
    private static final String SERVER_IP = "localhost";
    public static void main(String[] args) throws IOException {
        try (
                Socket socket = new Socket(SERVER_IP, PORT);
                PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
                BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))
        ) {
            out.println("бизнес");
            System.out.println(in.readLine());
        }
    }
}
