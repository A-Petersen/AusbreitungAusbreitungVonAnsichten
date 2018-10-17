import com.opencsv.CSVWriter;
import tech.tablesaw.api.Table;
import tech.tablesaw.plotly.Plot;
import tech.tablesaw.plotly.api.LinePlot;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

public class Untersuchung {
    int anzTage;
    int anzPersonen;
    int meinungsvertreter;
    double wahrscheinlichkeitBegegnung;
    double wahrscheinlichkeitMeinungsbildung;
    int anzDurchlaufe;

    Untersuchung(int t, int p, int m, double wB, double wMB, int aD)
    {
        anzTage = t;
        anzPersonen = p;
        meinungsvertreter = m;
        wahrscheinlichkeitBegegnung = wB;
        wahrscheinlichkeitMeinungsbildung = wMB;
        anzDurchlaufe = aD;
    }

    public void start() throws IOException {

        List<List<String[]>> u1 = new LinkedList<>();
        for (int i = 0; i < anzDurchlaufe; i++)
        {
            List<String[]> x = untersuchung(true, i);
            u1.add(x);
        }

        List<List<String[]>> u2 = new LinkedList<>();
        for (int i = 0; i < anzDurchlaufe; i++)
        {
            List<String[]> x = untersuchung(false, i);
            u1.add(x);
        }

        File file = new File("X:\\IntellijProjects\\IntSys\\AusbreitungVonAnsichten\\DatenReihen.csv");

        FileWriter outputfile = new FileWriter(file);
        CSVWriter writer = new CSVWriter(outputfile);
        String[] header = { "Tag", "Prozent", "TestReihe" };
        writer.writeNext(header);

        u1.forEach(l -> l.forEach(s -> writer.writeNext(s)));
        u2.forEach(l -> l.forEach(s -> writer.writeNext(s)));

        writer.close();

        Table table = Table.read().csv("DatenReihen.csv");
        Plot.show(LinePlot.create("Meinung",
                table, "Tag", "Prozent", "TestReihe"));
    }

    /**
     *
     * @param ablaufart true für abhängige, false für unabhängige Meinung
     */
    private List<String[]> untersuchung(boolean ablaufart, int nr)
    {
        Tagesablauf ablauf = new Tagesablauf(anzPersonen, meinungsvertreter);
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

            String[] csvData_x = {tag + "", ablauf.meinungsVerteilung() + "", ablaufart ? "abhängiger_" + nr :"unabhängiger_" + nr};
            csvDataList.add(csvData_x);
        }
        zwischenErgebnis(ablauf, ablaufart ? "abhängiger":"unabhängiger");
        return csvDataList;
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
