package com.cruz.fyp.virtualassistant.Azure.SpeechSynthesis;

import android.media.AudioManager;
import android.media.AudioTrack;
import android.os.AsyncTask;
import android.util.Log;

public class Synthesizer {

    private Voice mServiceVoice;
    private AudioTrack audioTrack;
    private Boolean isTalking;

    private TtsServiceClient mTtsServiceClient;
    private ServiceStrategy mEServiceStrategy;

    private void playSound(final byte[] sound) {
        if (sound == null || sound.length == 0){
            return;
        }
        AsyncTask.execute(() -> {
            final int SAMPLE_RATE = 16000;

            audioTrack = new AudioTrack(AudioManager.STREAM_MUSIC, SAMPLE_RATE, android.media.AudioFormat.CHANNEL_CONFIGURATION_MONO,
                android.media.AudioFormat.ENCODING_PCM_16BIT, AudioTrack.getMinBufferSize(SAMPLE_RATE, android.media.AudioFormat.CHANNEL_CONFIGURATION_MONO, android.media.AudioFormat.ENCODING_PCM_16BIT), AudioTrack.MODE_STREAM);

            if (audioTrack.getState() == AudioTrack.STATE_INITIALIZED) {
                audioTrack.play();
                audioTrack.write(sound, 0, sound.length);
                audioTrack.stop();
                audioTrack.release();
            }
        });
    }

    public void stopSound() {
        if(isTalking) {
            try {
                if (audioTrack != null && audioTrack.getState() == AudioTrack.STATE_INITIALIZED) {
                    audioTrack.pause();
                    audioTrack.flush();
                }
            } catch (Exception e) {
                Log.e("Synthesizer", e.getMessage());
            }
            isTalking = false;
        }
    }
    public enum ServiceStrategy {
        ALWAYS_SERVICE

    }

    public Synthesizer(String apiKey) {
        mServiceVoice = new Voice("en-US");
        mEServiceStrategy = ServiceStrategy.ALWAYS_SERVICE;
        mTtsServiceClient = new TtsServiceClient(apiKey);
        isTalking = false;
    }

    public void setVoice(Voice serviceVoice) {
        mServiceVoice = serviceVoice;
    }

    public void setServiceStrategy(ServiceStrategy eServiceStrategy) {
        mEServiceStrategy = eServiceStrategy;
    }

    private byte[] speak(String text) {
        String ssml = "<speak version='1.0' xml:lang='" + mServiceVoice.lang + "'><voice xml:lang='" + mServiceVoice.lang + "' xml:gender='" + mServiceVoice.gender + "'";
        if (mEServiceStrategy == ServiceStrategy.ALWAYS_SERVICE) {
            if (mServiceVoice.voiceName.length() > 0) {
                ssml += " name='" + mServiceVoice.voiceName + "'>";
            } else {
                ssml += ">";
            }
            ssml +=  text + "</voice></speak>";
        }
        return speakSSML(ssml);
    }

    public Boolean getTalking() {
        return isTalking;
    }

    public void speakToAudio(String text) {
        isTalking = true;
        playSound(speak(text));
    }

    private byte[] speakSSML(String ssml) {
        byte[] result = null;
        if (mEServiceStrategy == ServiceStrategy.ALWAYS_SERVICE) {
            result = mTtsServiceClient.SpeakSSML(ssml);
            if (result == null || result.length == 0) {
                return new byte[0];
            }
        }
        return result;
    }
}
