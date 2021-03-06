package rmiServer;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import java.rmi.RemoteException;
import java.rmi.server.ServerNotActiveException;
import java.rmi.server.UnicastRemoteObject;
import java.sql.Blob;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import java.util.ArrayList;
import java.util.List;


import dao.DatabaseConnection;
import model.Content;
import model.User;
import rmiService.ContentService;

/**
 *
 * @author karo
 */
public class ContentServiceImpl extends UnicastRemoteObject implements ContentService {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public ContentServiceImpl() throws RemoteException {
	}

	@Override
	public void insertConent(Content content) throws RemoteException {
		PreparedStatement pst = null;
		try {
			System.out.print("\nClient " + java.rmi.server.RemoteServer.getClientHost()
					+ " request to insert a new content ... ");
		} catch (ServerNotActiveException ex) {
			System.out.println(ex.getMessage());
		}
		try {
			String query = "INSERT INTO webservice.content ( title , topic ,file , user_id) VALUES ( ? ,  ?  , ? , ? ) ";

			try {
				pst = DatabaseConnection.connectToDataBase().prepareStatement(query);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			pst.setString(1, content.getTitle());

			File myData = new File(content.getFilePath());
			FileInputStream input = new FileInputStream(myData);
			pst.setBinaryStream(3, input);

			// pst.setString(2, content.getPath());
			pst.setString(2, content.getTopic());
			pst.setInt(4, content.getUser_id());

			pst.execute();

			// JOptionPane.showMessageDialog(null, "A new content has been successfully
			// added ! ");
			pst.close();
			// resultSet.close();
			System.out.print(" [successful]");
		} catch (SQLException e) {
			System.out.print(" [failed]");
			System.out.println(e);

		} catch (FileNotFoundException ex) {
			System.out.print(" [failed]");
			System.out.println(ex);
		}
	}

	@Override
	public int updateContent(Content content, int id) throws RemoteException {
		PreparedStatement pst = null;
		int status = 0;
		try {
			System.out.print("\nClient " + java.rmi.server.RemoteServer.getClientHost()
					+ " request to update the content with id " + id);
		} catch (ServerNotActiveException ex) {
			System.out.println(ex.getMessage());
		}
		String query = "update webservice.content c inner join webservice.user u on c.user_id = u.user_id set title = ? , file = ? , topic = ?  where c.id = " + id;
		try {
			try {
				pst = DatabaseConnection.connectToDataBase().prepareStatement(query);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			pst.setString(1, content.getTitle());
			pst.setString(2, content.getFilePath());
			pst.setString(3, content.getTopic());

			pst.execute();
			// JOptionPane.showMessageDialog(null, "The content with " + id + " has been
			// updated !");
			status =pst.executeUpdate();
			pst.close();
			System.out.println("[successful]");

		} catch (SQLException e) {
			System.out.println("[failed]");
			e.printStackTrace();
		}
		return status;
	}

	@Override
	public int deleteContent(int id) throws RemoteException {
		PreparedStatement pst = null;
		int status = 0;
		try {
			System.out.print("\nClient " + java.rmi.server.RemoteServer.getClientHost()
					+ " request to delete the content with id " + id);
		} catch (ServerNotActiveException ex) {
			System.out.println(ex.getMessage());
		}
		
		String query = "DELETE c "
				+ "FROM webservice.content AS c INNER JOIN webservice.user AS u "
				+ "ON c.user_id = u.user_id  WHERE c.id = " + id;

		try {

			try {
				pst = DatabaseConnection.connectToDataBase().prepareStatement(query);
			} catch (Exception e) {
				
				e.printStackTrace();
			}
			pst.execute();
			
			status =pst.executeUpdate(); 
			pst.close();
			System.out.println("[successful]");
			return status;
		} catch (SQLException e) {
			
			System.out.println(e);
		}
		return status;
	}

	@Override
	public FileOutputStream getContentById(int id, String extension) throws RemoteException {
		try {
			System.out.print("\nClient " + java.rmi.server.RemoteServer.getClientHost()
					+ " request to download the content with id " + id);
		} catch (ServerNotActiveException ex) {
			System.out.println(ex.getMessage());
		}
		String query = "select file from webservice.content where id = " + id;
		String filename = "file " + id;
		PreparedStatement pst = null;
		ResultSet rs = null;

		try {
			try {
				pst = DatabaseConnection.connectToDataBase().prepareStatement(query);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			pst.execute();
			rs = pst.executeQuery();
			while (rs.next()) {
				// write binary stream into file
				Blob blob = rs.getBlob("file");
				BufferedInputStream is = new BufferedInputStream(blob.getBinaryStream());

				File file = new File("/home/karo/Desktop/RMI-Don/" + filename + "."+extension);
				FileOutputStream output = new FileOutputStream(file);

				byte[] buffer = new byte[1024];
				int r = 0;
				System.out.println("Writing to file " + file.getAbsolutePath());
				while ((r = is.read(buffer)) != -1) {
					output.write(buffer, 0, r);
				}
				output.flush();
				output.close();
				is.close();
				blob.free();
				return output;
			}

		} catch (SQLException | IOException e) {
			System.out.println(e.getMessage());
		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
			} catch (SQLException e) {
				System.out.println(e.getMessage());
			}
		}
		return null;
	}
	@Override
	public FileOutputStream getContentByName(String title, String extension) throws RemoteException{
		try {
			System.out.print("\nClient " + java.rmi.server.RemoteServer.getClientHost()
					+ " request to download the content with title " + title);
		} catch (ServerNotActiveException ex) {
			System.out.println(ex.getMessage());
		}
		String query = "select file from webservice.content where title = '" + title + "';";
		String filename = "file " + title;
		PreparedStatement pst = null;
		ResultSet rs = null;

		try {
			try {
				pst = DatabaseConnection.connectToDataBase().prepareStatement(query);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			pst.execute();
			rs = pst.executeQuery();
			while (rs.next()) {
				// write binary stream into file
				Blob blob = rs.getBlob("file");
				BufferedInputStream is = new BufferedInputStream(blob.getBinaryStream());

				File file = new File("/home/karo/Desktop/RMI-Don/" + filename + "."+extension);
				FileOutputStream output = new FileOutputStream(file);

				byte[] buffer = new byte[1024];
				int r = 0;
				System.out.println("Writing to file " + file.getAbsolutePath());
				while ((r = is.read(buffer)) != -1) {
					output.write(buffer, 0, r);
				}
				output.flush();
				output.close();
				is.close();
				blob.free();
				return output;
			}

		} catch (SQLException | IOException e) {
			System.out.println(e.getMessage());
		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
			} catch (SQLException e) {
				System.out.println(e.getMessage());
			}
		}
		return null;
	}
	
	
	@Override
	public List<Content> getByTopic(String topic) throws RemoteException {
		PreparedStatement pst = null;
		String query = "select * from webservice.content where topic like '%" + topic + "%' ";

		try {

			try {
				pst = DatabaseConnection.connectToDataBase().prepareStatement(query);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			pst.execute();
			ResultSet rs = pst.executeQuery();

			List<Content> list = new ArrayList<Content>();

			while (rs.next()) {
				Content content = new Content();

				content.setId(rs.getInt("id"));
				content.setTitle(rs.getString("title"));
				content.setFilePath(rs.getString("file"));
				content.setTopic(rs.getString("topic"));
				content.setUser_id(rs.getInt("user_id"));
				list.add(content);
			}
			pst.close();
			rs.close();

			return list;

		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public List<Content> getAllContent() throws RemoteException {
		PreparedStatement pst = null;
		String query = "select * from webservice.content ";

		try {
			try {
				pst = DatabaseConnection.connectToDataBase().prepareStatement(query);
			} catch (Exception e) {

				e.printStackTrace();
			}

			pst.execute();
			ResultSet rs = pst.executeQuery();

			List<Content> list = new ArrayList<Content>();

			while (rs.next()) {
				Content content = new Content();

				content.setId(Integer.parseInt(rs.getString("id")));
				content.setTitle(rs.getString("title"));
				content.setTopic(rs.getString("topic"));
				content.setUser_id(rs.getInt("user_id"));
				list.add(content);
			}
			pst.close();
			rs.close();

			return list;

		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public List<Content> search(String name) throws RemoteException {
		PreparedStatement pst = null;
		String query = "select * from webservice.content where title like '%" + name + "%' ";

		try {
			try {
				pst = DatabaseConnection.connectToDataBase().prepareStatement(query);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			pst.execute();
			ResultSet rs = pst.executeQuery();

			List<Content> list = new ArrayList<Content>();

			while (rs.next()) {
				Content content = new Content();

				content.setId(Integer.parseInt(rs.getString("id")));
				content.setTitle(rs.getString("title"));
				content.setFilePath(rs.getString("file"));
				content.setTopic(rs.getString("topic"));
				content.setUser_id(rs.getInt("user_id"));
				list.add(content);
			}
			pst.close();
			rs.close();

			return list;

		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}

	public List<Content> searchWs(String name) throws RemoteException {
		PreparedStatement pst = null;
		String query = "select * from webservice.content where title like '%" + name + "%' ";

		try {
			try {
				pst = DatabaseConnection.connectToDataBase().prepareStatement(query);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			pst.execute();
			ResultSet rs = pst.executeQuery();

			List<Content> list = new ArrayList<Content>();

			while (rs.next()) {
				Content content = new Content();

				content.setId(Integer.parseInt(rs.getString("id")));
				content.setTitle(rs.getString("title"));
				content.setTopic(rs.getString("topic"));
				content.setUser_id(rs.getInt("user_id"));
				list.add(content);
			}
			pst.close();
			rs.close();

			return list;

		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}

	public int updateContentWs(String title, String topic, int id) throws RemoteException {
		PreparedStatement pst = null;
		int status = 0;
		try {
			System.out.print("\nClient " + java.rmi.server.RemoteServer.getClientHost()
					+ " request to update the content with id " + id);
		} catch (ServerNotActiveException ex) {
			System.out.println(ex.getMessage());
		}

		String query = "update webservice.content c inner join webservice.user u on c.user_id = u.user_id set title = ?  , topic = ?  where c.id = " + id;
		try {
			try {
				pst = DatabaseConnection.connectToDataBase().prepareStatement(query);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			pst.setString(1, title);
			pst.setString(2, topic);

			pst.execute();
			status =pst.executeUpdate();
			pst.close();
			System.out.println("[successful]");
			return status;
		} catch (SQLException e) {
			System.out.println("[failed]");
			e.printStackTrace();
		}
		return status;
	}

	public List<Content> getByTopicWs(String topic) throws RemoteException {
		PreparedStatement pst = null;
		String query = "select * from webservice.content where topic like '%" + topic + "%' ";

		try {

			try {
				pst = DatabaseConnection.connectToDataBase().prepareStatement(query);
			} catch (Exception e) {
				
				e.printStackTrace();
			}

			pst.execute();
			ResultSet rs = pst.executeQuery();

			List<Content> list = new ArrayList<Content>();

			while (rs.next()) {
				Content content = new Content();

				content.setId(Integer.parseInt(rs.getString("id")));
				content.setTitle(rs.getString("title"));
				content.setTopic(rs.getString("topic"));
				content.setUser_id(rs.getInt("user_id"));
				list.add(content);
			}
			pst.close();
			rs.close();

			return list;

		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public int checkUser(User user) {
		String query = "select * from webservice.user where name = '" + user.getName() 
		+ "' and password = '" + user.getPassword() + "' ;";
		PreparedStatement pst = null;
		ResultSet rs = null;
		int user_id = 0;
		try {
			try {
				pst = DatabaseConnection.connectToDataBase().prepareStatement(query);
				
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			pst.execute();
			rs = pst.executeQuery();
			
			while (rs.next()) {
				if(rs.getString("name") != null && rs.getString("password") != null ) {
					user_id = rs.getInt("user_id");
				}	
			}
			return user_id;

		} catch (SQLException e) {
			System.out.println(e.getMessage());
		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
			} catch (SQLException e) {
				System.out.println(e.getMessage());
			}
		}

		return user_id;
	}
	
	public void addUser(User user) throws RemoteException {
		PreparedStatement pst = null;
		try {
			System.out.print("\nClient " + java.rmi.server.RemoteServer.getClientHost()
					+ " request to add a new user ... ");
		} catch (ServerNotActiveException ex) {
			System.out.println(ex.getMessage());
		}
		try {
			String query = "INSERT INTO webservice.user ( name , password) VALUES ( ? ,  ? ) ";

			try {
				pst = DatabaseConnection.connectToDataBase().prepareStatement(query);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			pst.setString(1, user.getName());

			pst.setString(2, user.getPassword());

			pst.execute();

			pst.close();
			// resultSet.close();
			System.out.print(" [successful]");
		} catch (SQLException e) {
			System.out.print(" [failed]");
			System.out.println(e);

		}
	}



}