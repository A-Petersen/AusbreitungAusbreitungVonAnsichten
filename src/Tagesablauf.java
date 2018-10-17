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
    private Random randGen = new Random();
    private int anzMeinungA;

    /**
     * Konstruktor der Klasse Tagesablauf
     * @param p Anzahl an Personen im Tag
     * @param anzA Anzahl an Personen mit Meinung A
     */
    Tagesablauf (int p, int anzA)
    {
        personen = new LinkedList<Person>();
        fuellePersonen(p, anzA);
        anzMeinungA = anzA;
    }

    /**
     * Füllt die Lsite Peronen, sofern sie noch nicht existiert und
     * setzt die gewünschte Anzahl an Personen mit Meinung/Ansicht A.
     * @param n Anzahl an Personen
     * @param anzA Anzahl an Personen mit Meinung A
     */
    private void fuellePersonen (int n, int anzA)
    {
        if (!personen.isEmpty())
        {
            System.out.println("Personen Empty!");
            return;
        }

        for (int i = 0; i < n; i++)
        {
            Person p = new Person(false, 2);
            personen.add(p);
            System.out.println("Add Person.");
        }
        for (int i = 0; i < anzA; i++)
        {
            int rand = randGen.nextInt(n);
            System.out.println("Setze Meinung bei Person " + rand + ".");
            personen.get(rand).setMeinung();
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
    void simTagAbhaengigeMeinung(int pT)
    {
        int index = 0;
        for (Person person : personen)
        {
            for (int i = index + 1; i < personen.size(); i++)
            {
                int randomNum = randGen.nextInt(101);
                if (randomNum <= pT) person.abhaengigeMeinung(personen.get(i));
            }
            index++;
        }
    }

    private void erhöheAnzMeinungen(int n)
    {
        this.anzMeinungA = anzMeinungA + n;
    }

    double meinungsVerteilung ()
    {
        anzMeinungA = 0;
        personen.forEach(person -> {
            if (person.getAnsichtA())
            {
                erhöheAnzMeinungen(1);
            }
        });
        return personen.size()/(double)anzMeinungA;
    }

    String ausgabeMeinungsverteilung()
    {
        String ausgabe = "";
        for (Person p: personen)
        {
            if (p.getAnsichtA())
            {
                ausgabe = ausgabe + "|";
            } else {
                ausgabe = ausgabe + "-";
            }
        }
        return ausgabe;
    }

    int getAnzMeinungA()
    {
        return anzMeinungA;
    }
}
