package main;

import template.Device;
import template.Node;

import java.util.concurrent.Semaphore;

/**
 * Created by chendehua on 2017/11/14.
 */
public class Host extends Device {

    public static final String TASK = "TASK";
    public static final String ANSWER = "ANSWER";

    Node master;
    Semaphore waiting;

    public Host(String hostname, int port, Node master) {
        super(hostname, port);
        this.master = master;
        waiting = new Semaphore(1);
    }

    public boolean eval(Point point) {
        return (Math.sqrt(Math.pow(point.x, 2) + Math.pow(point.y, 2))) < 1;
    }

    @Override
    public void abstractCommands(String str) {
        String[] split = str.split(",");
        String[] xy = split[1].split(" ");

        String idx = split[0];
        int x = Integer.parseInt(xy[0]);
        int y = Integer.parseInt(xy[1]);
        int answer = 0;

        if (eval(new Point(x,y))) {
            answer = 1;
        }

        String msg_out = idx + "," + answer;
        sendMessage(master, msg_out);

        waiting.release();
    }

    @Override
    public void abstractRun() {
        while (true) {
            try {
                waiting.acquire();
                sendMessage(master, formatMsg(self, TASK));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
