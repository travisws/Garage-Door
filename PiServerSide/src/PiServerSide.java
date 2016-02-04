import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class PiServerSide extends Thread {
    protected static boolean serverContinue = true;
    protected Socket clientSocket;

    private PiServerSide (Socket clientSoc) {
        clientSocket = clientSoc;
        start();
    }

    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = null;

        try {
            serverSocket = new ServerSocket(8081);
            System.out.println("Connection Socket Created");
            try {
                while (serverContinue) {
                    System.out.println("Waiting for Connection");
                    new EchoServer(serverSocket.accept());
                }
            } catch (IOException e) {
                System.err.println("Accept failed.");
                System.exit(1);
            }
        } catch (IOException e) {
            System.err.println("Could not listen on port: 8081.");
            System.exit(1);
        } finally {
            try {
                serverSocket.close();
            } catch (IOException e) {
                System.err.println("Could not close port: 8081.");
                System.exit(1);
            }
        }
    }

    public void run() {
        System.out.println("New Communication Thread Started");

        try {
            PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

            String inputLine;

            while ((inputLine = in.readLine()) != null) {
                System.out.println("Server: " + inputLine);
                out.println(inputLine);

                if (inputLine.equals("on")) {

                    // set up the command and parameter
                    String pythonScriptPath = "/root/test.py";
                    String[] cmd = new String[2];
                    cmd[0] = "python"; // check version of installed python: python -V
                    cmd[1] = pythonScriptPath;

                    // create runtime to execute external command
                    Runtime rt = Runtime.getRuntime();
                    Process pr = rt.exec(cmd);

                }

                if (inputLine.equals("Bye."))
                    break;

                if (inputLine.equals("End Server."))
                    serverContinue = false;
            }

            out.close();
            in.close();
            clientSocket.close();
        } catch (IOException e) {
            System.err.println("Problem with Communication Server");
            // System.exit(1);
        }
    }
}
