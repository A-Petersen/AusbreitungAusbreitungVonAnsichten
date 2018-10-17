public class main {
    public static void main(String [ ] args) {
        Tagesablauf tag = new Tagesablauf(50, 3);
        tag.simTagAbhaengigeMeinung(50);
        System.out.println(
                "Meinungsverteilung in %:\t" + tag.meinungsVerteilung() + "\n" +
                tag.ausgabeMeinungsverteilung() + "\n" +
                tag.getAnzMeinungA()
        );
    }
}
