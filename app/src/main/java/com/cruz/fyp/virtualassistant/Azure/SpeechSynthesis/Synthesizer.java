package com.cruz.fyp.virtualassistant.Azure.SpeechSynthesis;

import android.media.AudioManager;
import android.media.AudioTrack;
import android.os.AsyncTask;

public class Synthesizer {

    private Voice m_serviceVoice;
    private AudioTrack audioTrack;
    private Boolean isTalking;

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
                e.printStackTrace();
            }
            isTalking = false;
        }
    }

    public enum ServiceStrategy {
        AlwaysService
    }

    public Synthesizer(String apiKey) {
        m_serviceVoice = new Voice("en-US");
        m_eServiceStrategy = ServiceStrategy.AlwaysService;
        m_ttsServiceClient = new TtsServiceClient(apiKey);
        isTalking = false;
    }

    public void SetVoice(Voice serviceVoice) {
        m_serviceVoice = serviceVoice;
    }

    public void SetServiceStrategy(ServiceStrategy eServiceStrategy) {
        m_eServiceStrategy = eServiceStrategy;
    }

    private byte[] Speak(String text) {
        String ssml = "<speak version='1.0' xml:lang='" + m_serviceVoice.lang + "'><voice xml:lang='" + m_serviceVoice.lang + "' xml:gender='" + m_serviceVoice.gender + "'";
        if (m_eServiceStrategy == ServiceStrategy.AlwaysService) {
            if (m_serviceVoice.voiceName.length() > 0) {
                ssml += " name='" + m_serviceVoice.voiceName + "'>";
            } else {
                ssml += ">";
            }
            ssml +=  text + "</voice></speak>";
        }
        return SpeakSSML(ssml);
    }

    public Boolean getTalking() {
        return isTalking;
    }

    public void SpeakToAudio(String text) {
        isTalking = true;
        playSound(Speak(text));
    }

    private byte[] SpeakSSML(String ssml) {
        byte[] result = null;
        if (m_eServiceStrategy == ServiceStrategy.AlwaysService) {
            result = m_ttsServiceClient.SpeakSSML(ssml);
            if (result == null || result.length == 0) {
                return null;
            }
        }
        return result;
    }

    private TtsServiceClient m_ttsServiceClient;
    private ServiceStrategy m_eServiceStrategy;
}
