package rmiService;

import java.rmi.*;

import model.Content;


//Interface of the callback's implementation
public interface CallbackInterface extends Remote{

    public String callMe(String message) throws java.rmi.RemoteException;	
    
    public int chooseD(Content[] af) throws RemoteException;
}