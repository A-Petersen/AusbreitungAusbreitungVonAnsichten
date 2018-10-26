import java.io.IOException;

/**
 * Ausführende Main-Klasse
 */
public class main {
    public static void main(String [ ] args) throws IOException {

        //TODO: uU. nicht gut implementiert, MagicValues !?
        Untersuchung u =    new Untersuchung
                            (
                            500,
                            50,
                            3,
                            0.00405,     //0.0063
                            0.022,   //0.045
                            10000,
                            2,
                            5,
                            false
                            );
        u.start();
    }
}
