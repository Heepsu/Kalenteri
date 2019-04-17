import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;


public class Tehtava implements Merkinta {
	public String nimi; 
	public int pv;
	public int kk; 
	public int vuosi; 
	public boolean muistutus;
	SQlite tietokanta = new SQlite();
	
	public Tehtava() {
		nimi = this.nimi; 
		pv = this.pv;
		kk = this.kk;
		vuosi = this.vuosi;
		muistutus = false; 
	}
	
	//getterit ja setterit
	public String getNimi() {
		return nimi; 
	}
	
	public int getPv() {
		return pv; 
	}
	
	public int getKk() {
		return kk; 
	}
	
	public int getVuosi() {
		return vuosi; 
	}
	
	public void setNimi(String nimi) {
		this.nimi = nimi; 
	}
	
	public void setPv(int pv) {
		this.pv = pv; 
	}
	
	public void setKk(int kk) {
		this.kk = kk; 
	}
	
	public void setVuosi(int vuosi) {
		this.vuosi = vuosi; 
	}
	
	public void setMuistutus(boolean muistutus) {
		if(muistutus == false) {
			this.muistutus = true;
		}
		else {
			this.muistutus = false; 
		}
	}
	
	public boolean getMuistutus() {
		return muistutus;
	}
	
	
	//muut metodit
	@Override
	public void lisaa() {
		String message = "INSERT INTO TEHTAVA(NAME, DAY, MONTH, YEAR) " +
				"VALUES (?, ?, ?, ?);"; 
		Connection c = null;
		Statement stmt = null;
	  
	  try {
	     Class.forName("org.sqlite.JDBC");
	     c = DriverManager.getConnection("jdbc:sqlite:src/database.db");
	     c.setAutoCommit(false);
	     System.out.println("Opened database successfully");
	     
	     stmt = c.createStatement();
	     PreparedStatement ps = c.prepareStatement(message);
	     ps.setString(1, nimi);
	     ps.setInt(2, pv);
	     ps.setInt(3, kk);
	     ps.setInt(4, vuosi);
	     ps.executeUpdate();
	     stmt.close();
	     c.commit();
	     c.close();
	     
	  } catch ( Exception e ) {
	     System.err.println( e.getClass().getName() + ": " + e.getMessage() );
	     System.exit(0);
	  }
	  
	  System.out.println("Records created successfully");
	
			
		}

	@Override
	public void poista(int id) {
		 tietokanta.delete(id);
		
	}


	@Override
	public void muokkaa(int id) {
		// TODO Auto-generated method stub
		
	}
	
}