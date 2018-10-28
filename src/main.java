import java.io.IOException;

/**
 * Ausführende Main-Klasse
 */
public class main {
    public static void main(String [ ] args) throws IOException {

        Untersuchung u =    new Untersuchung
                            (
                            500,
                            50,
                            3,
                            0.00405,        // 0.00405 ~Durchnittlich 200 nötige Tage für 100%
                            0.022,      // 0.022 ~Durchnittlich 200 nötige Tage für 100%
                            1000,
                            2,
                            10,
                            true
                            );
        u.start();
    }
}
