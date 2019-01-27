package rmiServer;

import java.rmi.AlreadyBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Scanner;

//import dao.DatabaseConnection;

public class ServerC {

	private static Scanner input;

	public static void main(String[] args) throws RemoteException, AlreadyBoundException {
		input = new Scanner(System.in);
        
        System.out.println("Set the Server ... \n=====================\n");
        System.out.println("Enter hostname IP address : ");
        String hostname = input.nextLine();
        System.out.println("Enter server port : ");
        
        int port = input.nextInt();
        
        System.out.println("=================================== \n ");
        
        System.setProperty("java.rmi.server.hostname", hostname);
        
        //DatabaseConnection.connectToDataBase();
        
        Registry registry = LocateRegistry.createRegistry(port);
        
        ContentServiceImpl contentServiceImpl = new ContentServiceImpl();
        
        //ContentService contentService = (ContentService) UnicastRemoteObject.exportObject(contentServiceImpl, 0);
        
        registry.bind("service", contentServiceImpl);
        
        System.out.println("Server is working very well keep going !!!");


	}

}
