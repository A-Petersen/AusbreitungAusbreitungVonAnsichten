import java.io.IOException;

/**
 * Ausführende Main-Klasse
 */
public class main {
    public static void main(String [ ] args) throws IOException {

        //TODO: uU. nicht gut implementiert, MagicValues !?
        Untersuchung u =    new Untersuchung
                            (
                            300,
                            50,
                            3,
                            0.0065,
                            0.03,
                            100,
                            2,
                            5
                            );

        u.start();
    }
}
