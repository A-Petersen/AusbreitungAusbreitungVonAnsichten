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
     * Summe der Tage für 100% pro Durchlauf
     */
    private double sumTage;
    /**
     * Debugausgabe für das Terminal setzen
     */
    private boolean verbose = false;

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
     * @param verbose Debugausgaben im Terminal aktivieren/deaktivieren
     */
    Untersuchung(int anzTage, int anzPersonen, int meinungsvertreter, double pBegegnung, double pMeinungsbildung, int anzDurchlaufe, int anzBenoetigterTreffen, int dauerEmpfaenglichkeit, boolean verbose)
    {
        this.anzTage = anzTage;
        this.anzPersonen = anzPersonen;
        this.meinungsvertreter = meinungsvertreter;
        this.pBegegnung = pBegegnung;
        this.pMeinungsbildung = pMeinungsbildung;
        this.anzDurchlaufe = anzDurchlaufe;
        this.anzBenoetigterTreffen = anzBenoetigterTreffen;
        this.dauerEmpfaenglichkeit = dauerEmpfaenglichkeit;
        this.verbose = verbose;
    }

    /**
     * Startet die spezifizierte Untersuchung
     * @throws IOException
     */
    public void start() throws IOException {
        erstelleDaten("DatenReihen_abhaengig.csv", true);
        if (verbose) System.out.println("\nDurchnitt nötiger Tage für 100% (abhaengig): " +
                            getAndResetSchnitt() + "\tBei " + pBegegnung*100.0 + "%");
        erstelleDaten("DatenReihen_unabhaengig.csv", false);
        if (verbose) System.out.println("\nDurchnitt nötiger Tage für 100% (unabhaengig): " +
                            getAndResetSchnitt() + "\tBei " + pMeinungsbildung*100.0 + "%");
    }

    /**
     * Führt eine Untersuchung/Durchlauf durch und erstellt eine Liste mit StringArrays.
     * Diese enthalten die Daten der Untersuchung, zugeschnitten für die Erstellung einer
     * CSV Datei.
     * @param ablaufart true für abhängige, false für unabhängige Meinung
     */
    private List<String[]> untersuchung(boolean ablaufart)
    {
        // Erstellen des Tagesablaufs
        Tagesablauf ablauf = new Tagesablauf(anzPersonen, meinungsvertreter, anzBenoetigterTreffen, dauerEmpfaenglichkeit);
        List<String[]> csvDataList = new LinkedList<String[]>();   // Liste mit StringArray für die Rückgabe der Methode (nötig für Erstellung von CSV-Datei)
        boolean maxErreicht = false;                    // Bool um durchgeführten Schritt für die Durchschnittsbildung (Tage bis 100% MeinungA) zu kennzeichnen
        for (int tag = 1; tag <= anzTage; tag++)            // Durchführen der Tage
        {
            if (ablaufart && !maxErreicht) {    // Abfrage nach Ablaufart (True abhängige, False unabhängige) und ob bereits 100% Meinungsvertreter erreicht
                ablauf.simTagAbhaengigeMeinung(pBegegnung);
            }
            else if (!ablaufart && !maxErreicht)
            {
                ablauf.simTagUnabhaengigeMeinung(pMeinungsbildung);
            }
            // Eintragen der relevanten Daten in ein Array und hinzufügen in die Liste
            String[] csvData_x = {  tag + "",
                                    ablauf.meinungsVerteilung() + "",
                                    ablaufart ? "abhängige Meinungsbildung" :"unabhängige Meinungsbildung"
                                    };
            csvDataList.add(csvData_x);
            // Abfrage Tag erreicht 100% Meinungsvertreter A und Schritt für die Durchschnittsbildung hat nocht nicht stattgefunden
            if (ablauf.meinungsVerteilung() >= 100 && !maxErreicht)
            {
                sumTage = sumTage + tag;    // Aufsummieren der nötigen Tage für 100%
                maxErreicht = true;     // Bool setzen
            }
        }
        if (verbose) zwischenErgebnis(ablauf, ablaufart ? "abhängiger":"unabhängiger"); // Terminal Ausgabe
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
                tag.ausgabeMeinungsverteilung() + "\n"
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
        List<List<String[]>> list = new LinkedList<>();             // Liste für Listen von Durchläufen
        File file = new File(dateiName);
        FileWriter output = new FileWriter(file);
        CSVWriter writer = new CSVWriter(output);
        for (int i = 0; i < anzDurchlaufe; i++)                     // Führt die gewünschte Anzahl an Durchläufen durch
        {
            List<String[]> x = untersuchung(untersuchungsart);      // Übergibt die gefüllte Liste mit Daten des Durchlaufs
            list.add(x);                                            // Hängt die erstellte Liste an die Liste mit allen Durchläufen
        }
        String[] kopf = { "Tag", "Prozent", "TestReihe" };          // Array für den CSV Kopf
        writer.writeNext(kopf);
        // Schreibt jedes Array Element aus den Listen der Durchläufe, welche aus der gesamt Liste aller Durchläufe kommmen, den CSV Eintrag
        // List<List<String[]>> list, wird hier durchlaufen
        list.forEach(l -> l.forEach(s -> writer.writeNext(s)));
        writer.close();

        // Erstellen der html-Ausgabe
        Table table = Table.read().csv(dateiName);
        NumberColumn testReihe = table.nCol("Prozent");
        Table table1 = table.summarize(testReihe, mean).by("Tag", "TestReihe");
        if (verbose) System.out.printf(table1.toString());
        Plot.show(LinePlot.create("Meinung",
                table1, "Tag", "Mean [Prozent]", "TestReihe"));
    }

    /**
     * Ermittle den Schnitt für Tage bis 100% Personen Meinung A vertreten
     * @return  Durchschnittliche Anzahl an Tagen
     */
    private double getAndResetSchnitt()
    {
        double x = sumTage;
        sumTage = 0;
        return x / anzDurchlaufe;
    }
}
