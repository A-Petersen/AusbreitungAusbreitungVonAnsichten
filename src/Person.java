import java.util.LinkedList;
import java.util.List;
import java.util.Random;

/**
 * Klasse Person
 */
public class Person {

    /**
     * Zähler für erstellte Personen
     */
    private static int pIDCount;
    /**
     * Personen ID
     */
    private int pID;

    /**
     * Vertritt Meinung A oder nicht
     */
    private boolean meinungA;
    /**
     * Anzahl der vergangenen Treffen
     */
    private int anzTreffen;
    /**
     * Benötigte Anzahl von Treffen für abhängige Meinungsbildung
     */
    private int anzBenoetigterTreffen;
    /**
     * vergangene Tage seit Empfänglichkeit für Meinung
     */
    private int vergangeneTage;
    /**
     * Anzahl der Tage bis die Empfänglichkeit erlischt
     */
    private int dauerEmpfaenglichkeit;
    /**
     * Liste der Personen, die in den vergangenen Tagen getroffen wurden und
     * Meinung A vertreten
     */
    private List<Person> missionare;
    /**
     * RandomGenerator
     */
    private Random randGen = new Random();

    /**
     * Konstruktor für Person
     * @param m     initiale Ansicht A
     * @param aT    Benötigte Anzahl von Treffen für abhängige Meinungsbildung
     */
    Person (boolean m, int aT, int dE)
    {
        pID = pIDCount;
        meinungA = m;
        anzBenoetigterTreffen = aT;
        pIDCount++;
        vergangeneTage = 0;
        dauerEmpfaenglichkeit = dE;
        missionare = new LinkedList<>();
    }

    /**
     * Triggert die unabhängige Meinungsbildung A.
     * Wobei pA der eintretenden Wahrscheinlichkeit in Prozent entspricht, dass die Meinung
     * A gebildet wird.
     * Kann die Meinung nur setzen, nicht rücksetzen!!!
     * @param pA    Wahrscheinlichkeint der Meinungsbildung (int von 0... 100)
     */
    boolean unabhaengigeMeinung (double pA)
    {
        boolean aenderung = false;
        if (!meinungA)
        {
            //TODO: new Random(x) zum debuggen (deterministisch)
            // pa = 0.1 ... pa,n = 1-(1-pa)^n .... k * pa,n (3 bereits bestehende Meinungen beachten)
            double randomNum = randGen.nextDouble();
            meinungA = randomNum < pA;
            aenderung = meinungA;
        }
        return aenderung;
    }

    /**
     * Triggert die abhängige Meinungsbildung A.
     * Erhöht die Anzahl der Treffen um 1 und die Ansicht A auf true, sofern die
     * nötige Anzahl von Treffen erreicht oder überschritten wurde
     * und die getroffene Person die nötige Ansicht A vertritt.
     */
    boolean abhaengigeMeinung(Person p)
    {
        if (vergangeneTage > dauerEmpfaenglichkeit)
        {
            anzTreffen = 0;
            missionare.clear();
        }

        boolean aenderung = false;

        if (!p.getMeinungA() && meinungA)
        {
            aenderung = p.abhaengigeMeinung(this);
        }

        if (p.getMeinungA()
            && !meinungA
            && (missionare.isEmpty() ? true : !missionare.contains(p)))
        {
            anzTreffen++;
            missionare.add(p);
            if (anzTreffen >= anzBenoetigterTreffen)
            {
                meinungA = true;
                aenderung = true;
            }
            resetVergangeneTage();
        }
        return aenderung;
    }

    /**
     * Setzt die Meinung/Ansicht A auf true.
     */
    void setMeinungA (boolean b)
    {
        meinungA = b;
    }

    /**
     * Gibt Zustand von Meinung A zurück
     * @return Hat oder hat nicht Meinung A
     */
    boolean getMeinungA()
    {
        return meinungA;
    }

    /**
     * toString Methode
     * @return Gibt Personen ID, anzahl an bereits grtroffenen Missionaren und derzeitige Meinung A zurück
     */
    public String toString() {
        return "PersonID:\t" + pID + "\tAnzahl an Treffen(A): " + anzTreffen + " Meinung: " + (meinungA ? "A" : "X") + ".";
    }

    /**
     * Erhöht die vergangenen Tage seit Missionierung
     */
    public void erhoeheVergangeneTage() {
        this.vergangeneTage = vergangeneTage + 1;
    }

    /**
     * Setzt die vergangenen Tage seit Missionierung zurück
     */
    public void resetVergangeneTage() {
        this.vergangeneTage = 0;
    }
}
