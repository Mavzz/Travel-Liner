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

public class mysqlSaveActivity extends AsyncTask<String,String,String> {

    @SuppressLint("StaticFieldLeak")
    private Context context;
    boolean flag;

    public mysqlSaveActivity(Context context, boolean flag)
    {
        this.context = context;
        this.flag = flag;
    }

    protected void onPreExecute(){
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    protected String doInBackground(String... arg0) {
        HttpURLConnection conn;
            try
            {
                if (flag)
                {
                    String firstname = arg0[0];
                    String lastname = arg0[1];
                    String username = arg0[2];
                    String emailid = arg0[3];
                    String passwd = arg0[4];
                    String birthdate = arg0[5];

                    URL url = new URL("http://192.168.108.1:8080/scripts/saveUser.php");
                    conn = (HttpURLConnection) url.openConnection();
                    conn.setDoOutput(true);
                    Uri.Builder builder = new Uri.Builder()
                            .appendQueryParameter("first_name", firstname)
                            .appendQueryParameter("last_name", lastname)
                            .appendQueryParameter("user_name", username)
                            .appendQueryParameter("emailid", emailid)
                            .appendQueryParameter("password", passwd)
                            .appendQueryParameter("birth_date", birthdate);

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
                else
                {
                    String id = arg0[0];
                    String emailid = arg0[1];
                    String username = arg0[2];

                    URL url = new URL("http://192.168.108.1:8080/scripts/saveUserGoogle.php");
                    conn = (HttpURLConnection) url.openConnection();
                    conn.setDoOutput(true);
                    Uri.Builder builder = new Uri.Builder()
                            .appendQueryParameter("id", id)
                            .appendQueryParameter("emailid", emailid)
                            .appendQueryParameter("user_name", username);

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

    @SuppressLint("SetTextI18n")
    protected void onPostExecute(String result){
        try
        {
            if (result.equalsIgnoreCase("true"))
            {
                Intent intent = new Intent(context, nav_bar_main_menu.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
            else if(result.equalsIgnoreCase("Email exists"))
            {
                Toast.makeText(context,"Email already exists use another email",Toast.LENGTH_SHORT).show();
            }
            else if(result.equalsIgnoreCase("Username exists"))
            {
                Toast.makeText(context,"Username already exists use another username",Toast.LENGTH_SHORT).show();
            }
            else
            {
                Toast.makeText(context,"Something went wrong",Toast.LENGTH_SHORT).show();
            }
        }
        catch(Exception e){
            e.printStackTrace();
            Toast.makeText(context,"Oops something went wrong!!",Toast.LENGTH_SHORT).show();
        }

    }
}
