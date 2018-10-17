import java.util.Random;

public class Person {

    private static int pIDCount;
    private int pID;

    private boolean AnsichtA;
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
        AnsichtA = m;
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
    void unabhaengigeMeinung (int pA)
    {
        int randomNum = randGen.nextInt(101);
        AnsichtA = ( randomNum <= pA && AnsichtA != true ) ? false : true;
    }

    /**
     * Triggert die abhängige Meinungsbildung A.
     * Erhöht die Anzahl der Treffen um 1 und die Ansicht A auf true, sofern die
     * nötige Anzahl von Treffen erreicht oder überschritten wurde
     * und die getroffene Person die nötige Ansicht A vertritt.
     */
    void abhaengigeMeinung(Person p)
    {
        if (p.getAnsichtA()) {
            if (anzTreffen >= anzBenoetigterTreffen) {
                AnsichtA = true;
            }
            anzTreffen++;
        }
    }

    /**
     * Setzt die Meinung/Ansicht A auf true.
     */
    void setMeinung ()
    {
        AnsichtA = true;
    }

    boolean getAnsichtA()
    {
        return AnsichtA;
    }
}
