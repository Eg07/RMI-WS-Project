package rmiServer;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Scanner;

//import dao.DatabaseConnection;
import javafx.application.Application;
import javafx.stage.Stage;

/**
 *
 * @author Elia Al Geith 
 */
public class Server extends Application{

    private Scanner input;

	@Override
    public void start(Stage primaryStage) throws Exception {
        
        input = new Scanner(System.in);
        
        System.out.println("Set the Server ... \n ===================== \n");
        System.out.println("Enter hostname IP address : ");
        String hostname = input.nextLine();
        System.out.println("Enter server port : ");
        
        int port = input.nextInt();
        
        System.out.println("=================================== \n ");
        
        System.setProperty("java.rmi.server.hostname", hostname);
        
       // DatabaseConnection conn =  new DatabaseConnection();
       // DatabaseConnection.connectToDataBase();
        
        Registry registry = LocateRegistry.createRegistry(port);
        
        ContentServiceImpl contentServiceImpl = new ContentServiceImpl();
        
        //ContentService contentService = (ContentService) UnicastRemoteObject.exportObject(contentServiceImpl, 0);
        
        registry.bind("service", contentServiceImpl);
        
        System.out.println("Server is working very well keep going !!!");
        
    }
    
    public static void main(String[] args) {
        launch(args);
    }
    
}