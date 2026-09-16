/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package music;

/**
 *
 * @author sharma_au
 */
public class Music {

    public static void tune(Instrument i) {
        // ...
        //play note MIDDLE_C
        i.play(Note.MIDDLE_C);
        //play note C_SHARP
        i.play(Note.C_SHARP);
    }

    public static void main(String[] args) {
        //create objects of Flute and Guitar
        Flute flute = new Flute();
        Guitar guitar = new Guitar();

        //call tune method
        tune(flute);
        tune(guitar);

        System.out.println("End of program.");
    }
}
