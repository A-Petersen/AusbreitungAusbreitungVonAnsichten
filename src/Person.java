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
     * RandomGenerator
     */
    private Random randGen = new Random();

    /**
     * Konstruktor für Person
     * @param m     initiale Ansicht A
     * @param aT    Benötigte Anzahl von Treffen für abhängige Meinungsbildung
     */
    Person (boolean m, int aT)
    {
        pID = pIDCount;
        meinungA = m;
        anzBenoetigterTreffen = aT;
        pIDCount++;
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
            double randomNum = randGen.nextInt(10001) / 100.0;
            meinungA = randomNum <= pA ? true : false;
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
        boolean aenderung = false;
        if (!p.getMeinungA() && meinungA) {
            aenderung = p.abhaengigeMeinung(this);
        }
        if (p.getMeinungA() && !meinungA) {
            if (anzTreffen >= anzBenoetigterTreffen) {
                meinungA = true;
                aenderung = true;
            }
            anzTreffen++;
        }
//        if (this.getMeinungA() && !p.getMeinungA()) {
//            p.setMeinungA(true);
//            if (p.getAnzTreffen() >= p.getAnzBenoetigterTreffen()) {
//                AnsichtA = true;
//            }
//            anzTreffen++;
//        }
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
}
