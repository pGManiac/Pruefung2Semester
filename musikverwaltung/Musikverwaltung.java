package Pruefung2Semester.musikverwaltung;

public class Musikverwaltung {
    private MusikverwaltungGUI view;

    public Musikverwaltung() {
        view = new MusikverwaltungGUI();
    }

    public static void main(String[] args) {
        Musikverwaltung musikverw = new Musikverwaltung();
        musikverw.view.setVisible(true);
    }
}
