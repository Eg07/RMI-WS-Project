package model;

import java.io.Serializable;

public class Content implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1;
	
	private int id;
	private String title;
	private String topic;
	private String filePath;
	private int user_id;
	
	public Content() { }

	public Content(int id, String title, String topic, String filePath , int user_id) {
		super();
		this.id = id;
		this.title = title;
		this.topic = topic;
		this.filePath = filePath;
		this.user_id= user_id;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getTopic() {
		return topic;
	}

	public void setTopic(String topic) {
		this.topic = topic;
	}

	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}
	
	public int getUser_id() {
		return user_id;
	}

	public void setUser_id(int user_id) {
		this.user_id = user_id;
	}

	public String getJson(){
		return "{\"id\":\""+this.getId()+"\",\"title\":\""+this.getTitle()+"\", \"topic\":\""+this.getTopic()+"\", \"filePath\":\""+this.getFilePath()
		+"\", \"user_id\":\""+this.getUser_id()+"\"}";
	}

	@Override
	public String toString() {
		return "Content [id=" + id + ", title=" + title + ", topic=" + topic + ", filePath=" + filePath + ", user_id="
				+ user_id + "]";
	}

	
	
	
	
}