import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.LinkedList;
import java.util.Locale;
import java.util.Vector;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;


/*
 * Luokasta voidaan luoda erilaisia näkymiä, jotka esittävät tietokannasta haetut tiedot taulukkona käyttäjälle
 * Nakymalla on kaksi konstruktoria, joista ensimmäinen ottaa parametrina päivän kuukauden ja vuoden ja näyttää taulukkona kaikki kyseisen päivän merkinnät
 * Toinen konstruktori hakee tiedot kaikista merkinnöistä
 */

public class Nakyma extends JFrame implements ActionListener {
	SQlite tietokanta = new SQlite();
	
	Connection con = null;
	Statement st = null; 
	ResultSet rs = null;
	String s;
	ResultSetMetaData rsmt = null;
	int c = 0; 
	
	//vektorit tiedon esittämistä varten
	Vector column = new Vector(c);
	Vector data = new Vector(); 
	Vector row = new Vector();
	
	
	JTable table = new JTable(data,column);
	JButton delete = new JButton("delete");
	JFrame frame = new JFrame(); 
	JPanel panel = new JPanel(); 
  	
	
	 
	public Nakyma(int day, int month, int year) { 
		try{ 
			con = DriverManager.getConnection("jdbc:sqlite:src/database.db");
			st = con.createStatement();
			s = "SELECT * FROM TAPAHTUMA WHERE DAY=" + day + " AND " + " MONTH=" + month + " AND " + " YEAR=" + year + " " ;
			rs = st.executeQuery(s);
			
			ResultSetMetaData rsmt = rs.getMetaData(); 
			int c = rsmt.getColumnCount();
			Vector column = new Vector(c);
			
	
			 for(int i = 1; i <= c; i++) {
					column.add(rsmt.getColumnName(i)); 
				 } 

			
			//   System.out.println( "DAY = " + day);
		    //   System.out.println( "MONTH = " + month );
		    //   System.out.println( "YEAR = " + year );
		     
			Vector data = new Vector(); 
			Vector row = new Vector(); 

			while(rs.next()) { 
			row = new Vector(c); 

		    for(int i = 1; i <= c; i++){
		    row.add(rs.getString(i)); 
		    } 

		    data.add(row); 
		    
		 } 

		 table = new JTable(data,column){
				public boolean isCellEditable(int rowIndex, int mColIndex){
					return false;
					}
				};
		 }
		
		catch(Exception e){ System.out.println(e.getMessage());
		
		}
		initGUI(); 
	}
	
	
	public Nakyma() {   
		try{ 
			con = DriverManager.getConnection("jdbc:sqlite:src/database.db");
			st = con.createStatement();
			s = "SELECT * FROM TAPAHTUMA, TEHTAVA" ;
			rs = st.executeQuery(s);	
		    rsmt = rs.getMetaData(); 
		    c = rsmt.getColumnCount();
	
			for(int i = 1; i <= c; i++) {
					column.add(rsmt.getColumnName(i)); 
				 } 
			
			while(rs.next()) { 
			row = new Vector(c); 
		    for(int i = 1; i <= c; i++){
		    row.add(rs.getString(i));
		    } 
		    data.add(row); 
		 } 
		 table = new JTable(data,column){
				public boolean isCellEditable(int rowIndex, int mColIndex){
					return false;
					}
				};	
		}
		catch(Exception e){ System.out.println(e.getMessage());
		}
		
		initGUI();
		panel.add(delete, BorderLayout.SOUTH);
		delete.addActionListener(this);
		
		 
	}
	
	
	public void initGUI() {
		 frame.setSize(700, 300); 
		 frame.setLocationRelativeTo(null);
		 JScrollPane jsp = new JScrollPane(table); 
		 panel.setLayout(new BorderLayout()); 
		 panel.add(jsp,BorderLayout.CENTER); 
		 frame.setContentPane(panel);
		 frame.setVisible(true); 
		 table.setColumnSelectionAllowed(true);
		 table.setRowSelectionAllowed(true);
		 table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
	}
	
	
	@Override
	public void actionPerformed(ActionEvent e) {
		 if(e.getSource() == delete) {
			 int x = table.getSelectedRow();
			 int y = table.getSelectedColumn();
			 String z = (String) table.getValueAt(x, y);
			 int f = Integer.parseInt(z);
			 tietokanta.delete(f);
		 }
	}
}
			

