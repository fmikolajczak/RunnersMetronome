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

public class MainActivity extends Activity implements  View.OnClickListener {
    Button button;
    MediaPlayer mp;
    ClickRunnable clickRunnable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        button = (Button) findViewById(R.id.button);
        button.setOnClickListener(this);

        button = (Button) findViewById(R.id.bt_thread);
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

        if (R.id.button == v.getId() ) {
            // click button
            mp.start();
        }

        if (R.id.bt_thread == v.getId()) {
            // run thread

            if(clickRunnable == null) {
                Toast.makeText(this, "start", Toast.LENGTH_SHORT).show();
                clickRunnable= new ClickRunnable();
                Thread thread = new Thread(clickRunnable);
                thread.start();

            } else {
                Toast.makeText(this, "stop", Toast.LENGTH_SHORT).show();
                clickRunnable.stop();
                clickRunnable = null;
            }

        }
    }

    class ClickRunnable implements Runnable {
        private boolean stop = false;

        @Override
        public void run() {
            Log.d(TAG, "run: Thread started");
            mp = MediaPlayer.create(MainActivity.this , R.raw.click);
            Boolean msgLogged = false;

            while (! stop) {
                if (! msgLogged) {
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

}
