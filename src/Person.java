import java.util.Random;

public class Person {

    private static int pIDCount;
    private int pID;

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
    private int dauerEmpfaenglichkeit;
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
            //TODO: DoubleGen nutzen, new Random(x) zum debuggen (deterministisch), < statt <=,
            // pa = 0.1 ... pa,n = 1-(1-pa)^n .... k * pa,n (3 bereits bestehende Meinungen beachten)
            double randomNum = randGen.nextInt(10001) / 100.0;
            meinungA = randomNum <= pA;
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
        if (vergangeneTage >= dauerEmpfaenglichkeit) anzTreffen = 0;

        boolean aenderung = false;
        if (!p.getMeinungA() && meinungA)
        {
            aenderung = p.abhaengigeMeinung(this);
            p.resetVergangeneTage();
        }
        if (p.getMeinungA() && !meinungA)
        {
            if (anzTreffen >= anzBenoetigterTreffen)
            {
                meinungA = true;
                aenderung = true;
            }
            anzTreffen++;
            resetVergangeneTage();
        }
//        System.out.println(toString() + "\tTreffen mit PersonID: " + p.pID);
        return aenderung;
    }

    /**
     * Setzt die Meinung/Ansicht A auf true.
     */
    void setMeinungA (boolean b)
    {
        meinungA = b;
    }

    boolean getMeinungA()
    {
        return meinungA;
    }

    int getAnzTreffen()
    {
        return anzTreffen;
    }

    int getAnzBenoetigterTreffen()
    {
        return anzBenoetigterTreffen;
    }

    public String toString() {
        return "PersonID:\t" + pID + "\tAnzahl an Treffen(A): " + anzTreffen + " Meinung: " + (meinungA ? "A" : "X") + ".";
    }

    public void erhoeheVergangeneTage() {
        this.vergangeneTage = vergangeneTage + 1;
    }

    public void resetVergangeneTage() {
        this.vergangeneTage = 0;
    }
}
