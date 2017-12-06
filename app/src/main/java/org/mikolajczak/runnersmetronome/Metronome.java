package org.mikolajczak.runnersmetronome;

import android.util.Log;

import static android.content.ContentValues.TAG;

/**
 * Created by fm on 2017-11-30.
 */

public class Metronome implements Runnable {
    private double bpm = 180;
    private int beat;
    private int noteValue;
    private int silence;

    private double beatSound = 659.26;
    private double sound = 659.26;
    private final int tick = 1000; // samples of tick

    private boolean play = true;

    private AudioGenerator audioGenerator = new AudioGenerator(8000);

    public Metronome() {
        Log.d(TAG, "Metronome: create player");
        audioGenerator.createPlayer();
    }

    public void calcSilence() {
        silence = (int) (((60/bpm)*8000)-tick);
    }

    public void play() {
        Log.d(TAG, "play: ");
        calcSilence();
        double[] tick =
                audioGenerator.getSineWave(this.tick, 8000, beatSound);
        double[] tock =
                audioGenerator.getSineWave(this.tick, 8000, sound);
        double silence = 0;
        double[] sound = new double[8000];
        int t = 0,s = 0,b = 0;
        do {
            for(int i=0; i < sound.length && play; i++) {
                if(t<this.tick) {
                    if(b == 0)
                        sound[i] = tock[t];
                    else
                        sound[i] = tick[t];
                    t++;
                } else {
                    sound[i] = silence;
                    s++;
                    if(s >= this.silence) {
                        t = 0;
                        s = 0;
                        b++;
                        if(b > (this.beat-1))
                            b = 0;
                    }
                }
            }
            Log.d(TAG, "play: write sound");
            audioGenerator.writeSound(sound);
        } while(play);
    }

    public void stop() {
        Log.d(TAG, "stop: ");
        play = false;
        audioGenerator.destroyAudioTrack();
    }

    @Override
    public void run() {
        play();
    }

}
