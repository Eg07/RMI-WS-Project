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

import javax.swing.JOptionPane;

import dao.DatabaseConnection;
import model.Content;
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
			String query = "INSERT INTO webservice.content ( title , topic ,file ) VALUES ( ? ,  ?  , ? ) ";

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
	public void updateContent(Content content, int id) throws RemoteException {
		PreparedStatement pst = null;
		try {
			System.out.print("\nClient " + java.rmi.server.RemoteServer.getClientHost()
					+ " request to update the content with id " + id);
		} catch (ServerNotActiveException ex) {
			System.out.println(ex.getMessage());
		}

		String query = "update webservice.content set title = ? , file = ? , topic = ?  where id = " + id;
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
			pst.close();
			System.out.println("[successful]");

		} catch (SQLException e) {
			System.out.println("[failed]");
			e.printStackTrace();
		}
	}

	@Override
	public void deleteContent(int id) throws RemoteException {
		PreparedStatement pst = null;
		try {
			System.out.print("\nClient " + java.rmi.server.RemoteServer.getClientHost()
					+ " request to delete the content with id " + id);
		} catch (ServerNotActiveException ex) {
			System.out.println(ex.getMessage());
		}

		String query = "delete from webservice.content where id = " + id;

		try {

			try {
				pst = DatabaseConnection.connectToDataBase().prepareStatement(query);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			pst.execute();
			// JOptionPane.showMessageDialog(null, "Content with ID " + id + " has been
			// removed !");
			pst.close();
			System.out.println("[successful]");

		} catch (SQLException e) {
			JOptionPane.showMessageDialog(null, e);
		}
	}

	@Override
	public FileOutputStream getContentById(int id) throws RemoteException {
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

				File file = new File("/home/karo/Desktop/RMI-Don/" + filename + ".jpg");
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
	public FileOutputStream getContentByName(String title) throws RemoteException{
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

				File file = new File("/home/karo/Desktop/RMI-Don/" + filename + ".jpg");
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

				content.setId(Integer.parseInt(rs.getString("id")));
				content.setTitle(rs.getString("title"));
				content.setFilePath(rs.getString("file"));
				content.setTopic(rs.getString("topic"));
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

	public void updateContentWs(String title, String topic, int id) throws RemoteException {
		PreparedStatement pst = null;
		try {
			System.out.print("\nClient " + java.rmi.server.RemoteServer.getClientHost()
					+ " request to update the content with id " + id);
		} catch (ServerNotActiveException ex) {
			System.out.println(ex.getMessage());
		}

		String query = "update webservice.content set title = ? , topic = ?  where id = " + id;
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
			// JOptionPane.showMessageDialog(null, "The content with " + id + " has been
			// updated !");
			pst.close();
			System.out.println("[successful]");

		} catch (SQLException e) {
			System.out.println("[failed]");
			e.printStackTrace();
		}
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

}