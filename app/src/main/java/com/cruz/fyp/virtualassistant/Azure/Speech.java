package com.cruz.fyp.virtualassistant.Azure;

import android.util.Log;

import com.microsoft.cognitiveservices.speech.ResultReason;
import com.microsoft.cognitiveservices.speech.SpeechConfig;
import com.microsoft.cognitiveservices.speech.SpeechRecognitionResult;
import com.microsoft.cognitiveservices.speech.SpeechRecognizer;
import java.util.concurrent.Future;

public class Speech {

    public Speech() {
    }

    public String startSpeech() {

        try {
            String speechSubscriptionKey = "fd42e10a86964bb49121885b0c33c170";

            String serviceRegion = "westeurope";
            SpeechConfig config = SpeechConfig.fromSubscription(speechSubscriptionKey, serviceRegion);
            SpeechRecognizer speechRecognizer = new SpeechRecognizer(config);
            Future<SpeechRecognitionResult> task = speechRecognizer.recognizeOnceAsync();
            SpeechRecognitionResult result = task.get();

            if (result.getReason() == ResultReason.RecognizedSpeech) {
                return result.getText();
            }

            speechRecognizer.close();
        } catch (Exception ex) {
            Log.e("Speech", "unexpected " + ex.getMessage());
        }
        return null;
    }


}
