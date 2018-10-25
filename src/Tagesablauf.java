import java.util.LinkedList;
import java.util.List;
import java.util.Random;

public class Tagesablauf {

    /**
     * Liste der Personen im Tag
     */
    private List<Person> personen;
    /**
     * RandomGenerator
     */
    private Random randGen = new Random(); //TODO: threadLocalRandom
    /**
     * Anzahl der Meinungen A in der Liste der Personen
     */
    private int anzMeinungA;

    /**
     * Konstruktor der Klasse Tagesablauf
     * @param p Anzahl an Personen im Tag
     * @param anzA Anzahl an Personen mit Meinung A
     */
    Tagesablauf (int p, int anzA, int aBT, int dE)
    {
        personen = new LinkedList<Person>();
        fuellePersonen(p, anzA, aBT, dE);
        anzMeinungA = anzA;
    }

    /**
     * Füllt die Lsite Peronen, sofern sie noch nicht existiert und
     * setzt die gewünschte Anzahl an Personen mit Meinung/Ansicht A.
     * @param n Anzahl an Personen
     * @param anzA Anzahl an Personen mit Meinung A
     */
    private void fuellePersonen (int n, int anzA, int aBT, int dE)
    {
        if (!personen.isEmpty())
        {
            System.out.println("Personen Empty!");
            return;
        }

        for (int i = 0; i < n; i++)
        {
            Person p = new Person(false, aBT, dE);
            personen.add(p);
//            System.out.println("Add Person.");
        }
        for (int i = 0; i < anzA; i++)
        {
//            int rand = randGen.nextInt(n);
            System.out.println("Setze Meinung bei Person " + i + ".");
//            //TODO: if meinung set nochmal
//
//            personen.get(rand).setMeinungA(true);
            personen.get(i).setMeinungA(true);
        }

    }

    /**
     * Entfernt eine Person aus der Liste der Personen.
     * @param p zu entfernende Person
     */
    void entfernePerson (Person p)
    {
        personen.remove(p);
    }

    /**
     * Simuliert einen Tag.
     * Lässt alle Personen in der Klasse eine Begegnung durchführen, mit der
     * Wahrscheinlichkeit pT.
     * @param pT Wahrschienlichkeit in Prozent (int 0... 100)
     */
    void simTagAbhaengigeMeinung(double pT)
    {
        int index = 1;
        boolean aenderung;
        for (Person person : personen)
        {
            for (int i = index; i < personen.size(); i++)
            {
                double randomNum = randGen.nextDouble() * 100.0;
                if (randomNum < pT) {
//                    System.out.println("PID: " + i + "\t" + personen.get(i).getMeinungA() + " \t\t- Self: " + person.getMeinungA());
                    aenderung = person.abhaengigeMeinung(personen.get(i));
                    if (aenderung) anzMeinungA++;
                }
            }
            index++;
            person.erhoeheVergangeneTage();
        }
    }

    /**
     * Simuliert die unabhängige Meinungsbildung der Personen, bei einer spezifizierten Wahrscheinlichkeit
     * @param pA Wahrscheinlichkeit der unabhängigen Meinungsbildung
     */
    void simTagUnabhaengigeMeinung(double pA) {
        boolean mAenderung;
        for (Person person : personen)
        {
            mAenderung = person.unabhaengigeMeinung(pA);
            if (mAenderung) anzMeinungA++;
        }
    }

//    private void erhöheAnzMeinungen(int n)
//    {
//        this.anzMeinungA = anzMeinungA + n;
//    }

    /**
     *
     * @return
     */
    double meinungsVerteilung ()
    {
//        anzMeinungA = 0;
//        personen.forEach(person -> {
//            if (person.getMeinungA())
//            {
//                erhöheAnzMeinungen(1);
//            }
//        });
        return (double)anzMeinungA/personen.size()*100.0;
    }

    String ausgabeMeinungsverteilung()
    {
        String ausgabe = "";
        for (Person p: personen)
        {
            if (p.getMeinungA())
            {
                ausgabe = ausgabe + "|";
            } else {
                ausgabe = ausgabe + "-";
            }
        }
        return ">> " + ausgabe + " <<";
    }

    int getAnzMeinungA()
    {
        return anzMeinungA;
    }

    List<Person> getPersonenCSVData()
    {
        return personen;
    }
}
