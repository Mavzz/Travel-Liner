package com.example.travelliner;

import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import androidx.annotation.RequiresApi;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

class userNameRetrival extends AsyncTask<String, String,String>
{
    onTaskCompletion mCompletion;
    public static String emailId;

    public userNameRetrival(String emailId)
    {
        userNameRetrival.emailId = emailId;
    }

    protected void onPreExecute(){
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    protected String doInBackground(String... arg0)
    {
        HttpURLConnection conn;
        try
        {
            URL url = new URL("http://192.168.108.1:8080/scripts/userNameRetrival.php");
            conn = (HttpURLConnection) url.openConnection();
            conn.setDoOutput(true);
            Uri.Builder builder = new Uri.Builder()
                    .appendQueryParameter("user_name", emailId);

            String query = builder.build().getEncodedQuery();
            OutputStream os = conn.getOutputStream();
            BufferedWriter writer = new BufferedWriter(
                    new OutputStreamWriter(os, StandardCharsets.UTF_8));

            writer.write( query );
            writer.flush();
            writer.close();
            os.close();
            conn.connect();
        }
        catch(Exception e)
        {
            e.printStackTrace();
            return "exception";
        }
        try{
            int response_code = conn.getResponseCode();
            if (response_code == HttpURLConnection.HTTP_OK)
            {
                // Read data sent from server
                InputStream input = conn.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(input));
                StringBuilder result = new StringBuilder();

                String line;
                while ((line = reader.readLine()) != null) {
                    result.append(line);
                }
                // Pass data to onPostExecute method
                return(result.toString());
            }
            else{
                return("unsuccessful");
            }
        }
        catch(Exception e){
            e.printStackTrace();
            return "exception";
        }
    }

    protected void onPostExecute(String result)
    {
        mCompletion.onTaskCompleted(result);
    }
}