package rmiService;


import java.io.FileOutputStream;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

import model.Content;

/**
 *
 * @author karo
 */
public interface ContentService extends Remote {
    
    void insertConent(Content content) throws RemoteException;
    
    int updateContent(Content content, int id ) throws RemoteException;
    
    int deleteContent(int id) throws RemoteException;
    
    FileOutputStream getContentById(int id, String extension) throws RemoteException;
    FileOutputStream getContentByName(String title, String extension) throws RemoteException;
    
    List<Content> getByTopic(String topic) throws RemoteException;
    
    List<Content> getAllContent() throws RemoteException;
    
    List<Content> search(String topic) throws RemoteException;
    
}