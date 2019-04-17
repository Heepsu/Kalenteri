import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Main {
		 
	 public static void main(String[] args) throws SQLException{
		 
		 	//Luodaan tietokanta ja sen taulut
			SQlite tietokanta = new SQlite();
	        tietokanta.createNewDatabase("database.db");
	        tietokanta.createTables();
	        
	        //Luodaan uusi kalenteri
	        Kalenteri kalenteri = new Kalenteri();
	        kalenteri.setLocationRelativeTo(null);    
	 }
}
