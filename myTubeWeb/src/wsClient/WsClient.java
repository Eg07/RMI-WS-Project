package wsClient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.rmi.NotBoundException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

import javax.ws.rs.core.MediaType;


import model.Content;
import model.User;
import rmiService.ContentService;

public class WsClient {
	private static boolean logStatus;
	private static int user_id;
	private static Scanner scanner;
	private static ContentService contentService;

	public static void main(String[] args) throws IOException, NotBoundException {
		scanner = new Scanner(System. in);
		

		System.out.println("\tConnect to Server ... \n\t=====================\n");
		System.out.println("\tEnter server IP address : ");
		String ip = scanner.nextLine();

		System.out.println("\tEnter server port : ");

		int port = scanner.nextInt();

		System.out.println("\t===================================");

		Registry registry = LocateRegistry.getRegistry(ip, port);

		contentService = (ContentService) registry.lookup("service");
		
		welcome();
		
		char c = 'c';
		System.out.println("\t\t\t\t\t\t-----------------------------------------");
		System.out.println("\t\t\t\t\t\t| Welcome to My Tube Web Service        |");
		System.out.println("\t\t\t\t\t\t-----------------------------------------");
		opt();
		while (true) {
			
			c = (char) System.in.read();
			//get all
			if(c == 's' || c == 'S' ) {
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
			else if(c == 'a' || c == 'A' ) {
				String title;
				
				System.out.println("\tplease enter the title: ");
				title = scanner.next();
				
				String topic;
				System.out.println("\tplease enter the topic: ");
				topic = scanner.next();
				
				String path;
				System.out.println("\tplease enter the path: ");
				path = scanner.next();
				
				Content cont = new Content();
				cont.setFilePath(path);
				cont.setTitle(title);
				cont.setTopic(topic);
				cont.setUser_id(user_id);
				postNewContent(cont);
				try {
					TimeUnit.SECONDS.sleep(2);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				opt();
				continue;
			}
			
			else if(c == 'd' || c == 'D' ) {
				int id;
				System.out.println("\tplease enter the id : ");
				while (!scanner.hasNextInt()) { 
					System.out.print("Error: Please just enter numbers ! \n");
					scanner.next();
				}
				id = scanner.nextInt();
				
				delete(String.valueOf(id));
				try {
					TimeUnit.SECONDS.sleep(2);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				opt();
				continue;
			}
			
			else if(c == 'u' || c == 'U' ) {
				int id;
				String title, topic;
				System.out.println("\tplease enter the id : ");
				while (!scanner.hasNextInt()) { 
					System.out.print("Error: Please just enter numbers ! \n");
					scanner.next();
				}
				id = scanner.nextInt();
				
				System.out.println("\tplease enter the title: ");
				title = scanner.next();
				
				System.out.println("\tplease enter the topic: ");
				topic = scanner.next();
				
				update(title, topic, String.valueOf(id));
				try {
					TimeUnit.SECONDS.sleep(2);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				opt();
				continue;
			}
			
			else if(c == 'f' || c == 'F' ) {
				String title;
				
				System.out.println("\tplease enter the title: ");
				title = scanner.next();
				search(title);
				try {
					TimeUnit.SECONDS.sleep(2);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				opt();
				continue;
			}
			
			else if(c == 't' || c == 'T' ) {
				String topic;
				
				System.out.println("\tplease enter the topic: ");
				topic = scanner.next();
				getTopic(topic);
				try {
					TimeUnit.SECONDS.sleep(2);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				opt();
				continue;
			}
			
			else if(c == 'g' || c == 'G' ) {
				int id;
				System.out.println("\tplease enter the id : ");
				while (!scanner.hasNextInt()) { 
					System.out.print("Error: Please just enter numbers ! \n");
					scanner.next();
				}
				id = scanner.nextInt();
				
				String extension;
				
				System.out.println("\tplease enter the file extension: ");
				extension = scanner.next();
				
				downloadId(String.valueOf(id),extension);
				try {
					TimeUnit.SECONDS.sleep(2);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				opt();
				continue;
			}
			
			else if(c == 'n' || c == 'N' ) {
				String title,extension;
				
				System.out.println("\tplease enter the title: ");
				title = scanner.next();
				
				System.out.println("\tplease enter the file extension: ");
				extension = scanner.next();
				
				downloadName(title,extension);
				try {
					TimeUnit.SECONDS.sleep(2);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				opt();
				continue;
			}
			
			else if(c == 'e' || c == 'E' )
				break;
			
		} 
		
		System.out.println("\t| Thanks for using My Tube Web Service|");

	}
	
	private static void getAll() {
		try {
			URL url = new URL("http://localhost:8080/myTubeWeb/rest/all");
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("GET");
			conn.setRequestProperty("Accept", MediaType.APPLICATION_JSON);
			if (conn.getResponseCode() != 200) {
				throw new RuntimeException("Failed: HTTP error code: " + conn.getResponseCode());
			}
						
			BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			String output = br.readLine();
			ArrayList<String> list = new ArrayList<>();
			
			for(String str : output.split("},")) 
				list.add(str + "}");
			
			for (String s : list)
				System.out.println("\t " + s);
			
			conn.disconnect();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private static void postNewContent(Content newContent) {
		try {
			URL url = new URL("http://localhost:8080/myTubeWeb/rest/post");
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setDoOutput(true);
			conn.setRequestMethod("POST");
			conn.setRequestProperty("Content-Type", "application/json");
			
			String jsonFromPojo = newContent.getJson();
			
			OutputStream os = conn.getOutputStream();
			os.write(jsonFromPojo.getBytes());
			os.flush();
			if (conn.getResponseCode() != 200 ) {
				throw new RuntimeException("Failed: HTTP error code: " + conn.getResponseCode());
			}
			BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			String output;
			while ((output = br.readLine()) != null) {
				System.out.println("\nClient POST new Content. Response: " + output);
			}
			conn.disconnect();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private static void delete(String id) {
		try {
			URL url = new URL("http://localhost:8080/myTubeWeb/rest/delete?id="+id);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setDoOutput(true);
			conn.setRequestProperty("Accept", MediaType.APPLICATION_JSON);
			conn.setRequestMethod("GET");
			
			if (conn.getResponseCode() != 200 ) {
				throw new RuntimeException("Failed: HTTP error code: " + conn.getResponseCode());
			}
			BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			String output;
			while ((output = br.readLine()) != null) {
				System.out.println("\nClient Delete a contnent with id: "+id+" Response: " + output);
			}
			conn.disconnect();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private static void search(String title) {
		try {
			URL url = new URL("http://localhost:8080/myTubeWeb/rest/search?title="+title);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setDoOutput(true);
			conn.setRequestProperty("Accept", MediaType.APPLICATION_JSON);
			conn.setRequestMethod("GET");
			
			if (conn.getResponseCode() != 200 ) {
				throw new RuntimeException("Failed: HTTP error code: " + conn.getResponseCode());
			}
			BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			String output = br.readLine();
			ArrayList<String> list = new ArrayList<>();
			
			for(String str : output.split("},")) 
				list.add(str + "}");
			
			for (String s : list)
				System.out.println("\t " + s);
			conn.disconnect();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private static void update(String title, String topic, String id) {
		try {
			URL url = new URL("http://localhost:8080/myTubeWeb/rest/update?title="+title+"&topic="+topic+
					"&id="+id);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setDoOutput(true);
			conn.setRequestProperty("Accept", MediaType.APPLICATION_JSON);
			conn.setRequestMethod("GET");
			
			if (conn.getResponseCode() != 200 ) {
				throw new RuntimeException("Failed: HTTP error code: " + conn.getResponseCode());
			}
			BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			String output;
			while ((output = br.readLine()) != null) {
				System.out.println("\nClient Update Content. Response: " + output);
			}
			conn.disconnect();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private static void getTopic(String topic) {
		try {
			URL url = new URL("http://localhost:8080/myTubeWeb/rest/search?title="+topic);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setDoOutput(true);
			conn.setRequestProperty("Accept", MediaType.APPLICATION_JSON);
			conn.setRequestMethod("GET");
			
			if (conn.getResponseCode() != 200 ) {
				throw new RuntimeException("Failed: HTTP error code: " + conn.getResponseCode());
			}
			BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			String output = br.readLine();
			ArrayList<String> list = new ArrayList<>();
			
			for(String str : output.split("},")) 
				list.add(str + "}");
			
			for (String s : list)
				System.out.println("\t " + s);
			conn.disconnect();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private static void downloadId(String id , String extension) {
		try {
			URL url = new URL("http://localhost:8080/myTubeWeb/rest/getId?id="+id+"&extension="+extension);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setDoOutput(true);
			conn.setRequestProperty("Accept", MediaType.APPLICATION_OCTET_STREAM);
			conn.setRequestMethod("GET");
			
			if (conn.getResponseCode() != 200 ) {
				throw new RuntimeException("Failed: HTTP error code: " + conn.getResponseCode());
			}
			BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			String output;
			while ((output = br.readLine()) != null) {
				System.out.println("\nClient Download by ID . Response: " + output);
			}
			conn.disconnect();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private static void downloadName(String name , String extension) {
		try {
			URL url = new URL("http://localhost:8080/myTubeWeb/rest/getName?name="+name+"&extension="+extension);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setDoOutput(true);
			conn.setRequestProperty("Accept", MediaType.APPLICATION_OCTET_STREAM);
			conn.setRequestMethod("GET");
			
			if (conn.getResponseCode() != 200 ) {
				throw new RuntimeException("Failed: HTTP error code: " + conn.getResponseCode());
			}
			BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			String output;
			while ((output = br.readLine()) != null) {
				System.out.println("\nClient Download by Name . Response: " + output);
			}
			conn.disconnect();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private static void checkUser(String name, String password) {
		try {
			URL url = new URL("http://localhost:8080/myTubeWeb/rest/checkUser?name="+name+"&password="+password);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setDoOutput(true);
			conn.setRequestProperty("Accept", MediaType.APPLICATION_JSON);
			conn.setRequestMethod("GET");
			
			if (conn.getResponseCode() != 200 ) {
				throw new RuntimeException("Failed: HTTP error code: " + conn.getResponseCode());
			}
			BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			String output;
			
			while ((output = br.readLine()) != null) {
				if(isInteger(output)) {
					user_id = Integer.parseInt(output);
					logStatus = true;
				}
				//System.out.println("\nClient check user. Response: " + output);
			}
			conn.disconnect();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private static void postNewUser(User user) {
		try {
			URL url = new URL("http://localhost:8080/myTubeWeb/rest/addUser");
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setDoOutput(true);
			conn.setRequestMethod("POST");
			conn.setRequestProperty("Content-Type", "application/json");
			
			String jsonFromPojo = user.getJson();
			
			OutputStream os = conn.getOutputStream();
			os.write(jsonFromPojo.getBytes());
			os.flush();
			if (conn.getResponseCode() != 200 ) {
				throw new RuntimeException("Failed: HTTP error code: " + conn.getResponseCode());
			}
			BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			String output;
			while ((output = br.readLine()) != null) {
				System.out.println("\nClient POST new User. Response: " + output);
			}
			conn.disconnect();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
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
	
	private static void welcome() throws IOException {
		char c = 'c';
		System.out.println("\t\t\t\t\t\t-----------------------------------------");
		System.out.println("\t\t\t\t\t\t|please choose an option                |");
		System.out.println("\t\t\t\t\t\t|x : to add a new user                  |");
		System.out.println("\t\t\t\t\t\t|y : to log in with existing user       |");
		System.out.println("\t\t\t\t\t\t-----------------------------------------");
		boolean in = true;
		while (in) {
			c = (char) System.in.read();
			//get all
			if(c == 'x' || c == 'X' ) {
				signIn();
				try {
					TimeUnit.SECONDS.sleep(2);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				System.out.println("\t\t\t\t\t\t good job now you are in our system      ");
				System.out.println("\t\t\t\t\t\t please log in with your login data      ");
				System.out.println("\t\t\t\t\t\t-----------------------------------------");
				login();
				in=false;
			}
			else if(c == 'y' || c == 'Y' ) {
				login();
				in = false;
			}
			else {
				System.out.println("\t\t\t\t\t\twrong option please x or y               ");
				continue;
			}
		}
	}
	
	private static void signIn() {
		String userName;
		
		System.out.println("\tplease enter user's name: ");
		userName = scanner.next();
		
		String userPassWord;
		System.out.println("\tplease enter user's password: ");
		userPassWord = scanner.next();
		
		User user = new User(userName,userPassWord);
		postNewUser(user);
	}
	private static void login() {
		while(!logStatus) {
			String userName, userPassWord;
			
			System.out.println("\tplease enter user's name: ");
			userName = scanner.next();
			
			System.out.println("\tplease enter user's password: ");
			userPassWord = scanner.next();
	
			
			checkUser(userName, userPassWord);
			if(logStatus)
				System.out.println("Log in !!!!");
			else {
				System.out.println("User name or password is wrong !");
			}
		}
		
		try {
			TimeUnit.SECONDS.sleep(2);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

	}
	
	private static boolean isInteger(String s) {
	    try { 
	        Integer.parseInt(s); 
	    } catch(NumberFormatException e) { 
	        return false; 
	    } catch(NullPointerException e) {
	        return false;
	    }
	    return true;
	}


}
