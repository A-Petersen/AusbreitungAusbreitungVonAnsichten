import java.util.LinkedList;
import java.util.List;
import java.util.Random;

/**
 * Klasse Tagesablauf
 */
public class Tagesablauf {

    /**
     * Liste der Personen im Tag
     */
    private List<Person> personen;
    /**
     * RandomGenerator
     */
    private Random randGen = new Random();
    /**
     * Anzahl der Meinungen A in der Liste der Personen
     */
    private int anzMeinungA;

    /**
     * Konstruktor der Klasse Tagesablauf
     *
     * @param p    Anzahl an Personen im Tag
     * @param anzA Anzahl an Personen mit Meinung A
     * @param aBT   Anzahl benötigter Treffen
     * @param dE    Dauer der Empfänglichkeit
     */
    Tagesablauf(int p, int anzA, int aBT, int dE) {
        personen = new LinkedList<Person>();
        fuellePersonen(p, anzA, aBT, dE);
        anzMeinungA = anzA;
    }

    /**
     * Füllt die Lsite Peronen, sofern sie noch nicht existiert und
     * setzt die gewünschte Anzahl an Personen mit Meinung/Ansicht A.
     *
     * @param n     Anzahl an Personen
     * @param anzA  Anzahl an Personen mit Meinung A
     * @param aBT   Anzahl benötigter Treffen
     * @param dE    Dauer der Empfänglichkeit
     */
    private void fuellePersonen(int n, int anzA, int aBT, int dE) {
        for (int i = 0; i < n; i++) {           // Erstellen der Personen und füllen der Liste
            Person p = new Person(false, aBT, dE);
            personen.add(p);
        }
        for (int i = 0; i < anzA; i++) {        // Setzen der initialen Meinungsvertreter
            personen.get(i).setMeinungA(true);
        }
    }

    /**
     * Simuliert einen Tag.
     * Lässt alle Personen in der Klasse eine Begegnung durchführen, mit der
     * Wahrscheinlichkeit pT.
     *
     * @param pT Wahrschienlichkeit in Prozent (int 0... 100)
     */
    void simTagAbhaengigeMeinung(double pT) {
        int index = 1;           // Startindex der Schleife für die zu treffenden Personen
        boolean aenderung;
        for (Person person : personen) {
            for (int i = index; i < personen.size(); i++) {     // Durchlaufen aller verbliebenen Personen (nach Index)
                double randomNum = randGen.nextDouble();
                if (randomNum < pT) {                           // Abfrage, ob Treffen stattfindet
                    aenderung = person.abhaengigeMeinung(personen.get(i));  // Abhängige Meinungsbildung durchlaufen + Änderung der Meinung setzten, falls eingetreten
                    if (aenderung) anzMeinungA++;               // Wenn sich Meinung geändert hat, Anzahl an Meinungsvertretern erhöhen
                }
            }
            index++;
            person.erhoeheVergangeneTage();     // Anzahl an vergangenen Tagen erhöhen (Für dauer der Empfänglichkeit wichtig)
        }
    }

    /**
     * Simuliert die unabhängige Meinungsbildung der Personen, bei einer spezifizierten Wahrscheinlichkeit
     *
     * @param pA Wahrscheinlichkeit der unabhängigen Meinungsbildung
     */
    void simTagUnabhaengigeMeinung(double pA) {
        boolean mAenderung;
        for (Person person : personen) {
            mAenderung = person.unabhaengigeMeinung(pA);    // Unabhängige Meinungsbildung durchlaufen + Änderung der Meinung setzten, falls eingetreten
            if (mAenderung) anzMeinungA++;                  // Wenn sich Meinung geändert hat, Anzahl an Meinungsvertretern erhöhen
        }
    }

    /**
     * Gibt die Meinungsverteilung des Tages zurück (von Meinung A)
     *
     * @return Meinungsverteilung in Prozent
     */
    double meinungsVerteilung() {
        return (double) anzMeinungA / personen.size() * 100.0;
    }

    /**
     * Gibt einen String aus, der als Balkenanzeige die Meinungsverteilung von Meinung A
     * wiederspiegelt
     * Beispielfür Meinungsverteilung 92% (46/50):
     * >> |||||||||||||||--||||||-||||||||||||||-||||||||||| <<
     *
     * @return Balkenanzeige der Meinungsverteilung von Meinung A
     */
    String ausgabeMeinungsverteilung() {
        String ausgabe = "";
        for (Person p : personen) {
            if (p.getMeinungA()) {
                ausgabe = ausgabe + "|";
            } else {
                ausgabe = ausgabe + "-";
            }
        }
        return ">> " + ausgabe + " <<";
    }

    /**
     * Gibt die Anzahl der Meinungsvertreter für Meinung A zurück
     *
     * @return Anzahl der Meinungsvertreter für Meinung A
     */
    int getAnzMeinungA() {
        return anzMeinungA;
    }
}