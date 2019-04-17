import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

/*
 * Luokka tapahtumamerkintöjen luomista varten
 * pv = päivä
 * kk = kuukausi
 */

public class Tapahtuma implements Merkinta{
	public String nimi; 
	public int pv; 
	public int kk; 
	public int vuosi; 
	public String sijainti;
	public int aloitusaika;
	public int lopetusaika;
	public boolean muistutus; 
	SQlite tietokanta = new SQlite();
	
	public Tapahtuma() {
		nimi = this.nimi; 
		pv = this.pv;
		kk = this.kk;
		vuosi = this.vuosi;
		sijainti = this.sijainti; 
		aloitusaika = this.aloitusaika;
		lopetusaika = this.lopetusaika; 
		muistutus = false; 
		tietokanta = this.tietokanta; 
		
	}
	
	//getterit ja setterit
	public String getNimi() {
		return nimi; 
	}
	
	public String getSijainti() {
		return sijainti; 
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
	
	public int getAloitusaika() {
		return aloitusaika;
	}
	
	public int getLopetusaika() {
		return lopetusaika; 
	}
	
	public void setNimi(String nimi) {
		this.nimi = nimi; 
	}
	
	public void setSijainti(String sijainti) {
		this.sijainti = sijainti; 
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
	
	public void setAloitusaika(int aloitusaika) {
		this.aloitusaika = aloitusaika; 
	}
	
	public void setLopetusaika(int lopetusaika) {
		this.lopetusaika = lopetusaika; 
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
	
	/*
	 * Toteutetaan Merkintä-rajapinnassa määrittelyt metodit
	 */
	
	@Override
	public void lisaa(){
		String message = "INSERT INTO TAPAHTUMA(DAY, MONTH, YEAR, NAME, LOCATION, START, END) " +
						"VALUES (?, ?, ?, ?, ?, ?, ? );"; 
		Connection c = null;
	    Statement stmt = null;
	      
	      try {
	         Class.forName("org.sqlite.JDBC");
	         c = DriverManager.getConnection("jdbc:sqlite:src/database.db");
	         c.setAutoCommit(false);
	         
	         
	         stmt = c.createStatement();
	         PreparedStatement ps = c.prepareStatement(message);
	         ps.setInt(1, pv);
	         ps.setInt(2, kk );
	         ps.setInt(3, vuosi );
	         ps.setString(4, nimi);     
	         ps.setString(5, sijainti);
	         ps.setInt(6, aloitusaika);
	         ps.setInt(7, lopetusaika);
	         ps.executeUpdate();
	   
	         stmt.close();
	         c.commit();
	         c.close();
	         
	        
	         
	      } catch ( Exception e ) {
	         System.err.println( e.getClass().getName() + ": " + e.getMessage() );
	         System.exit(0);
	      }	
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

