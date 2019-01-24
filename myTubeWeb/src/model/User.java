package model;

public class User {
	private int id;
	private String name;
	private String password;
	
	public User(String name, String password) {
		this.name = name;
		this.password = password;
	}

	public int getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public String getPassword() {
		return password;
	}
}