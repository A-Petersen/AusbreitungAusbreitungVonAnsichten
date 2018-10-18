import java.io.IOException;

public class main {
    public static void main(String [ ] args) throws IOException {

        //TODO: uU. nicht gut implementiert, MagicValues !?
        Untersuchung u =    new Untersuchung
                            (
                            230,
                            50,
                            3,
                            0.9,
                            2,
                            50,
                            2,
                            5
                            );

        u.start();

    }
}
