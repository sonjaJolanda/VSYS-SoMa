package B;

import java.io.*;
import java.net.*;

class ServerSonja {

    public static void main(String args[]) throws Exception {
        ServerSocket socket = new ServerSocket(888);

        // connect it to client socket
        Socket s = socket.accept();
        System.out.println("Connection established");

        // to send data to the client
        PrintStream printStream = new PrintStream(s.getOutputStream());

        BufferedReader inputClient = new BufferedReader(new InputStreamReader(s.getInputStream()));
        BufferedReader inputKeyboard = new BufferedReader(new InputStreamReader(System.in));

        while (true) {
            String inputClientStr, inputServerStr;
            while ((inputClientStr = inputClient.readLine()) != null) {
                System.out.println(inputClientStr);
                inputServerStr = inputKeyboard.readLine();
                printStream.println(inputServerStr); // send to client
            }

            printStream.close();
            inputClient.close();
            inputKeyboard.close();
            socket.close();
            s.close();
            System.exit(0);
        }
    }
}

