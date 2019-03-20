package com.cruz.fyp.virtualassistant;

import android.os.AsyncTask;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class Think extends AsyncTask<String, Void, String>  {

    private String messaage;

    public Think(String messaage) {
        this.messaage = messaage;
    }

    private String collectAnswer(){
        return "Nothing";
    }

    @Override
    protected String doInBackground(String... strings) {
        HttpURLConnection urlConnection = null;
        StringBuilder result = new StringBuilder();
        try {
            URL url = new URL("http://192.168.1.5/ulguidedatabase/test.php?code="+messaage);
            urlConnection = (HttpURLConnection) url.openConnection();
            int code = urlConnection.getResponseCode();

            if(code==200){
                InputStream in = new BufferedInputStream(urlConnection.getInputStream());
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(in));
                String line;

                while ((line = bufferedReader.readLine()) != null)
                    result.append(line);
                in.close();
            }
            return result.toString();
        }
        catch (IOException e) {
            e.printStackTrace();
        }

        finally {
            assert urlConnection != null;
            urlConnection.disconnect();
        }
        return result.toString();
    }
}
