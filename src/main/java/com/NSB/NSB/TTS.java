package com.NSB.NSB;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.AsyncTask;
import android.speech.tts.TextToSpeech;

import java.util.Locale;

import static android.media.AudioManager.ERROR;

class TTS extends Thread{
    private TextToSpeech tts;
    private String input;
    TTS(Context context) {
        tts = new TextToSpeech(context, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if(status != ERROR) {
                    // 언어를 선택한다.
                    tts.setLanguage(Locale.KOREAN);
                }
            }
        });
    }

    void speak(String input) {
        this.input = input;
        MyAsyncTask run = new MyAsyncTask();
        run.execute();
    }

    @SuppressLint("StaticFieldLeak")
    public class MyAsyncTask extends AsyncTask<Integer, Integer, Void> {

        @Override
        protected Void doInBackground(Integer... integers) {
            tts.speak(input, TextToSpeech.QUEUE_FLUSH, null);
//            try {
//                Thread.sleep(2000);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
            return null;
        }
    }
}
