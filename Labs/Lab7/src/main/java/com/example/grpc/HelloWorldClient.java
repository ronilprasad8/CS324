package com.example.grpc;

/** 
 * 
 * @author ronilprasad8
 */
import com.example.grpc.HelloWorldProto.HelloReply;
import com.example.grpc.HelloWorldProto.HelloRequest;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;

public class HelloWorldClient {
    public static void main(String[] args) throws InterruptedException {
        ManagedChannel channel = ManagedChannelBuilder.forAddress("localhost", 50051)
                .usePlaintext()
                .build();

        GreeterGrpc.GreeterBlockingStub stub = GreeterGrpc.newBlockingStub(channel);

        HelloReply response = stub.sayHello(HelloRequest.newBuilder().setName("World").build());

        System.out.println("Greeting: " + response.getMessage());

        channel.shutdown();
    }
}