package com.example;

import org.zeromq.ZMQ;

public class ZmqClient {
    public static void main(String[] args) {
        // Create a ZeroMQ context
        ZMQ.Context context = ZMQ.context(1);
        // Create a socket to send requests
        ZMQ.Socket requester = context.socket(ZMQ.REQ);
        requester.connect("tcp://localhost:5555");
        System.out.println("ZeroMQ Client started, sending request...");
        // Send a request to the server
        String request = "Hello, Server!";
        requester.send(request.getBytes(ZMQ.CHARSET), 0);
        System.out.println("Sent request: " + request);
        // Receive the reply from the server
        String reply = requester.recvStr(0);
        System.out.println("Received reply: " + reply);
        // Close the requester socket and context
        requester.close();
        context.close();
    }
}
