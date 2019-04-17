import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.DriverPropertyInfo;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.sql.Statement;
import java.util.Properties;
import java.util.Vector;
import java.util.logging.Logger;


/*
 * Luokka tietokannan luomista varten
 */


public class SQlite implements Driver {
	
	
	//metodi uuden tietokannan luomiseksi
	 public void createNewDatabase(String fileName) {
		 
	        String url = "jdbc:sqlite:src/" + fileName;
	 
	        try (Connection conn = DriverManager.getConnection(url)) {
	            if (conn != null) {
	                DatabaseMetaData meta = conn.getMetaData();
	                System.out.println("The driver name is " + meta.getDriverName());
	                System.out.println("A new database has been created.");
	            }
	 
	        } catch (SQLException e) {
	            System.out.println(e.getMessage());
	        }
	    }
	 
	
	//metodi, jonka avulla luodaan tietokantaan taulut TAPAHTUMA ja TEHTÄVÄ
	public void createTables() {
        String url = "jdbc:sqlite:src/database.db";
        
        String sql1 = "CREATE TABLE IF NOT EXISTS TAPAHTUMA " +
                "(ID INTEGER PRIMARY KEY     AUTOINCREMENT," +
                " DAY          INT   NOT NULL, " + 
                " MONTH         INT   NOT NULL, " + 
                " YEAR         INT   NOT NULL, " + 
                " NAME           TEXT    NOT NULL, " + 
                " LOCATION       TEXT, " + 
                " START          INT, " + 
                " END            INT, "  +
                "REMINDER        BOOLEAN)";
        
        String sql2 = "CREATE TABLE IF NOT EXISTS TEHTAVA " +
                "(ID INTEGER PRIMARY KEY     AUTOINCREMENT," +
                " DAY          INT   NOT NULL, " + 
                " MONTH         INT   NOT NULL, " + 
                " YEAR         INT   NOT NULL, " + 
                " NAME           TEXT    NOT NULL," +
                "REMINDER		BOOLEAN)";	
        
        try (Connection conn = DriverManager.getConnection(url);
                Statement stmt = conn.createStatement()) {
        	//taulun varsinainen luominen
            stmt.execute(sql1);
            System.out.println("Table creation succeeded");
            stmt.execute(sql2);
            System.out.println("Table creation succeeded");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
	
	
	//metodi tietojen tallentamiseksi tietokantaan 
	public void newQuery(String message) {
		 Connection c = null;
	     Statement stmt = null;
	      
	      try {
	         Class.forName("org.sqlite.JDBC");
	         c = DriverManager.getConnection("jdbc:sqlite:src/database.db");
	         c.setAutoCommit(false);
	         System.out.println("Opened database successfully");

	         stmt = c.createStatement();
	         String sql = message; 
	         stmt.executeUpdate(sql);

	         stmt.close();
	         c.commit();
	         c.close();
	         
	      } catch ( Exception e ) {
	         System.err.println( e.getClass().getName() + ": " + e.getMessage() );
	         System.exit(0);
	      }
	      System.out.println("Query done successfully");
	   
	 }
	
	//metodi merkintöjen poistamiseksi (kesken) 
	public void delete(int id) {
		 String message = "DELETE from TAPAHTUMA where ID=? ";
		 Connection c = null;
	     Statement stmt = null;
	      
	      try {
	         Class.forName("org.sqlite.JDBC");
	         c = DriverManager.getConnection("jdbc:sqlite:src/database.db");
	         c.setAutoCommit(false);
	         
	         stmt = c.createStatement();
	         PreparedStatement ps = c.prepareStatement(message);
	        
	         ps.setInt(1, id);
	         System.out.println(id);
	         ps.executeUpdate();
	         c.commit();
	         stmt.close();
	         c.close();
	         
	      } catch ( Exception e ) {
	         System.err.println( e.getClass().getName() + ": " + e.getMessage() );
	         System.exit(0);
	      }
	      
	      System.out.println("Operation done successfully");
		
			}

	
	
	
	@Override
	public boolean acceptsURL(String url) throws SQLException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public DriverPropertyInfo[] getPropertyInfo(String url, Properties info) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getMajorVersion() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getMinorVersion() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public boolean jdbcCompliant() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Logger getParentLogger() throws SQLFeatureNotSupportedException {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public Connection connect(String url, Properties info) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}
	
	
	

}
