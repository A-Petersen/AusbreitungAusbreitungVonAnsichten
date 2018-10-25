import com.opencsv.CSVWriter;
import tech.tablesaw.api.NumberColumn;
import tech.tablesaw.api.Table;
import tech.tablesaw.plotly.Plot;
import tech.tablesaw.plotly.api.LinePlot;
import static tech.tablesaw.aggregate.AggregateFunctions.*;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

public class Untersuchung {
    /**
     * Anzahl der durchzuführenden Tage
      */
    private int anzTage;
    /**
     *  Anzahl der beteiligten Personen
     */
    private int anzPersonen;
    /**
     * Anzahl der Meinungsvertreter A bei start
     */
    private int meinungsvertreter;
    /**
     * Wahrscheinlichkeit der Begegnungen zwischen den Personen
     */
    private double wahrscheinlichkeitBegegnung;
    /**
     * Wahrscheinlichkeit der unabhängigen Meinungsbildung
     */
    private double wahrscheinlichkeitMeinungsbildung;
    /**
     * Anzahl der Testdurchläufe
     */
    private int anzDurchlaufe;
    /**
     * Benötigte Anzahl von Treffen für abhängige Meinungsbildung
     */
    private int anzBenoetigterTreffen;
    private int dauerEmpfaenglichkeit;

    /**
     * Spezifizieren der durchzuführenden Untersuchung
     * @param t Anzahl der Tage
     * @param p Anzahl der Personen
     * @param m Anzahl der Meinungsvertreter
     * @param wB Wahrscheinlichkeit einer Begegnung
     * @param wMB Wahrscheinlichkeit persönlicher Meinungsbildung
     * @param aD Anzahl der Durchläufe
     * @param aBT Anzahl nötiger Treffen zur Überzeugung
     * @param dE Dauer der Empfänglichkeit
     */
    Untersuchung(int anzTage, int anzPersonen, int meinungsvertreter, double wahrscheinlichkeitBegegnung, double wahrscheinlichkeitMeinungsbildung, int anzDurchlaufe, int anzBenoetigterTreffen, int dauerEmpfaenglichkeit)
    {
        this.anzTage = anzTage;
        this.anzPersonen = anzPersonen;
        this.meinungsvertreter = meinungsvertreter;
        this.wahrscheinlichkeitBegegnung = wahrscheinlichkeitBegegnung;
        this.wahrscheinlichkeitMeinungsbildung = wahrscheinlichkeitMeinungsbildung;
        this.anzDurchlaufe = anzDurchlaufe;
        this.anzBenoetigterTreffen = anzBenoetigterTreffen;
        this.dauerEmpfaenglichkeit = dauerEmpfaenglichkeit;
    }

    /**
     * Startet die spezifizierte Untersuchung
     * @throws IOException
     */
    public void start() throws IOException {

        List<List<String[]>> u1 = new LinkedList<>();
        for (int i = 0; i < anzDurchlaufe; i++)
        {
            List<String[]> x = untersuchung(true, i);
            u1.add(x);
        }
        for (int i = 0; i < anzDurchlaufe; i++)
        {
            List<String[]> x = untersuchung(false, i);
            u1.add(x);
        }

        File file = new File("DatenReihen.csv");

        FileWriter outputfile = new FileWriter(file);
        CSVWriter writer = new CSVWriter(outputfile);
        String[] kopf = { "Tag", "Prozent", "TestReihe" };
        writer.writeNext(kopf);

        u1.forEach(l -> l.forEach(s -> writer.writeNext(s)));

        writer.close();



        Table table = Table.read().csv("DatenReihen.csv");

        System.out.printf(table.toString());
        NumberColumn testReihe = table.nCol("Prozent");
        Table table1 = table.summarize(testReihe, mean).by("Tag", "TestReihe");
//        table1.first(3);
//        table1.last(4);
        System.out.printf(table1.toString());
        Plot.show(LinePlot.create("Meinung",
                table1, "Tag", "Mean [Prozent]", "TestReihe"));
    }

    /**
     *
     * @param ablaufart true für abhängige, false für unabhängige Meinung
     */
    private List<String[]> untersuchung(boolean ablaufart, int nr)
    {
        Tagesablauf ablauf = new Tagesablauf(anzPersonen, meinungsvertreter, anzBenoetigterTreffen, dauerEmpfaenglichkeit);
        List<String[]> csvDataList = new LinkedList<String[]>();
        for (int tag = 0; tag < anzTage; tag++)
        {
            if (ablaufart) {
                ablauf.simTagAbhaengigeMeinung(wahrscheinlichkeitBegegnung);
            }
            else
            {
                ablauf.simTagUnabhaengigeMeinung(wahrscheinlichkeitMeinungsbildung);
            }
            String[] csvData_x = {  tag + "",
                                    ablauf.meinungsVerteilung() + "",
                                    ablaufart ? "abhaengigeM" :"unabhaengigeM",
//                    ablaufart ? "AbhaengigeMeinungsbildung_" + nr :"UnabhaengigeMeinungsbildung_" + nr,
//                                    "x" //TODO: vll nötig
                                    };
            csvDataList.add(csvData_x);
        }
        zwischenErgebnis(ablauf, ablaufart ? "abhängiger":"unabhängiger");
        return csvDataList;
    }

    private void zwischenErgebnis(Tagesablauf tag, String s)
    {
        System.out.println( "\nTagesablauf mit " + s + " Meinungsbildung\n" +
                "Meinungsverteilung in %:\t" + tag.meinungsVerteilung() +
                " (" +  tag.getAnzMeinungA() + "/" + anzPersonen + ")\n" +
                tag.ausgabeMeinungsverteilung()
        );
    }
}
