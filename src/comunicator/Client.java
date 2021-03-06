package comunicator;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ConnectException;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 * Created by chendehua on 2017/10/8.
 */
public class Client {

    public void sendMessage(String hostName, int port, String msg) {
        DataOutputStream out = null;
        Socket socket = null;

        try {

            socket = new Socket(hostName, port);
            out = new DataOutputStream(socket.getOutputStream());
            out.writeUTF(msg);

        } catch (UnknownHostException e) {
            System.err.println("Unknown host: " + hostName);
            System.exit(1);
        } catch (ConnectException e) {
            System.err.println("Connection refused by host: " + hostName);
            System.exit(1);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                socket.close();
                System.out.flush();
            } catch (IOException e ) {
                System.out.println("Couldn't close socket");
            }
        }
    }

}
