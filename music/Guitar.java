package music;

/**
 *
 * @author ronilprasad8
 */
public class Guitar extends Instrument {

    @Override
    public void play(Note n) {
        System.out.println("Guitar is playing " + n);
    }
}
