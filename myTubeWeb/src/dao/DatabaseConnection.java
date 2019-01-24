package dao;

import java.sql.Connection;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;

public class DatabaseConnection {
	
	private static DataSource dataSource = null;
	private static Context context = null;
	
	private static DataSource getDataSource() throws Exception{
		
		if(dataSource != null)
			return dataSource;
		
		try {
			if(context == null)
				context = new InitialContext();
			dataSource = (DataSource) context.lookup("java:/webservice");
		} catch (Exception e) {
			e.getStackTrace();
		}
		return dataSource;
	}
	/**
	 * 
	 * @return connection to data base 
	 */
	public static Connection connectToDataBase () {
		Connection conn = null;
		try {
			conn = getDataSource().getConnection();
			System.out.println(" Connection Successful !");
			return conn;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}
