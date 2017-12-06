package org.mikolajczak.runnersmetronome;

import android.app.Activity;
import android.content.Context;
import android.media.AudioAttributes;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import static android.content.ContentValues.TAG;

public class MainActivity extends Activity implements View.OnClickListener {
    Button button;
    MediaPlayer mp;
    ClickRunnable clickRunnable;
    Metronome metronome;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        button = (Button) findViewById(R.id.button);
        button.setOnClickListener(this);

        button = (Button) findViewById(R.id.bt_thread);
        button.setOnClickListener(this);

        button = (Button) findViewById(R.id.bt_audiometronome);
        button.setOnClickListener(this);

        button = (Button) findViewById(R.id.bt_play_melody);
        button.setOnClickListener(this);

        SoundPool soundPool = new SoundPool.Builder()
                .setAudioAttributes(
                        new AudioAttributes.Builder()
                                .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                                .setUsage(AudioAttributes.USAGE_GAME)
                                .build()
                )
                .setMaxStreams(1)
                .build();

        soundPool.load(this, R.raw.click, 1);

        mp = MediaPlayer.create(this, R.raw.click);
    }

    @Override
    public void onClick(View v) {

        if (R.id.button == v.getId()) {
            // click button
            mp.start();
        }

        if (R.id.bt_thread == v.getId()) {
            // run thread

            if (clickRunnable == null) {
                Toast.makeText(this, "start", Toast.LENGTH_SHORT).show();
                clickRunnable = new ClickRunnable();
                Thread thread = new Thread(clickRunnable);
                thread.start();

            } else {
                Toast.makeText(this, "stop", Toast.LENGTH_SHORT).show();
                clickRunnable.stop();
                clickRunnable = null;
            }

        }

        if (R.id.bt_audiometronome == v.getId()) {
            if (metronome == null) {
                metronome = new Metronome();
                Thread metronomeThread = new Thread(metronome);
                metronomeThread.start();
            } else {
                metronome.stop();
                metronome = null;
            }

        }

        if (R.id.bt_play_melody == v.getId()) {
            playMelody();
        }
    }

    class ClickRunnable implements Runnable {
        private boolean stop = false;

        @Override
        public void run() {
            Log.d(TAG, "run: Thread started");
            mp = MediaPlayer.create(MainActivity.this, R.raw.click);
            Boolean msgLogged = false;

            while (!stop) {
                if (!msgLogged) {
                    Log.d(TAG, "run: in while loop!");
                    msgLogged = true;
                }
                mp.start();
                try {
                    Thread.sleep(333);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

        public void stop() {
            stop = true;
        }
    }

    public void playMelody() {
        Log.d(TAG, "playMelody: ");
        
        AudioGenerator audio = new AudioGenerator(8000);

        double[] silence = audio.getSineWave(200, 8000, 0);

        int noteDuration = 2400;

        double[] doNote = audio.getSineWave(noteDuration/2, 8000, 523.25);
        double[] reNote = audio.getSineWave(noteDuration/2, 8000, 587.33);
        double[] faNote = audio.getSineWave(noteDuration, 8000, 698.46);
        double[] laNote = audio.getSineWave(noteDuration, 8000, 880.00);
        double[] laNote2 =
                audio.getSineWave((int) (noteDuration*1.25), 8000, 880.00);
        double[] siNote = audio.getSineWave(noteDuration/2, 8000, 987.77);
        double[] doNote2 =
                audio.getSineWave((int) (noteDuration*1.25), 8000, 523.25);
        double[] miNote = audio.getSineWave(noteDuration/2, 8000, 659.26);
        double[] miNote2 = audio.getSineWave(noteDuration, 8000, 659.26);
        double[] doNote3 = audio.getSineWave(noteDuration, 8000, 523.25);
        double[] miNote3 = audio.getSineWave(noteDuration*3, 8000, 659.26);
        double[] reNote2 = audio.getSineWave(noteDuration*4, 8000, 587.33);

        audio.createPlayer();
        audio.writeSound(doNote);
        audio.writeSound(silence);
        audio.writeSound(reNote);
        audio.writeSound(silence);
        audio.writeSound(faNote);
        audio.writeSound(silence);
        audio.writeSound(laNote);
        audio.writeSound(silence);
        audio.writeSound(laNote2);
        audio.writeSound(silence);
        audio.writeSound(siNote);
        audio.writeSound(silence);
        audio.writeSound(laNote);
        audio.writeSound(silence);
        audio.writeSound(faNote);
        audio.writeSound(silence);
        audio.writeSound(doNote2);
        audio.writeSound(silence);
        audio.writeSound(miNote);
        audio.writeSound(silence);
        audio.writeSound(faNote);
        audio.writeSound(silence);
        audio.writeSound(faNote);
        audio.writeSound(silence);
        audio.writeSound(miNote2);
        audio.writeSound(silence);
        audio.writeSound(doNote3);
        audio.writeSound(silence);
        audio.writeSound(miNote3);
        audio.writeSound(silence);
        audio.writeSound(doNote);
        audio.writeSound(silence);
        audio.writeSound(reNote);
        audio.writeSound(silence);
        audio.writeSound(faNote);
        audio.writeSound(silence);
        audio.writeSound(laNote);
        audio.writeSound(silence);
        audio.writeSound(laNote2);
        audio.writeSound(silence);
        audio.writeSound(siNote);
        audio.writeSound(silence);
        audio.writeSound(laNote);
        audio.writeSound(silence);
        audio.writeSound(faNote);
        audio.writeSound(silence);
        audio.writeSound(doNote2);
        audio.writeSound(silence);
        audio.writeSound(miNote);
        audio.writeSound(silence);
        audio.writeSound(faNote);
        audio.writeSound(silence);
        audio.writeSound(faNote);
        audio.writeSound(silence);
        audio.writeSound(miNote2);
        audio.writeSound(silence);
        audio.writeSound(miNote2);
        audio.writeSound(silence);
        audio.writeSound(reNote2);

        audio.destroyAudioTrack();
    }

}
