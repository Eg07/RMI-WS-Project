package rmiClient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

import javax.ws.rs.core.MediaType;

import model.Content;
import rmiService.ContentService;

public class Client {

	private static ContentService contentService;
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

		contentService = (ContentService) registry.lookup("service");

		char c = 'c';
		System.out.println("\t\t\t\t\t\t-----------------------------------------");
		System.out.println("\t\t\t\t\t\t| Welcome to My Tube Web Service        |");
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
			if (conn.getResponseCode() != 200) {
				throw new RuntimeException("Failed: HTTP error code: " + conn.getResponseCode());
			}
			BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			String output;
			while ((output = br.readLine()) != null) {
				System.out.println("\nClient POST. Response: " + output);
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
			URL url = new URL("http://localhost:8080/myTubeWeb/rest/delete?id=" + id);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setDoOutput(true);
			conn.setRequestProperty("Accept", MediaType.APPLICATION_JSON);
			conn.setRequestMethod("GET");

			if (conn.getResponseCode() != 200) {
				throw new RuntimeException("Failed: HTTP error code: " + conn.getResponseCode());
			}
			BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			String output;
			while ((output = br.readLine()) != null) {
				System.out.println("\nClient Search. Response: " + output);
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
			URL url = new URL("http://localhost:8080/myTubeWeb/rest/search?title=" + title);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setDoOutput(true);
			conn.setRequestProperty("Accept", MediaType.APPLICATION_JSON);
			conn.setRequestMethod("GET");

			if (conn.getResponseCode() != 200) {
				throw new RuntimeException("Failed: HTTP error code: " + conn.getResponseCode());
			}
			BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			String output = br.readLine();
			ArrayList<String> list = new ArrayList<>();

			for (String str : output.split("},"))
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
			URL url = new URL(
					"http://localhost:8080/myTubeWeb/rest/update?title=" + title + "&topic=" + topic + "&id=" + id);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setDoOutput(true);
			conn.setRequestProperty("Accept", MediaType.APPLICATION_JSON);
			conn.setRequestMethod("GET");

			if (conn.getResponseCode() != 200) {
				throw new RuntimeException("Failed: HTTP error code: " + conn.getResponseCode());
			}
			BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			String output;
			while ((output = br.readLine()) != null) {
				System.out.println("\nClient Search. Response: " + output);
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
			URL url = new URL("http://localhost:8080/myTubeWeb/rest/search?title=" + topic);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setDoOutput(true);
			conn.setRequestProperty("Accept", MediaType.APPLICATION_JSON);
			conn.setRequestMethod("GET");

			if (conn.getResponseCode() != 200) {
				throw new RuntimeException("Failed: HTTP error code: " + conn.getResponseCode());
			}
			BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			String output = br.readLine();
			ArrayList<String> list = new ArrayList<>();

			for (String str : output.split("},"))
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

	private static void downloadId(String id) {
		try {
			URL url = new URL("http://localhost:8080/myTubeWeb/rest/getId?id=" + id);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setDoOutput(true);
			conn.setRequestProperty("Accept", MediaType.APPLICATION_OCTET_STREAM);
			conn.setRequestMethod("GET");

			if (conn.getResponseCode() != 200) {
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

	private static void downloadName(String name) {
		try {
			URL url = new URL("http://localhost:8080/myTubeWeb/rest/getName?name=" + name);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setDoOutput(true);
			conn.setRequestProperty("Accept", MediaType.APPLICATION_OCTET_STREAM);
			conn.setRequestMethod("GET");

			if (conn.getResponseCode() != 200) {
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
