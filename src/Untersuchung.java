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

/**
 * Klasse Untersuchung
 */
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
    private double pBegegnung;
    /**
     * Wahrscheinlichkeit der unabhängigen Meinungsbildung
     */
    private double pMeinungsbildung;
    /**
     * Anzahl der Testdurchläufe
     */
    private int anzDurchlaufe;
    /**
     * Benötigte Anzahl von Treffen für abhängige Meinungsbildung
     */
    private int anzBenoetigterTreffen;
    /**
     * Anzahl der Tage bis die Empfänglichkeit erlischt
     */
    private int dauerEmpfaenglichkeit;

    /**
     * Spezifizieren der durchzuführenden Untersuchung
     * @param anzTage Anzahl der Tage
     * @param anzPersonen Anzahl der Personen
     * @param meinungsvertreter Anzahl der Meinungsvertreter
     * @param pBegegnung Wahrscheinlichkeit einer Begegnung
     * @param pMeinungsbildung Wahrscheinlichkeit persönlicher Meinungsbildung
     * @param anzDurchlaufe Anzahl der Durchläufe
     * @param anzBenoetigterTreffen Anzahl nötiger Treffen zur Überzeugung
     * @param dauerEmpfaenglichkeit Dauer der Empfänglichkeit
     */
    Untersuchung(int anzTage, int anzPersonen, int meinungsvertreter, double pBegegnung, double pMeinungsbildung, int anzDurchlaufe, int anzBenoetigterTreffen, int dauerEmpfaenglichkeit)
    {
        this.anzTage = anzTage;
        this.anzPersonen = anzPersonen;
        this.meinungsvertreter = meinungsvertreter;
        this.pBegegnung = pBegegnung;
        this.pMeinungsbildung = pMeinungsbildung;
        this.anzDurchlaufe = anzDurchlaufe;
        this.anzBenoetigterTreffen = anzBenoetigterTreffen;
        this.dauerEmpfaenglichkeit = dauerEmpfaenglichkeit;
    }

    /**
     * Startet die spezifizierte Untersuchung
     * @throws IOException
     */
    public void start() throws IOException {
        erstelleDaten("DatenReihen_abhaengig.csv", true);
        erstelleDaten("DatenReihen_unabhaengig.csv", false);
    }

    /**
     * Führt eine Untersuchung/Durchlauf durch und erstellt eine Liste mit StringArrays.
     * Diese enthalten die Daten der Untersuchung, zugeschnitten für die Erstellung einer
     * CSV Datei.
     * @param ablaufart true für abhängige, false für unabhängige Meinung
     */
    private List<String[]> untersuchung(boolean ablaufart)
    {
        Tagesablauf ablauf = new Tagesablauf(anzPersonen, meinungsvertreter, anzBenoetigterTreffen, dauerEmpfaenglichkeit);
        List<String[]> csvDataList = new LinkedList<String[]>();
        for (int tag = 1; tag <= anzTage; tag++)
        {
            if (ablaufart) {
                ablauf.simTagAbhaengigeMeinung(pBegegnung);
            }
            else
            {
                ablauf.simTagUnabhaengigeMeinung(pMeinungsbildung);
            }
            String[] csvData_x = {  tag + "",
                                    ablauf.meinungsVerteilung() + "",
                                    ablaufart ? "abhängige Meinungsbildung" :"unabhängige Meinungsbildung"
                                    };
            csvDataList.add(csvData_x);
        }
        zwischenErgebnis(ablauf, ablaufart ? "abhängiger":"unabhängiger");
        return csvDataList;
    }

    /**
     * Gibt ein Zwischenergebnis für einen Durchlauf auf dem Terminal aus.
     * Beispielausgabe:
     * Tagesablauf mit abhängiger Meinungsbildung
     * Meinungsverteilung in %:	96.0 (48/50)
     * >> ||||||||||||||||||||||||||||||||||-||||||||||||-|| <<
     * @param tag Benötigt die Instanz des Tagesablauf
     * @param s Bennenung der Durchlaufart (unabhängig oder abhängig)
     */
    private void zwischenErgebnis(Tagesablauf tag, String s)
    {
        System.out.println( "\nTagesablauf mit " + s + " Meinungsbildung\n" +
                "Meinungsverteilung in %:\t" + tag.meinungsVerteilung() +
                " (" +  tag.getAnzMeinungA() + "/" + anzPersonen + ")\n" +
                tag.ausgabeMeinungsverteilung()
        );
    }

    /**
     * Führt die Untersuchung durch und erstellt die CSV Datei für eine der spezifizierten
     * Untersuchungsarten durch.
     * (True für abhängige, False für unabhängige Meinungsbildung)
     * @param dateiName Gewünschter Dateiname der CSV Datei
     * @param untersuchungsart  True für abhängige, False für unabhängige Meinungsbildung
     * @throws IOException
     */
    private void erstelleDaten(String dateiName, boolean untersuchungsart) throws IOException {
        List<List<String[]>> list = new LinkedList<>();
        File file = new File(dateiName);
        FileWriter output = new FileWriter(file);
        CSVWriter writer = new CSVWriter(output);
        for (int i = 0; i < anzDurchlaufe; i++)
        {
            List<String[]> x = untersuchung(untersuchungsart);
            list.add(x);
        }
        String[] kopf = { "Tag", "Prozent", "TestReihe" };
        writer.writeNext(kopf);
        list.forEach(l -> l.forEach(s -> writer.writeNext(s)));
        writer.close();

        Table table = Table.read().csv(dateiName);
        NumberColumn testReihe = table.nCol("Prozent");
        Table table1 = table.summarize(testReihe, mean).by("Tag", "TestReihe");
        System.out.printf(table1.toString());
        Plot.show(LinePlot.create("Meinung",
                table1, "Tag", "Mean [Prozent]", "TestReihe"));
    }
}
