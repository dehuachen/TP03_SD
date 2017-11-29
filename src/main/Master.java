package main;

import template.Device;
import template.Node;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;
import java.util.Timer;

/**
 * Created by chendehua on 2017/11/14.
 */
public class Master extends Device{

    ArrayList<Point> points;
    int[] answers;
    boolean has_task;
    ArrayList<Node> requested_nodes;
    int last_point_idx;

    public Master(String hostname, int port, int n) {
        this(hostname, port);
        generatePoints(n);
    }

    public Master(String hostname, int port) {
        super(hostname, port);
        has_task = false;
        requested_nodes = new ArrayList<>();
    }

    // check if all points are processed
    private boolean all_received() {

        for (int i = 0; i < answers.length; i++) {
            if (answers[i] == -1) return false;
        }

        return true;
    }

    // print the approximation of pi
    private void print_pi() {
        int count = 0;
        for (int i = 0; i < answers.length; i++) {
            count += answers[i];
        }

        double approximations_pi = 4.0 * count / answers.length;
        System.out.println("\n============================");
        System.out.println("Approximations of pi: " + String.valueOf(approximations_pi));
        System.out.println("Time: " + (System.currentTimeMillis() - start_time)/1000.0);
        System.out.println("============================\n");

        Scanner s = new Scanner(System.in);
        System.out.print("\nNumber of point: ");
        int n = s.nextInt();

        generatePoints(n);

    }

    // send task to requested host
    private void send_task(String msg) {

        Point tmp;
        Node node = findNodeRef(getNodeFromMsg(msg));
        String msg_out = "";

        if (!has_task) {
            requested_nodes.add(node);
        } else {
            for (int i = 0; i < requested_nodes.size(); i++) {

                // if there is no more task, put the node into a waiting queue and quit.
                if (last_point_idx == answers.length) {
                    requested_nodes.add(node);
                    return;
                }

                Node n = requested_nodes.remove(0);
                tmp = points.get(last_point_idx);
                msg_out = last_point_idx + "," + tmp.x + " " + tmp.y;
                sendMessage(n, msg_out);
                last_point_idx++;
            }

            if (last_point_idx == answers.length) {
                requested_nodes.add(node);
                return;
            }

            tmp = points.get(last_point_idx);
            msg_out = last_point_idx + "," + tmp.x + " " + tmp.y;
            sendMessage(node, msg_out);
            last_point_idx++;
        }

    }

    // send task to requested host
    private void receive_answer(String msg) {

        String[] split = msg.split(",");
        int idx = Integer.parseInt(split[0]);
        int answer = Integer.parseInt(split[1]);

        answers[idx] = answer;

        if (all_received()) {
            has_task = false;
            print_pi();
        }
    }

    @Override
    public synchronized void abstractCommands(String str) {
        String action = getAction(str);
        String msg = str.split(",", 2)[1];

        if (action.equalsIgnoreCase(Host.TASK)) {
            send_task(msg);
        } else if (action.equalsIgnoreCase(Host.ANSWER)) {
            receive_answer(msg);
        }

    }

    @Override
    public void abstractRun() {
        Scanner s = new Scanner(System.in);
        System.out.print("\nNumber of point: ");
        int n = s.nextInt();
        generatePoints(n);
    }

    private long start_time;
    public void generatePoints(int n) {
        start_time = System.currentTimeMillis();
        has_task = true;
        last_point_idx = 0;

        if (points == null) {
            points = new ArrayList<>();
        } else {
            points.clear();
        }

        answers = new int[n];
        double x,y;

        for (int i = 0; i < n; i++) {
            x = Math.random();
            y = Math.random();

            answers[i] = -1;
            points.add(new Point(x,y));
        }

        for (int i = 0; i < requested_nodes.size(); i++) {

            // if there is no more task, put the node into a waiting queue and quit.
            if (last_point_idx == answers.length) {
                return;
            }

            Node node = requested_nodes.remove(0);
            Point tmp = points.get(last_point_idx);
            String msg_out = last_point_idx + "," + tmp.x + " " + tmp.y;
            sendMessage(node, msg_out);
            last_point_idx++;
        }
    }
}
