package api;

import java.rmi.*;

public interface Api extends Remote {
    public Data setBalance(Data value) throws RemoteException;
    public Data deposit(Data amount) throws RemoteException;
    public Data withdraw(Data amount) throws RemoteException;
    public Data addInterest(Data rate) throws RemoteException;
}