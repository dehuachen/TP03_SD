package main;

import template.Device;

/**
 * Created by chendehua on 2017/11/14.
 */
public class Master extends Device{

    public Master(String hostname, int port) {
        super(hostname, port);
    }

    @Override
    public void abstractCommands(String str) {

    }

    @Override
    public void abstractRun() {

    }
}
