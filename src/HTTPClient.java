import java.io.*;
import java.net.Socket;

/**
 * Created with IntelliJ IDEA.
 * User: raqibul
 * Date: 6/17/13
 * Time: 10:26 AM
 * To change this template use File | Settings | File Templates.
 */
public class HTTPClient {
    public static void main(String args[])  throws IOException {
        Socket httpClientSocket = null;
        PrintWriter out = null;
        BufferedReader in = null;
        BufferedReader stdIn = null;

        try {
            httpClientSocket = new Socket("localhost", 6789);
            out = new PrintWriter(httpClientSocket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(httpClientSocket.getInputStream()));

            stdIn = new BufferedReader(new InputStreamReader(System.in));
            String userInput, serverOutput;

            System.out.print(">>");
            userInput = stdIn.readLine();

            out.println(userInput);
            String delims = "[ ]+";
            String[] tokens = userInput.split(delims);

            if (tokens[0].equals("post")) {
                //Open File and send the lines of the file ...
                String currentDir = System.getProperty("user.dir");
                String filePath = "";
                filePath = currentDir + "/" + tokens[1];
                BufferedReader bfr = new BufferedReader(new FileReader(filePath));
                try {
                    String line = null;
                    while ((line = bfr.readLine()) != null) {
                        out.println(line);
                    }
                } catch (Exception e) {
                    //Handle Exception Here ...
                } finally {
                    bfr.close();
                }
            } else if (tokens[0].equals("get")) {
                while ((serverOutput = in.readLine()) != null) {
                    System.out.println(serverOutput);
                }
            }
        } catch (Exception e) {
            //Handle exception here !
        } finally {
            try {
                in.close();
            } catch (IOException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }
            out.close();
            stdIn.close();
            try {
                httpClientSocket.close();
            } catch (IOException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }
        }
    }
}
