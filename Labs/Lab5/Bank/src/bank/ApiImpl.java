package bank;

import java.rmi.*;
import java.rmi.server.*;
import api.*;

public class ApiImpl extends UnicastRemoteObject implements Api {
    private static final long serialVersionUID = 1L;
    private Data account = new Data(0);

    public ApiImpl() throws RemoteException {
        super();
    }

    @Override
    public synchronized Data setBalance(Data value) throws RemoteException {
        account.setValue(value.getValue());
        System.out.println("new balance: " + account.getValue());
        return new Data(account.getValue());
    }

    @Override
    public synchronized Data deposit(Data amount) throws RemoteException {
        account.setValue(account.getValue() + amount.getValue());
        System.out.println("deposited: " + amount.getValue() + ", new balance: " + account.getValue());
        return new Data(account.getValue());
    }

    @Override
    public synchronized Data withdraw(Data amount) throws RemoteException {
        account.setValue(account.getValue() - amount.getValue());
        System.out.println("withdrawn: " + amount.getValue() + ", new balance: " + account.getValue());
        return new Data(account.getValue());
    }

    @Override
    public synchronized Data addInterest(Data rate) throws RemoteException {
        double current = account.getValue();
        double newBalance = current + (current * rate.getValue() / 100.0);
        account.setValue((int) Math.round(newBalance));
        System.out.println("added interest rate " + rate.getValue() + "%, new balance: " + account.getValue());
        return new Data(account.getValue());
    }
}