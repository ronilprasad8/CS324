package customer;

import java.rmi.registry.*;
import api.*;

public class Customer {
    private static final String HOST = "localhost";
    private static final int PORT = 1099;
    private static Registry registry;   

    public static void main(String[] args) throws Exception {
        registry = LocateRegistry.getRegistry(HOST, PORT);
        Api remoteApi = (Api) registry.lookup(Api.class.getSimpleName());
        
        Data depositResult = remoteApi.deposit(new Data(500));
        System.out.println("Deposit 500: New balance = " + depositResult.getValue());

        Data withdrawResult = remoteApi.withdraw(new Data(200));
        System.out.println("Withdraw 200: New balance = " + withdrawResult.getValue());

        Data interestResult = remoteApi.addInterest(new Data(10));
        System.out.println("Add 10% Interest: New balance = " + interestResult.getValue());
    }
}