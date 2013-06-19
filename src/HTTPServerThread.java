import java.io.*;
import java.net.Socket;

/**
 * Created with IntelliJ IDEA.
 * User: raqibul
 * Date: 6/17/13
 * Time: 12:36 PM
 * To change this template use File | Settings | File Templates.
 */
public class HTTPServerThread implements Runnable {
    private Socket socket = null;
    private PrintWriter out = null;
    private BufferedReader in = null;
    String clientSentence, fileSentence;

    public HTTPServerThread(Socket socket) {
        //To change body of created methods use File | Settings | File Templates.
        //super("HTTPServerThread");
        this.socket = socket;
    }

    public void run() {
        try {
            out = new PrintWriter(socket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            clientSentence = in.readLine();
            System.out.println(">> " + clientSentence);
            String delims = "[ ]+";
            String[] tokens = clientSentence.split(delims);

            if(tokens[0].equals("get")) {
                //read();
                String currentDir = System.getProperty("user.dir");
                String filePath = "";
                filePath = currentDir + "/" + tokens[1];

                BufferedReader bfr = new BufferedReader(new FileReader(filePath));
                try{
                    String line = null;
                    //StringBuffer totData = new StringBuffer();

                    while((line = bfr.readLine()) != null) {
                        //totData.append(line);
                        out.println(line);
                    }
                    System.out.println("Server Send the File!");
                    //out.flush();
                } catch (Exception e) {
                    //Handle Exception Here ...
                } finally {
                    bfr.close();
                }
            }
            else if(tokens[0].equals("post")) {
                //write();
                System.out.println("File Name : " + tokens[1]);
                while((fileSentence = in.readLine()) != null) {
                    //if(fileSentence.equals("null")) break;
                    System.out.println(fileSentence);
                }
                out.println("Server Got the file!");
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
}
