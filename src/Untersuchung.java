public class Untersuchung {
    int anzTage;
    int anzPersonen;
    int meinungsvertreter;
    double wahrscheinlichkeitBegegnung;
    double wahrscheinlichkeitMeinungsbildung;

    Untersuchung(int t, int p, int m, double wB, double wMB)
    {
        anzTage = t;
        anzPersonen = p;
        meinungsvertreter = m;
        wahrscheinlichkeitBegegnung = wB;
        wahrscheinlichkeitMeinungsbildung = wMB;
    }

    public void start()
    {
        untersuchung(true);
        untersuchung(false);
    }

    /**
     *
     * @param ablaufart true für abhängige, false für unabhängige Meinung
     */
    private void untersuchung(boolean ablaufart)
    {
        Tagesablauf ablauf = new Tagesablauf(anzPersonen, meinungsvertreter);
        for (int tag = 0; tag < anzTage; tag++)
        {
            if (ablaufart) {
                ablauf.simTagAbhaengigeMeinung(wahrscheinlichkeitBegegnung);
            }
            else
            {
                ablauf.simTagUnabhaengigeMeinung(wahrscheinlichkeitMeinungsbildung);
            }
//            if (tag % 50 == 0) zwischenErgebnis(ablauf, ablaufart ? "abhängiger":"unabhängiger");
        }
        zwischenErgebnis(ablauf, ablaufart ? "abhängiger":"unabhängiger");
    }

    private void zwischenErgebnis(Tagesablauf tag, String s)
    {
        System.out.println( "\nTagesablauf mit " + s + " Meinungsbildung\n" +
                "Meinungsverteilung in %:\t" + tag.meinungsVerteilung() +
                " (" +  tag.getAnzMeinungA() + "/50)\n" +
                tag.ausgabeMeinungsverteilung()
        );
    }
}
