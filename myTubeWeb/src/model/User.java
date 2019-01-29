package model;

public class User {
	private int id;
	private String name;
	private String password;
	
	public User(String name, String password) {
		this.name = name;
		this.password = password;
	}
	
	public User() {
		super();
		
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
	public String getJson(){
		return "{\"id\":\""+this.getId()+"\",\"name\":\""+this.getName()+"\", \"password\":\""+this.getPassword()+"\"}";
	}

	@Override
	public String toString() {
		return "User [id=" + id + ", name=" + name + ", password=" + password + "]";
	}
	
	
}