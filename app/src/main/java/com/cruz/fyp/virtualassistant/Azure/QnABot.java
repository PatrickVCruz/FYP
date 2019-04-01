package com.cruz.fyp.virtualassistant.Azure;

import android.os.AsyncTask;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.nio.charset.StandardCharsets;

import javax.net.ssl.HttpsURLConnection;

public class QnABot extends AsyncTask<String, Void, String> {

    private String kb = "80fdef9b-f16a-49fc-b1bf-ac9204ad25f3";
    private String method = "/qnamaker/knowledgebases/" + kb + "/generateAnswer";
    private String askedQuestion;
    private String question;

    public void setQuestion(String askedQuestion) {
        this.askedQuestion = askedQuestion;
        this.question = "{ 'question' : '"+ askedQuestion +"', 'top' : 1 }";
    }

    public QnABot() {
        this.question = "{ 'question' : '"+ askedQuestion +"', 'top' : 1 }";
    }


    private String PrettyPrint(String json_text) throws JSONException {
        JsonParser parser = new JsonParser();
        JsonElement json = parser.parse(json_text);
        Gson gson = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();
        String result = gson.toJson(json);

        JSONObject obj = new JSONObject(result);
        JSONArray gdata = obj.getJSONArray("answers");
        JSONObject person = gdata.getJSONObject(0);

        return gson.toJson(person.getString("answer"));
    }


    @Override
    public String doInBackground(String... strings) {
        URL url = null;
        StringBuilder response = new StringBuilder ();
        try {
            String host = "https://fyp-faq.azurewebsites.net";
            url = new URL(host + method);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        HttpsURLConnection connection = null;
        try {
            assert url != null;
            connection = (HttpsURLConnection) url.openConnection();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            assert connection != null;
            connection.setRequestMethod("POST");
        } catch (ProtocolException e) {
            e.printStackTrace();
        }
        connection.setRequestProperty("Content-Type", "application/json");
        connection.setRequestProperty("Content-Length", question.length() + "");
        String endpoint_key = "11e1a057-6d48-453d-b3bc-1591e956016a";
        connection.setRequestProperty("Authorization", "EndpointKey " + endpoint_key);
        connection.setDoOutput(true);

        DataOutputStream wr;
        try {
            wr = new DataOutputStream(connection.getOutputStream());
            byte[] encoded_content = question.getBytes(StandardCharsets.UTF_8);
            wr.write(encoded_content, 0, encoded_content.length);
            wr.flush();
            wr.close();

            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8));

            String line;
            while ((line = in.readLine()) != null) {
                response.append(line);
            }
            Log.d("unexpected ", response.toString());
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            String result = PrettyPrint(response.toString());
            if(result.matches("\"No good match found in KB.\"")){
                return "I'm sorry, could you repeat that";
            }
            else
                return result;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return "I'm sorry, could you repeat that";
    }
}
