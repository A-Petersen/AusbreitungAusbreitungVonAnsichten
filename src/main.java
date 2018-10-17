public class main {
    public static void main(String [ ] args) {

        int anzTage = 100;
        int anzPersonen = 50;
        int meinungsvertreter = 3;
        double wahrscheinlichkeitBegegnung = 0.5;
        double wahrscheinlichkeitMeinungsbildung = 0.5;

        // Tagesablauf mit abhängiger Meinungsbildung
        Tagesablauf tag_1 = new Tagesablauf(anzPersonen, meinungsvertreter);
        for (int i = 0; i < anzTage; i++) {
            tag_1.simTagAbhaengigeMeinung(wahrscheinlichkeitBegegnung);
        }
        zwischenErgebnis(tag_1, "abhängiger");

        System.out.println("Test der ges. Meinungen A: " +
                tag_1.getPersonen().stream().filter(person -> person.getMeinungA()).count() + "\n");

        // Tagesablauf mit unabhängiger Meinungsbildung
        Tagesablauf tag_2 = new Tagesablauf(anzPersonen, meinungsvertreter);
        for (int i = 0; i < anzTage; i++) {
            tag_2.simTagUnabhaengigeMeinung(wahrscheinlichkeitMeinungsbildung);
        }
        zwischenErgebnis(tag_2, "unabhängiger");

        System.out.println("Test der ges. Meinungen A: " +
                tag_2.getPersonen().stream().filter(person -> person.getMeinungA()).count() + "\n");
    }

    private static void zwischenErgebnis(Tagesablauf tag, String s)
    {
        System.out.println( "Tagesablauf mit " + s + " Meinungsbildung\n" +
                "Meinungsverteilung in %:\t" + tag.meinungsVerteilung() +
                " (" +  tag.getAnzMeinungA() + "/50)\n" +
                tag.ausgabeMeinungsverteilung()
        );
    }
}
