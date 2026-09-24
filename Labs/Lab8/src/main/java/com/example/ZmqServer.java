package com.example;

import org.zeromq.ZMQ;

public class ZmqServer {
    public static void main(String[] args) {
        // Create a ZeroMQ context
        ZMQ.Context context = ZMQ.context(1);
        // Create a socket to respond to requests
        ZMQ.Socket responder = context.socket(ZMQ.REP);
        responder.bind("tcp://*:5555");
        System.out.println("ZeroMQ Server started, waiting for requests...");
        while (true) {
            // Wait for the next request from the client
            String request = responder.recvStr(0);
            System.out.println("Received request: " + request);
            // Do some 'work' and send a reply
            String reply = "Hello, Client!";
            responder.send(reply.getBytes(ZMQ.CHARSET), 0);
            System.out.println("Sent reply: " + reply);
        }
    }
}
