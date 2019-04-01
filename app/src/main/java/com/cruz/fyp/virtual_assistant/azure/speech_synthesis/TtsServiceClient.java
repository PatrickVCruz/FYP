package com.cruz.fyp.virtual_assistant.azure.speech_synthesis;

import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

class TtsServiceClient {
    private static final String LOG_TAG = "SpeechSDKTTS";
    private final String mServiceUri;
    private String mOutputFormat;
    private final Authentication mAuth;
    private byte[] mResult;

    TtsServiceClient(String apiKey) {
        mOutputFormat = "raw-16khz-16bit-mono-pcm";
        mServiceUri = "https://westeurope.tts.speech.microsoft.com/cognitiveservices/v1?traffictype=AndroidTest";
        mAuth = new Authentication(apiKey);
    }

    private void doWork(String ssml) {
        int code;
        synchronized(mAuth) {
            String accessToken = mAuth.getAccessToken();
            try {
                URL url = new URL(mServiceUri);
                HttpsURLConnection urlConnection = (HttpsURLConnection)url.openConnection();
                urlConnection.setDoInput(true);
                urlConnection.setDoOutput(true);
                urlConnection.setConnectTimeout(5000);
                urlConnection.setReadTimeout(15000);
                urlConnection.setRequestMethod("POST");
                String sContentType = "application/ssml+xml";
                urlConnection.setRequestProperty("Content-Type", sContentType);
                urlConnection.setRequestProperty("X-MICROSOFT-OutputFormat", mOutputFormat);
                urlConnection.setRequestProperty("Authorization", "Bearer " + accessToken);
                urlConnection.setRequestProperty("X-Search-AppId", "07D3234E49CE426DAA29772419F436CA");
                urlConnection.setRequestProperty("X-Search-ClientID", "1ECFAE91408841A480F00935DC390960");
                urlConnection.setRequestProperty("User-Agent", "TTSAndroid");
                urlConnection.setRequestProperty("Accept", "*/*");

                byte[] ssmlBytes = ssml.getBytes();
                urlConnection.setRequestProperty("content-length", String.valueOf(ssmlBytes.length));
                urlConnection.connect();
                urlConnection.getOutputStream().write(ssmlBytes);
                code = urlConnection.getResponseCode();
                if (code == 200) {
                    InputStream in = urlConnection.getInputStream();
                    ByteArrayOutputStream bout = new ByteArrayOutputStream();
                    byte[] bytes = new byte[1024];
                    int ret = in.read(bytes);
                    while (ret > 0) {
                        bout.write(bytes, 0, ret);
                        ret = in.read(bytes);
                    }
                    mResult = bout.toByteArray();
                }
                urlConnection.disconnect();
            } catch (Exception e) {
                Log.e(LOG_TAG, "Exception error", e);
            }
        }
    }

    byte[] speakSSML(final String ssml) {
        Thread th = new Thread(() -> doWork(ssml));
        try {
            th.start();
            th.join();
        } catch (Exception e) {
            Log.e(LOG_TAG, "Exception error", e);
        }

        return mResult;
    }
}
