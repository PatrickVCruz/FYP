package com.cruz.fyp.virtualassistant.Speech_Recognisition;

import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class FindTheRoom extends AsyncTask<String, Void, String>  {

    private String message;
    private int searchByName;

    FindTheRoom(String message, int searchByName) {
        this.message = message;
        this.searchByName = searchByName;
    }

    @Override
    protected String doInBackground(String... strings) {
        HttpURLConnection urlConnection = null;
        StringBuilder result = new StringBuilder();
        try {
            URL url = new URL("http://192.168.137.1/ulguidedatabase/test.php?code="+ message +"&search="+searchByName);
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
            Log.e("FindTheRoom",e.getMessage());
        }

        finally {
            assert urlConnection != null;
            urlConnection.disconnect();
        }
        return result.toString();
    }


}
