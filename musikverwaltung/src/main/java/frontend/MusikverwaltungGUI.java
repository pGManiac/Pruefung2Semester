package frontend;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;

public class MusikverwaltungGUI extends JFrame {
    //ein Attribut das Objekte der Songklasse enthält, um darauf zugreifen zu können?

    private final int WIDTH = 100;
    private final int HEIGHT = 20;
    private JTextField eingabe;
    private JButton search; //eventuell unnötig, weil JTextField action listener hat
    private JMenuBar menueBar;
    private JMenu sortierung;
    private JMenuItem az;
    private JMenuItem za;
    private JMenu genres;
    private JMenuItem metal;
    private JMenuItem punk;
    private JMenuItem pop;
    private JMenuItem rock;
    private JMenuItem indie;
    private JMenuItem hiphop;
    private JButton add;
    private JButton remove;
    private JTable songsTable;
    private JScrollPane scroller1;
    private JFileChooser files;
    private JPanel top;
    

    public MusikverwaltungGUI() {
        this.setLayout(new BorderLayout());
        this.setTitle("Musikverwaltung");
        this.setState(Frame.NORMAL);
		//this.setSize(500, 500);
		this.setLocation(0, 0);
		this.getContentPane().setBackground(Color.white);

        top = new JPanel();

        //Suchfeld
        eingabe = new JTextField("suchen...", 110);
        eingabe.setBounds(20, 20, WIDTH, HEIGHT);
        top.add(eingabe);

        //Sortiermenue
        sortierung = new JMenu("Sortierung");
        az = new JMenuItem("A-Z", KeyEvent.VK_A);
        za = new JMenuItem("Z-A", KeyEvent.VK_Z);
        genres = new JMenu("Genre");
        metal = new JMenuItem("Metal", KeyEvent.VK_M);
        punk = new JMenuItem("Punk", KeyEvent.VK_P);
        pop = new JMenuItem("Pop", KeyEvent.VK_O);
        rock = new JMenuItem("Rock", KeyEvent.VK_R);
        indie = new JMenuItem("Indie", KeyEvent.VK_I);
        hiphop = new JMenuItem("Hip-Hop", KeyEvent.VK_H);
        genres.add(metal);
        genres.add(punk);
        genres.add(pop);
        genres.add(rock);
        genres.add(indie);
        genres.add(hiphop);
        sortierung.add(az);
        sortierung.add(za);
        sortierung.add(genres);
        menueBar = new JMenuBar();
        menueBar.setBounds(370, 20, WIDTH, HEIGHT);
        menueBar.add(sortierung);
        top.add(menueBar);

        //Loeschen
        remove = new JButton("Loeschen");
        remove.setBounds(250, 20, WIDTH, HEIGHT);
        top.add(remove);

        //Hinzufuegen
        add = new JButton("Hinzu");
        add.setBounds(130, 20, WIDTH, HEIGHT);
        top.add(add);

        this.add(top, BorderLayout.PAGE_START);

        //Songdarstellung
        String[] columns = {"Titel", "Interpret", "Genre"};
        String[][] rows = {{"Gagasmana", "Sellerie", "Metal"}, {"Hortensie", "Nonne", "Hip-Hop"}};
        songsTable = new JTable(rows, columns);
        scroller1 = new JScrollPane(songsTable);
        songsTable.setFillsViewportHeight(true);
        scroller1.setBounds(20, 100, 450, 300);
        this.add(scroller1, BorderLayout.CENTER);


    }


}
