import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created with IntelliJ IDEA.
 * User: raqibul
 * Date: 6/17/13
 * Time: 10:26 AM
 * To change this template use File | Settings | File Templates.
 */
public class HTTPServer {
    private static final int NTHREAD = 1;
    private static int serverPort = 6789;

    public static void main(String args[]) throws IOException {
        ServerSocket httpServerSocket = null;
        boolean listening = true;
        ExecutorService executor = Executors.newFixedThreadPool(NTHREAD);

        try {
            httpServerSocket = new ServerSocket(serverPort);
        } catch (IOException e) {
            System.err.println("Can't listen on port : 6789");
            System.exit(1);
        }

        while(listening) {
            Runnable thread = new HTTPServerThread(httpServerSocket.accept());
            executor.execute(thread);
        }

        executor.shutdown();
        httpServerSocket.close();
    }
}
