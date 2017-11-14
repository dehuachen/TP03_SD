package main;

import template.Device;

import java.util.ArrayList;

/**
 * Created by chendehua on 2017/11/14.
 */
public class Master extends Device{

    ArrayList<Point> points;

    public Master(String hostname, int port, int n) {
        super(hostname, port);

        points = new ArrayList<>();
        generatePoints(n);

    }

    @Override
    public void abstractCommands(String str) {

    }

    @Override
    public void abstractRun() {

    }

    public void generatePoints(int n) {
        double x,y;

        for (int i = 0; i < n; i++) {
            x = Math.random();
            y = Math.random();

            points.add(new Point(x,y));
        }
    }
}
