import org.junit.Test;
import org.junit.experimental.theories.DataPoint;

import static org.junit.Assert.*;

public class PersonTest {

    @DataPoint public static Person personU = new Person(false, 2, 5);
    @DataPoint public static Person personA = new Person(false, 2, 5);
    @DataPoint public static Person personB = new Person(true, 2, 5);
    @DataPoint public static Person personC = new Person(true, 2, 5);
    @DataPoint public static Person personD = new Person(false, 2, 5);
    @DataPoint public static double pA_100 = 1;
    @DataPoint public static double pA_0 = 0;

    @Test
    public void unabhaengigeMeinungTest() {
        assertFalse(personU.unabhaengigeMeinung(pA_0));
        assertFalse(personU.getMeinungA());
        assertTrue(personU.unabhaengigeMeinung(pA_100));
        assertTrue(personU.getMeinungA());
    }

    @Test
    public void abhaengigeMeinungTest() {
        assertFalse(personA.abhaengigeMeinung(personB));
        assertFalse(personA.getMeinungA());
        assertTrue(personA.abhaengigeMeinung(personC));
        assertTrue(personA.getMeinungA());

        assertFalse(personA.abhaengigeMeinung(personD));
        assertTrue(personB.abhaengigeMeinung(personD));
        assertTrue(personD.getMeinungA());
    }
}