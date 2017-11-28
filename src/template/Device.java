package template;

import comunicator.Callback;
import comunicator.Client;
import comunicator.Server;
import main.Main;

import java.util.*;

/**
 * Created by chendehua on 2017/11/14.
 */
public class Device extends Thread implements Callback {
    Client client;
    Server server;
    public Node self;
    public ArrayList<Node> nodes;

    public Device(String hostname, int port) {
        this.client = new Client();
        this.server = new Server(this, port);
        this.self = new Node(hostname, port);

        this.nodes = new ArrayList<>();

        this.server.start();
    }

    public void abstractCommands(String str) {}

    public void abstractRun() {}

    public String getAction(String str) {
        String[] command = str.split(",", 2);

        String action = command[0];

        return action;
    }

    @Override
    public synchronized void callback(String str) {
        String[] command = str.split(",", 2);

        String action = command[0];

        if (action.equalsIgnoreCase(ADD)) {
            addNode(command[1]);
        } else {
            abstractCommands(str);
        }

        if (Main.SHOW_MSG) {
            System.out.println("(" + this.self.hostname + ", " + this.self.port + "): " + str);
        }
    }

    @Override
    public void run() {
        abstractRun();
    }

    public Node findNodeRef(Node node) {
        for (Node n: this.nodes) {
            if (node.equals(n)) {
                return n;
            }
        }
        return null;
    }

    public Node getNodeFromMsg(String msg) {
        String[] strings = msg.split(",");
        String hostname = strings[0];
        int port = Integer.parseInt(strings[1]);

        return new Node(hostname, port);
    }


    public String formatMsg(Node node, String type) {
        return type+","+node.hostname+","+node.port;
    }

    private void broadcast(Node node) {
        for (Node n: this.nodes) {
            String msg = formatMsg(node, ADD);
            sendMessage(n, msg);
        }
    }

    private void sendListBack(Node node) {

        String msg = formatMsg(this.self, ADD);
        sendMessage(node, msg);

        for (Node n: this.nodes) {
            msg = formatMsg(n, ADD);
            sendMessage(node, msg);
        }
    }

    public void sendMessage(String hostname, int port, String msg) {
        this.client.sendMessage(hostname, port, msg);
    }

    public void sendMessage(Node node, String msg) {
        this.client.sendMessage(node.hostname, node.port, msg);
    }

    private void addNode(String msg) {
        Node node = getNodeFromMsg(msg);

        if (this.self.port == port_base) {
            broadcast(node);
            sendListBack(node);
        }

        nodes.add(node);
    }


    // global attributes
    public final static String ADD = "add";
    public final static int port_base = 8081;

    public static void sleep(long time) {
        try {
            Thread.sleep(time);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
