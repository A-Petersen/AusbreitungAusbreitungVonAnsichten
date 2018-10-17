import java.util.Random;

public class Person {

    private boolean AnsichtA;
    private Random randGen;

    Person (boolean m) {
        AnsichtA = m;
    }

    /**
     * Triggert die unabhängige Meinungsbildung A.
     * Wobei pA der eintretenden Wahrscheinlichkeit in Prozent entspricht, dass die Meinung
     * A gebildet wird.
     * Kann die Meinung nur setzen, nicht rücksetzen!!!
     * @param pA Wahrscheinlichkeint der Meinungsbildung (int von 0... 100)
     */
    void unabhaengigeMeinung (int pA) {
        int randomNum = randGen.nextInt(100);
        this.AnsichtA = ( randomNum > pA && AnsichtA != true ) ? false : true;
    }

    /**
     * Setzt die Meinung/Ansicht A auf true.
     */
    void abhaengigeMeinung () {
        this.AnsichtA = true;
    }
}
