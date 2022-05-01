package B;

import java.io.*;
import java.net.*;

class ClientSonja {

    public static void main(String args[]) throws Exception {
        Socket socket = new Socket("localhost", 888);

        // to send data to the server
        DataOutputStream outputStream = new DataOutputStream(socket.getOutputStream());
        BufferedReader inputServer = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        BufferedReader inputKeyboard = new BufferedReader(new InputStreamReader(System.in));

        String inputKeyboardStr, inputServerStr;
        while (!(inputKeyboardStr = inputKeyboard.readLine()).equals("exit")) {
            outputStream.writeBytes(inputKeyboardStr + "\n"); // send to the serve
            inputServerStr = inputServer.readLine(); // receive from the server
            System.out.println(inputServerStr);
        }

        outputStream.close();
        inputServer.close();
        inputKeyboard.close();
        socket.close();
    }
}

