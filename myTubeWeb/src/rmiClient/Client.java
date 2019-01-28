package rmiClient;

import java.io.IOException;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;


import model.Content;
import rmiServer.ContentServiceImpl;


public class Client {

	private static ContentServiceImpl contentService;
	private static Scanner input;

	public static void main(String[] args) throws NotBoundException, IOException {
		input = new Scanner(System.in);

		System.out.println("Connect to Server ... \n ===================== \n");
		System.out.println("Enter server IP address : ");
		String ip = input.nextLine();

		System.out.println("Enter server port : ");

		int port = input.nextInt();

		System.out.println("===================================");

		Registry registry = LocateRegistry.getRegistry(ip, port);

		contentService = (ContentServiceImpl) registry.lookup("service");

		char c = 'c';
		System.out.println("\t\t\t\t\t\t-----------------------------------------");
		System.out.println("\t\t\t\t\t\t| Welcome to My Tube RMI Application    |");
		System.out.println("\t\t\t\t\t\t-----------------------------------------");
		opt();
		while (true) {

			c = (char) System.in.read();
			// get all
			if (c == 's' || c == 'S') {
				getAll();
				try {
					TimeUnit.SECONDS.sleep(2);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				opt();
				continue;
			}
			// post a new content
			else if (c == 'a' || c == 'A') {
				String title, topic, path;

				System.out.println("\tplease enter the title: ");
				title = input.next();

				System.out.println("\tplease enter the topic: ");
				topic = input.next();

				System.out.println("\tplease enter the path: ");
				path = input.next();

				Content cont = new Content();
				cont.setFilePath(path);
				cont.setTitle(title);
				cont.setTopic(topic);
				postNewContent(cont);
				try {
					TimeUnit.SECONDS.sleep(2);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				opt();
				continue;
			}

			else if (c == 'd' || c == 'D') {
				int id;
				System.out.println("\tplease enter the id : ");
				while (!input.hasNextInt()) {
					System.out.print("Error: Please just enter numbers ! \n");
					input.next();
				}
				id = input.nextInt();

				delete(String.valueOf(id));
				try {
					TimeUnit.SECONDS.sleep(2);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				opt();
				continue;
			}

			else if (c == 'u' || c == 'U') {
				int id;
				String title, topic;
				System.out.println("\tplease enter the id : ");
				while (!input.hasNextInt()) {
					System.out.print("Error: Please just enter numbers ! \n");
					input.next();
				}
				id = input.nextInt();

				System.out.println("\tplease enter the title: ");
				title = input.next();

				System.out.println("\tplease enter the topic: ");
				topic = input.next();

				update(title, topic, String.valueOf(id));
				try {
					TimeUnit.SECONDS.sleep(2);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				opt();
				continue;
			}

			else if (c == 'f' || c == 'F') {
				String title;

				System.out.println("\tplease enter the title: ");
				title = input.next();
				search(title);
				try {
					TimeUnit.SECONDS.sleep(2);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				opt();
				continue;
			}

			else if (c == 't' || c == 'T') {
				String topic;

				System.out.println("\tplease enter the topic: ");
				topic = input.next();
				getTopic(topic);
				try {
					TimeUnit.SECONDS.sleep(2);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				opt();
				continue;
			}

			else if (c == 'g' || c == 'G') {
				int id;
				System.out.println("\tplease enter the id : ");
				while (!input.hasNextInt()) {
					System.out.print("Error: Please just enter numbers ! \n");
					input.next();
				}
				id = input.nextInt();

				downloadId(String.valueOf(id));
				try {
					TimeUnit.SECONDS.sleep(2);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				opt();
				continue;
			}

			else if (c == 'n' || c == 'N') {
				String title;

				System.out.println("\tplease enter the title: ");
				title = input.next();

				downloadName(title);
				try {
					TimeUnit.SECONDS.sleep(2);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				opt();
				continue;
			}

			else if (c == 'e' || c == 'E')
				break;

		}

		System.out.println("\t| Thanks for using My Tube Web Service|");

	}

	private static void getAll() throws RemoteException {
		List<Content> temp = new ArrayList<>();
		temp = contentService.getAllContent();
		
		for (Content c : temp)
			System.out.println(c.toString());
	}

	private static void postNewContent(Content newContent) throws RemoteException {
		contentService.insertConent(newContent);
	}

	private static void delete(String id) throws NumberFormatException, RemoteException {
		contentService.deleteContent(Integer.parseInt(id));
	}

	private static void search(String title) throws RemoteException {
		contentService.search(title);
	}

	private static void update(String title, String topic, String id) throws NumberFormatException, RemoteException {
		Content temp = new Content();
		temp.setTitle(title);
		temp.setTopic(topic);
		contentService.updateContent(temp, Integer.parseInt(id));
	}

	private static void getTopic(String topic) throws RemoteException {
		List<Content> temp = new ArrayList<>();
		temp = contentService.getByTopic(topic);
		
		for (Content c : temp)
			System.out.println(c.toString());
	}

	private static void downloadId(String id) throws NumberFormatException, RemoteException {
		contentService.getContentById(Integer.parseInt(id));
	}

	private static void downloadName(String name) throws RemoteException {
		contentService.getContentByName(name);
	}

	private static void opt() {
		System.out.println("\t\t\t\t\t\t-----------------------------------------");
		System.out.println("\t\t\t\t\t\t| Please pick an option                 |");
		System.out.println("\t\t\t\t\t\t| s : to list all                       |");
		System.out.println("\t\t\t\t\t\t| a : to add new contnet                |");
		System.out.println("\t\t\t\t\t\t| u : to update a contnet               |");
		System.out.println("\t\t\t\t\t\t| d : to delete a contnet               |");
		System.out.println("\t\t\t\t\t\t| f : to find a contnet with title      |");
		System.out.println("\t\t\t\t\t\t| t : to find a contnet with topic      |");
		System.out.println("\t\t\t\t\t\t| g : to download a contnet with id     |");
		System.out.println("\t\t\t\t\t\t| n : to download a contnet with name   |");
		System.out.println("\t\t\t\t\t\t| e : to exit                           |");
		System.out.println("\t\t\t\t\t\t-----------------------------------------");

	}

}
