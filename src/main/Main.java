package main;

import template.Device;

import java.util.ArrayList;

/**
 * Created by chendehua on 2017/11/14.
 */
public class Main {

    public static boolean SHOW_MSG = false;

    public static void startDevices(int num_hosts) {


        // create devices
        println("Creating hosts...");
        println("Number of hosts: " + num_hosts);
        Device[] ds = new Device[num_hosts];
        for (int i=0; i<ds.length; i++) {
            println("Host "+ i + ": (localhost, " + (Device.port_base + i) + ")");
            ds[i] = new Device("localhost", Device.port_base + i);
        }
        println("Creation: done.");

        // connect to each other
        println("Connecting hosts...");
        for (int i=0; i<ds.length; i++) {
            Device.sleep(1000);
            if (i != 0) {
                ds[i].sendMessage("localhost", Device.port_base, Device.ADD+","+ds[i].self.hostname+","+ds[i].self.port);
            }
        }
        Device.sleep(2000);
        for (int i = 0; i < ds.length; i++) {
            println("Host "+ i + ": " + ds[i].nodes.size() + " hosts connected.");
        }
        println("Connection: done.");

        // start to run
        println("Starting to run...");
        for (int i=0; i<ds.length; i++) {
            println("Host "+ i + ": started.");
            ds[i].start();
        }
        println("All started.\n");

    }

    public static void println(String str) {
        System.out.println(str);
    }

    public static void monte_carlo(int n) {

        // prepare environment
        boolean[] inout = new boolean[n];
        ArrayList<Point> points = new ArrayList<>();
        double x,y;

        for (int i = 0; i < n; i++) {
            x = Math.random();
            y = Math.random();

            points.add(new Point(x,y));
        }

        // evaluate each point
        for (int i = 0; i < n; i++) {
            inout[i] = eval(points.get(i));
        }

        // calculate pi
        int count = 0;
        for (int i = 0; i < n; i++) {
            if (inout[i]) count++;
        }

        println("pi: " + String.valueOf(4.0*count/n));
    }

    public static boolean eval(Point point) {
        return (Math.sqrt(Math.pow(point.x, 2) + Math.pow(point.y, 2))) < 1;
    }

    public static void main(String... args) {

        int num_args = args.length;

        int num_hosts = 6;

        if (num_args > 0) {
            num_hosts = Integer.parseInt(args[0]);
        }

        if (num_args > 1) {
            SHOW_MSG = Boolean.parseBoolean(args[1]);
        }

//        startDevices(num_hosts);

        monte_carlo(1000000);

    }

}

