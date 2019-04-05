package com.cruz.fyp.virtual_assistant.azure;

import android.util.Log;

import com.microsoft.cognitiveservices.speech.CancellationDetails;
import com.microsoft.cognitiveservices.speech.ResultReason;
import com.microsoft.cognitiveservices.speech.SpeechConfig;
import com.microsoft.cognitiveservices.speech.SpeechRecognitionResult;
import com.microsoft.cognitiveservices.speech.SpeechRecognizer;

import java.util.concurrent.Future;

public class Speech {

    private static final String LOG_TAG = "Speech";


    public Speech() {
        //Do nothing
    }

    public String startSpeech() {

        try {
            String speechSubscriptionKey = "sub_key";
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
                Log.d(LOG_TAG, result.getText() );
                Log.d(LOG_TAG, String.valueOf(cancellation.getErrorCode()));
                Log.d(LOG_TAG, cancellation.getErrorDetails() );
                Log.d(LOG_TAG, String.valueOf(cancellation.getReason().getValue()));
                return result.toString();
            }

        } catch (Exception ex) {
            Log.e(LOG_TAG, "unexpected " + ex.getMessage());
        }
        return "I'm sorry, I didn't quite hear you could you repeat that?";
    }


}
