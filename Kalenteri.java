import java.awt.event.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;
import java.awt.Dimension;
import java.awt.Event;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Point;
import java.util.Arrays;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Vector;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;

import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;

/*
 * Luokka kalenteri on vastuussa sovelluksen päänäkymän luomisesta ja kalenterin toimintalogiikasta
 */

public class Kalenteri extends JFrame implements ActionListener, MouseListener{
	
	//paneelit ja niihin sijoitettavat komponentit
	JPanel container = new JPanel();
	JPanel subcontainer = new JPanel(); 
	JPanel bottom = new JPanel(); 
	JPanel yearContainer = new JPanel();
	JPanel kalenteripaneeli = new JPanel();
	DefaultTableModel kalenterimalli = new DefaultTableModel(){
		public boolean isCellEditable(int rowIndex, int mColIndex){
			return false;
			}
		};
		
	JTable kalenteritaulukko = new JTable(kalenterimalli); 
	JScrollPane kalenteriskrolli = new JScrollPane(kalenteritaulukko);
	
	//otsikot
	JLabel paivalbl = new JLabel("Päivä");
	JLabel kuukausilbl = new JLabel("Kuukausi");
	JLabel otsikko = new JLabel("B U J O G O E S D I G I T A L");
	JLabel vuosilbl = new JLabel("Select year ");
	
	//nappulat
	JButton seuraava = new JButton();
	JButton edellinen = new JButton();
	JButton lisaa = new JButton();
	JButton poista = new JButton();
	JButton info = new JButton(); 
	JComboBox box1 = new JComboBox();
	JComboBox box2 = new JComboBox();
	JComboBox box3 = new JComboBox();
	JComboBox box4 = new JComboBox();
	JComboBox box5 = new JComboBox();
	
	//fontit otsikoita varten
	Font font1 = new Font("Courier", Font.PLAIN, 30);
	Font font2 = new Font("Lucida Sans Typewriter", Font.PLAIN, 16);
	Font font3 = new Font("Courier", Font.PLAIN, 42);
	
	SQlite tietokanta = new SQlite(); 
	GregorianCalendar kalenteri = new GregorianCalendar();
	
	
	String[] months = {"JANUARY", "FEBRUARY", "MARCH", "APRIL", "MAY", "JUNE", "JULY", "AUGUST", "SEPTEMBER", "OCTOBER", "NOVEMBER", "DECEMBER"};
	String[] numberMonths = {"1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12"};
	String[] headers = {"mon", "tue", "wed", "thu", "fri", "sat", "sun"};
	
	int day;
	private int month;
	int year;
	int currentYear;
	int currentMonth;
	int daysinmonth;
	int noOfDays;
	int startOfMonth;

	
	//konstruktori alustaa kalenterin
	public Kalenteri(){
		
		//luodaan graafinen käyttöliittymä
		initGUI();
		this.tietokanta = tietokanta;
		
		
		day = kalenteri.get(GregorianCalendar.DAY_OF_MONTH);
		setMonth(kalenteri.get(GregorianCalendar.MONTH));
		year = kalenteri.get(GregorianCalendar.YEAR);
		currentMonth = getMonth(); 
		currentYear = year; 
		
		//lisätään kalenteritaulukkoon viikonpäivien nimet
		for (int i=0; i<7; i++){
			kalenterimalli.addColumn(headers[i]); 
			}
		
		kalenteritaulukko.getTableHeader().setReorderingAllowed(false);
		kalenteritaulukko.getTableHeader().setFont(font2);
		kalenteritaulukko.setColumnSelectionAllowed(true);
		kalenteritaulukko.setRowSelectionAllowed(true);
		kalenteritaulukko.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		kalenteritaulukko.getParent().setBackground(kalenteritaulukko.getBackground());	
		kalenteritaulukko.setRowHeight(80);
		kalenterimalli.setColumnCount(7);
		kalenterimalli.setRowCount(6);
		
		
		//lisätään valintaboxiin vuodet
		for (int i=currentYear-100; i<=currentYear+100; i++){
		box1.addItem(String.valueOf(i));
		
		//päivitetään kalenteri näyttämään meneillään oleva kuukausi ja vuosi
		refreshCalendar (currentMonth, currentYear);

	//	kalenteritaulukko.setDefaultRenderer(kalenteritaulukko.getColumnClass(0), new TableRenderer()); 

		}
	}
	

	public int getMonth() {
		return month;
	}

	public void setMonth(int month) {
		this.month = month;
	}	
	
	// metodi kalenterin päivittämistä varten
	public void refreshCalendar (int month, int year) {
		//Selvitetään valitussa kuukaudessa olevien päivien määrä ja ensimmäisen päivän viikonpäivä
		GregorianCalendar calendar = new GregorianCalendar(year, month, 0);
		noOfDays = calendar.getActualMaximum(GregorianCalendar.DAY_OF_MONTH);
		startOfMonth = calendar.get(GregorianCalendar.DAY_OF_WEEK);
		daysinmonth = noOfDays; 

		seuraava.setEnabled(true);
		edellinen.setEnabled(true);
		
		//estetään liikkuminen pitkälle menneisyyteen tai tulevaisuuteen
		if(month == 0 && year <= currentYear-100) {
			edellinen.setEnabled(false);
		}
		if(month == 11 && year >= currentYear+100) {
			seuraava.setEnabled(false);
		}
		
		//asetetaan otsikoksi meneillään olevan kuukauden nimi ja valintaboxiin meneillään oleva vuosi
		kuukausilbl.setText(months[month]); 
		box1.setSelectedItem(String.valueOf(year));
		
		
		//muotoillaan kalenteritaulukko ja asetetaan päivien numerot
		for(int i=0; i<6; i++) {
			for(int j=0; j<7; j++) {
				kalenterimalli.setValueAt(null, i, j);
				
			}
		}

		for(int i=1; i<=noOfDays; i++) {
			int row = new Integer((i+startOfMonth-2)/7);
			int column = (i+startOfMonth-2)%7;
		  
			kalenterimalli.setValueAt(i, row, column);
			
		}	
	}


	//metodi käsittelee hiiren klikkaamisesta tapahtuvan toiminnallisuuden
	public void mouseClicked(MouseEvent e) {
		
		//käyttäjä painaa lisäys-nappia
		if(e.getSource() == lisaa) {
			Object[] options = {"Event", "Task"};
            int i = JOptionPane.showOptionDialog(null, "What would you like to add?", "Add", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE, null, options, options[0]);
            
            //käyttäjä valitsee vaihtoehdon "Event" 
            if (i == JOptionPane.YES_OPTION) {
            	
            	//muuttujat käyttäjän syöttämän tiedon tallentamista varten
            	JTextField eventName = new JTextField();
            	JTextField eventLocation = new JTextField();
            	JTextField eventDay = new JTextField();
            	JTextField eventStart = new JTextField();
            	JTextField eventEnd = new JTextField();

            	//asetetaan valintaboxeihin kuukaudet ja vuodet
        		for (int j=0; j< months.length; j++){
        			box2.addItem(String.valueOf(numberMonths[j]));		
        		}
        		
        		for (int s=currentYear-100; s<= currentYear+100; s++){
        			box3.addItem(String.valueOf(s));	
        		}
        		
        		//aseteaan päivämäärän valintaa varten oletukseksi sen hetkinen päivämäärä
        		eventDay.setText((new Integer(kalenteri.get(GregorianCalendar.DAY_OF_MONTH))).toString());
        		box2.setSelectedIndex(getMonth());
        		box3.setSelectedItem(String.valueOf(year));
        	
            	//käyttäjälle näkyvä viesti
            	Object[] message1 = {
            	    "Title:", eventName,
            	    "Day:", eventDay,
            	    "Month:",  box2, 
            	    "Year:", box3,
            	    "Location:", eventLocation,
            	    "Start time", eventStart,
            	    "End time:", eventEnd, 
            	};
            	
         
            	int option1 = JOptionPane.showConfirmDialog(null, message1, "Add event", JOptionPane.OK_CANCEL_OPTION);
            		
            		//kun käyttäjä painaa OK, luodaan uusi tapahtuma
	            	if (option1 == JOptionPane.OK_OPTION) {
	            		
	            		String z = eventDay.getText();
	            		String d = box2.getSelectedItem().toString();
	            		String f = box3.getSelectedItem().toString();
	            		String x = eventName.getText(); 
	                	String y = eventLocation.getText(); 
	                	String a = eventStart.getText();
	                	String b = eventEnd.getText();
						this.addTapahtuma(z, d, f, x, y, a, b);
						
						
	                 }
            }
            
         //käyttäjä valitsee vaihtoehdon "Task"
         else if (i == JOptionPane.NO_OPTION) {
            	JTextField taskName = new JTextField();
            	JTextField taskDay = new JTextField();
           
                	Object[] message2 = {
                	    "Title:", taskName, 
                	    "Day:", taskDay,
                	    "Month:", box4,
                	    "Year:", box5,
                	    
                };
                	
                for (int j=0; j< months.length; j++){
            		box4.addItem(String.valueOf(numberMonths[j]));		
            	}
            		
            	for (int s=currentYear-100; s<= currentYear+100; s++){
            			box5.addItem(String.valueOf(s));	
            	}
            		
            	taskDay.setText((new Integer(kalenteri.get(GregorianCalendar.DAY_OF_MONTH))).toString());
            	box4.setSelectedIndex(getMonth());
            	box5.setSelectedItem(String.valueOf(year));
            	
            	//kun käyttäjä painaa OK, luodaan uusi tehtävä
                int option2 = JOptionPane.showConfirmDialog(null, message2, "Add task", JOptionPane.OK_CANCEL_OPTION);
                	if (option2 == JOptionPane.OK_OPTION) {
                		String x1 = taskDay.getText();
	            		String x2 = taskName.getText(); 
	            		String x3 = box4.getSelectedItem().toString();
	            		String x4 = box5.getSelectedItem().toString();
	            		this.addTehtava(x2, x1, x3, x4);
 
            	}
            }
		}
		
		
		//poista-painikkeen klikkaamisesta seuraava toiminnallisuus
		else if(e.getSource() == poista) {
			Nakyma kaikki = new Nakyma(); 
			kaikki.setLocationRelativeTo(null);

			
		}
		
		else if(e.getSource() == info) {
			JOptionPane.showMessageDialog(null, "Welcome to your digital calendar! \n \n You can add events and tasks. \n Press  + to add  \n You can view your events and tasks for certain day by clicking the day in calendar \n Press - to delete \n Select the id of the event or task you want to delete and press delete " );
			
		}
		
		
		else if(e.getSource() == kalenteritaulukko) {
			int column = kalenteritaulukko.getSelectedColumn();
			int row = kalenteritaulukko.getSelectedRow();
			int day = (int) kalenterimalli.getValueAt(row, column);
			String vuosi = box1.getSelectedItem().toString();
			int vvuosi = Integer.parseInt(vuosi); 
			String kuukausi = kuukausilbl.getText();
			int kkuukausi = Arrays.asList(months).indexOf(kuukausi) + 1;
			
			System.out.println(day);
			System.out.println(vvuosi);
			System.out.println(kkuukausi);
			
			Nakyma paiva = new Nakyma(day, kkuukausi, vvuosi); 
			paiva.setLocationRelativeTo(null);
			
			
				
		}
		
	}
	
	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
	}
	
	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub	
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub	
	}


	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub	
	}
	
	
	//metodi käsittelee nappuloiden edellinen ja seuraava klikkaamisen käsittelyn sekä comboboxin toiminnallisuuden käsittelyn
	@Override
	public void actionPerformed(ActionEvent e) {
		//seuraava-nappulan kuuntelija 
		if(e.getSource() == seuraava) {
			if(currentMonth == 11) {
				currentMonth = 0; 
				currentYear += 1; 
			}
			else {
				currentMonth += 1; 
			}
			refreshCalendar(currentMonth, currentYear); 
			
		}
		//edellinen-nappulan kuuntelija
		else if(e.getSource() == edellinen) {
			if(currentMonth == 0) {
				currentMonth = 11; 
				currentYear -= 1; 
			}
			else {
				currentMonth -= 1; 
			}
			refreshCalendar(currentMonth, currentYear); 
			
		}
		
		//comboboxin kuuntelija
		else if(box1.getSelectedItem() != null) {
			String x = box1.getSelectedItem().toString();
			year= Integer.parseInt(x);
			refreshCalendar(currentMonth, year);
		}
		
		
	}
	
	//metodi tapahtumien lisäämiseksi
	public void addTapahtuma(String paiva, String kuukausi, String vuosi, String nimi, String sijainti, String aloitusaika, String lopetusaika) {
		Tapahtuma tapahtuma = new Tapahtuma();
		int pvInt = Integer.parseInt(paiva);
		int kkInt = Integer.parseInt(kuukausi);
		int vuosiInt = Integer.parseInt(vuosi);
		int aloitusInt = Integer.parseInt(aloitusaika);
		int lopetusInt = Integer.parseInt(lopetusaika);
		
		tapahtuma.setPv(pvInt);
		tapahtuma.setKk(kkInt);
		tapahtuma.setVuosi(vuosiInt);
		tapahtuma.setNimi(nimi);
		tapahtuma.setSijainti(sijainti);
		tapahtuma.setAloitusaika(aloitusInt);
		tapahtuma.setLopetusaika(lopetusInt);
		tapahtuma.lisaa();

		
	}
	
	//metodi tehtävien lisäämiseksi
	public void addTehtava(String nimi, String paiva, String kuukausi, String vuosi) {
		Tehtava tehtava = new Tehtava();
		int pvInt = Integer.parseInt(paiva);
		int kkInt = Integer.parseInt(kuukausi);
		int vuosiInt = Integer.parseInt(vuosi);
		
			tehtava.setNimi(nimi);
			tehtava.setPv(pvInt);
			tehtava.setKk(kkInt);
			tehtava.setVuosi(vuosiInt); 
			tehtava.lisaa();
			
	}
	
	
	public void initGUI(){
		setSize(1000, 800);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setResizable(false); 
		
		//asetetaan paneelien värit
		bottom.setBackground(Color.WHITE);
		kalenteritaulukko.setBackground(new Color(192, 225, 232));
		kalenteripaneeli.setBackground(Color.WHITE);
		kalenteriskrolli.setBackground(Color.WHITE);
		container.setBackground(Color.WHITE);
		yearContainer.setBackground(Color.WHITE);
		
		//asetetaan paneelien layoutit
		container.setLayout(new BoxLayout(container, BoxLayout.LINE_AXIS));
		subcontainer.setLayout(new BoxLayout(subcontainer, BoxLayout.Y_AXIS));
		kalenteripaneeli.setLayout(new BoxLayout(kalenteripaneeli, BoxLayout.Y_AXIS));
		bottom.setLayout(new GridLayout());
		yearContainer.setLayout(new BoxLayout(yearContainer, BoxLayout.X_AXIS));
		
		//muutetaan paneelien kokoa
		bottom.setPreferredSize(new Dimension(1000, 30));
		kalenteripaneeli.setPreferredSize(new Dimension(1000,500));
		yearContainer.setPreferredSize(new Dimension(800, 100));

		
		Border border = new EmptyBorder(10,10,10,0);
	    Border margin = new EmptyBorder(10,10,10,0);
	    Border margin2 = new EmptyBorder(0,0,0,0);
	    Border border1 = new EmptyBorder(10,345,10, 0);
	    Border border2 = new EmptyBorder(10,5 ,2, 5);
	    edellinen.setBorder(new CompoundBorder(border,margin));
	    lisaa.setBorder(new CompoundBorder(border,margin));
	    poista.setBorder(new CompoundBorder(border,margin));
	    info.setBorder(new CompoundBorder(border,margin));
	    seuraava.setBorder(new CompoundBorder(border,margin));
	    otsikko.setBorder(new CompoundBorder(border,margin));
	    bottom.setBorder(new CompoundBorder(border2,margin2));
	    vuosilbl.setBorder(new CompoundBorder(border,margin));
	    kuukausilbl.setBorder(new CompoundBorder(border1,margin));
	   // kalenteriskrolli.setBorder(new CompoundBorder(border, margin));
	    
		//luodaan kuvakkeet nappuloita varten
		edellinen.setIcon(new ImageIcon(this.getClass().getResource("back-2.png")));
		seuraava.setIcon(new ImageIcon(this.getClass().getResource("next-2.png")));
		lisaa.setIcon(new ImageIcon(this.getClass().getResource("add.png")));
		poista.setIcon(new ImageIcon(this.getClass().getResource("minus.png")));
		info.setIcon(new ImageIcon(this.getClass().getResource("question.png")));
		
		
		//lisätään komponentit paneeleihin
		bottom.add(edellinen);
		bottom.add(lisaa);
		bottom.add(info);
		bottom.add(poista); 
		bottom.add(seuraava);
		yearContainer.add(vuosilbl); 
		yearContainer.add(box1);
		kalenteripaneeli.add(otsikko);
		kalenteripaneeli.add(kuukausilbl);
		kalenteripaneeli.add(yearContainer);
		kalenteripaneeli.add(kalenteriskrolli);
		subcontainer.add(kalenteripaneeli);
		subcontainer.add(bottom);
		container.add(subcontainer);
		
		//asetetaan fontit
		kuukausilbl.setFont(font1);
		otsikko.setFont(font3);
		vuosilbl.setFont(font2); 
		box1.setFont(font2);
		
		//lisätään actionlistenerit ja mouselistenerit
		edellinen.addActionListener(this);
		seuraava.addActionListener(this);
		box1.addActionListener(this);
		lisaa.addActionListener(this);
		lisaa.addMouseListener(this);
		poista.addMouseListener(this);
		info.addMouseListener(this);
		kalenteritaulukko.addMouseListener(this);

		//lisätään kaikki elementit ikkunaan
		add(container);
		setVisible(true);
		
	}
}
	
	

