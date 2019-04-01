package com.cruz.fyp.virtualassistant.Azure;

import android.util.Log;

import com.microsoft.cognitiveservices.speech.CancellationDetails;
import com.microsoft.cognitiveservices.speech.ResultReason;
import com.microsoft.cognitiveservices.speech.SpeechConfig;
import com.microsoft.cognitiveservices.speech.SpeechRecognitionResult;
import com.microsoft.cognitiveservices.speech.SpeechRecognizer;

import java.util.concurrent.Future;

public class Speech {

    public Speech() {}

    public String startSpeech() {

        try {
            String speechSubscriptionKey = "c44bdcc80c534399867a85e8212b806f";
            String serviceRegion = "westeurope";
            SpeechConfig config = SpeechConfig.fromSubscription(speechSubscriptionKey, serviceRegion);
            SpeechRecognizer speechRecognizer = new SpeechRecognizer(config);
            Future<SpeechRecognitionResult> task = speechRecognizer.recognizeOnceAsync();
            SpeechRecognitionResult result = task.get();

            if (result.getReason() == ResultReason.RecognizedSpeech) {
                speechRecognizer.close();
                return result.getText();
            }
            else {
                speechRecognizer.close();
                CancellationDetails cancellation = CancellationDetails.fromResult(result);
                Log.d("Speech","unexpected " + result.getText() );
                Log.d("Speech","unexpected " + cancellation.getErrorCode() );
                Log.d("Speech","unexpected " + cancellation.getErrorDetails() );
                Log.d("Speech","unexpected " + cancellation.getReason().getValue() );
                return result.toString();
            }

        } catch (Exception ex) {
            Log.e("Speech", "unexpected " + ex.getMessage());
        }
        return "I'm sorry, I didn't quite hear you could you repeat that?";
    }


}
