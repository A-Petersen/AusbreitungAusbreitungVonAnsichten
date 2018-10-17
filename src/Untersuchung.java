import java.util.LinkedList;
import java.util.List;
import java.util.Map;

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
        System.out.println(untersuchung(true));
        untersuchung(false);
    }

    /**
     *
     * @param ablaufart true für abhängige, false für unabhängige Meinung
     */
    private List<Double> untersuchung(boolean ablaufart)
    {
        Tagesablauf ablauf = new Tagesablauf(anzPersonen, meinungsvertreter);
        List<Double> pTag = new LinkedList<Double>();
        for (int tag = 0; tag < anzTage; tag++)
        {
            if (ablaufart) {
                ablauf.simTagAbhaengigeMeinung(wahrscheinlichkeitBegegnung);
            }
            else
            {
                ablauf.simTagUnabhaengigeMeinung(wahrscheinlichkeitMeinungsbildung);
            }
            pTag.add(ablauf.meinungsVerteilung());
//            if (tag % 50 == 0) zwischenErgebnis(ablauf, ablaufart ? "abhängiger":"unabhängiger");
        }
        zwischenErgebnis(ablauf, ablaufart ? "abhängiger":"unabhängiger");
        return pTag;
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
