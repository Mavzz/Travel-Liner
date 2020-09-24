package com.example.travelliner;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.widget.Toast;

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

public class SigninActivity extends AsyncTask<String, String, String> {

    @SuppressLint("StaticFieldLeak")
    private Context context;
    public static String emailid;

    public SigninActivity(String emailid, Context context) {
        this.context = context;
        this.emailid = emailid;
    }

    protected void onPreExecute(){
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    protected String doInBackground(String... arg0) {
        HttpURLConnection conn;
        String emailId = arg0[0];
        String password = arg0[1];

        if (emailId.equals(""))
        {
            return "email";
        }
        else if (password.equals(""))
        {
            return "password";
        }
        else {
            try
            {
                URL url = new URL("http://192.168.108.1:8080/scripts/login.php");
                conn = (HttpURLConnection) url.openConnection();
                conn.setDoOutput(true);
                Uri.Builder builder = new Uri.Builder()
                        .appendQueryParameter("user_name", emailId)
                        .appendQueryParameter("user_password", password);

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

    }

    @SuppressLint("SetTextI18n")
    protected void onPostExecute(String result){
        try
        {
            if (result.equalsIgnoreCase("true"))
            {
                Intent intent = new Intent(context, nav_bar_main_menu.class);
                intent.putExtra("emailId",emailid);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
            else if(result.equalsIgnoreCase(("false")))
            {
                Toast.makeText(context,"Invalid email or password",Toast.LENGTH_SHORT).show();
            }
            else if (result.equalsIgnoreCase("exception") || result.equalsIgnoreCase("unsuccessful"))
            {
                Toast.makeText(context,"Oops something went wrong!!",Toast.LENGTH_SHORT).show();
            }
            else if (result.equalsIgnoreCase("email"))
            {
                Toast.makeText(context,"Enter the email address",Toast.LENGTH_SHORT).show();
            }
            else if (result.equalsIgnoreCase("password"))
            {
                Toast.makeText(context,"Enter password",Toast.LENGTH_SHORT).show();
            }
        }
        catch(Exception e){
            e.printStackTrace();
            Toast.makeText(context,"Oops something went wrong!!",Toast.LENGTH_SHORT).show();
        }

    }
}

