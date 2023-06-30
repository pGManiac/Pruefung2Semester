package frontend;

public class Musikverwaltung {
    private MusikverwaltungGUI view;

    public Musikverwaltung() {
        view = new MusikverwaltungGUI();
    }

    public static void main(String[] args) {
        Musikverwaltung musikverwaltung = new Musikverwaltung();
        musikverwaltung.view.setVisible(true);
    }
}
