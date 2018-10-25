import java.io.IOException;

public class main {
    public static void main(String [ ] args) throws IOException {

        //TODO: uU. nicht gut implementiert, MagicValues !?
        Untersuchung u =    new Untersuchung
                            (
                            250,
                            50,
                            3,
                            0.5,
                            2,
                            500,
                            2,
                            5
                            );

        u.start();

        //TODO: 102% bei unabhängige möglich !!!!!!!!
    }
}
