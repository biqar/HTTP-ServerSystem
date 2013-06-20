import java.io.*;
import java.net.Socket;
import java.nio.CharBuffer;
import java.text.SimpleDateFormat;
import java.util.Date;

import static java.lang.System.out;

/**
 * Created with IntelliJ IDEA.
 * User: raqibul
 * Date: 6/17/13
 * Time: 12:36 PM
 * To change this template use File | Settings | File Templates.
 */
public class HTTPRequestHandler implements Runnable {
    private Socket socket = null;
    private PrintWriter out = null;
    private BufferedReader in = null;
    private String clientRequestHeaderLine, fileSentence, serverResponseHeader, separator;
    private String[] tokens;
    private char[] requestHeader;
    private int requestHeaderSize;

    public HTTPRequestHandler(Socket socket) {
        //To change body of created methods use File | Settings | File Templates.
        //super("HTTPServerThread");
        this.socket = socket;
    }

    public void run() {
        try {
            out = new PrintWriter(socket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            /*requestHeaderSize = socket.getInputStream().available();
            requestHeader = new char[requestHeaderSize];
            in.read(requestHeader, 0, requestHeaderSize);
            //System.out.println(requestHeader);

            clientRequestSentence = String.valueOf(requestHeader);
            System.out.println("The client's request is:\n" + clientRequestSentence);*/

            clientRequestHeaderLine = in.readLine();
            //System.out.println(clientRequestHeaderLine);

            separator = "[ ]+";
            tokens = clientRequestHeaderLine.split(separator);

            if(tokens[0].equals("GET")) {
                //read();
                do {
                    clientRequestHeaderLine = in.readLine();
                    //System.out.println(clientRequestHeaderLine);
                } while(in.ready());

                String currentDir = System.getProperty("user.dir");
                String filePath = "";
                filePath = currentDir + tokens[1];
                System.out.println(filePath);

                BufferedReader bfr = new BufferedReader(new FileReader(filePath));
                try{
                    String line = null;
                    serverResponseHeader = getGETResponseHeader(filePath);
                    System.out.println(serverResponseHeader);

                    out.println(serverResponseHeader);
                    while((line = bfr.readLine()) != null) {
                        out.println(line);
                    }
                    System.out.println("Server got the GET!");
                } catch (Exception e) {
                    //Handle Exception Here ...
                } finally {
                    bfr.close();
                }
            }
            else if(tokens[0].equals("POST")) {
                //write();
                do {
                    clientRequestHeaderLine = in.readLine();
                    //System.out.println(clientRequestHeaderLine);
                    if(clientRequestHeaderLine.indexOf("Content-Disposition:") != -1) {
                        String separator = "[ =\"]+";
                        String[] tokens = clientRequestHeaderLine.split(separator);

                        clientRequestHeaderLine = in.readLine();
                        clientRequestHeaderLine = in.readLine();
                        System.out.println(tokens[3] + ": " + clientRequestHeaderLine);
                    }
                } while(in.ready());
                System.out.println("Server Got the POST!");
                out.println("Server Handle The POST");
            }
            else {
                out.println("Error: Command Not Found !");
            }

            System.out.println("Server Work End!");
        } catch (IOException e) {
            System.err.println(e);
        } finally {
            try {
                in.close();
            } catch (IOException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }
            out.close();
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }
        }
    }

    public String getGETResponseHeader(String filePath) {
        String GETResponseHeader = null;
        File file = new File(filePath);
        Date date = new Date();
        Date lastModifiedDate = new Date(file.lastModified());

        GETResponseHeader = String.format("HTTP/1.1 200 OK\nDate: %s\nServer: Custom Server (Unix) (Red-Hat/Linux)\nLast-Modified: %s\nContent-Type: text/html; charset=UTF-8\nContent-Length: %d\nConnection: close\n", date, lastModifiedDate, file.length());
        return GETResponseHeader;
    }
}
