package com.example.travelliner.ui.home;

import android.app.DatePickerDialog;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import com.example.travelliner.R;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class HomeFragment extends Fragment implements AdapterView.OnItemSelectedListener, onTaskCompletion {

    onTaskCompletion mCompletion;
    View root;

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        mCompletion = this;
        ArrayList<String> text = new ArrayList<>();
        root = inflater.inflate(R.layout.fragment_home, container, false);

        calanderFunctionality(root);
        arrivalCountrySpinnerFunctionality(root);
        departureCountrySpinnerFunctionality(root);
        text.add("Select a city");
        departureSpinnerFunctionality(root, text);
        arrivalSpinnerFunctionality(root, text);

        return root;
    }
    public  void calanderFunctionality(View view)
    {
        final EditText arrivalDate = view.findViewById(R.id.arrivalDate);
        final EditText departDate = view.findViewById(R.id.departDate);
        final Calendar arrivalDateCalendar = Calendar.getInstance();
        final Calendar departDateCalendar = Calendar.getInstance();
        final SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yy", Locale.US);
        final String currentDateandTime = sdf.format(new Date());

        RadioButton radioButton = view.findViewById(R.id.radioButton);
        RadioButton radioButton2 = view.findViewById(R.id.radioButton2);

        radioButton.setChecked(true);

        radioButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                arrivalDate.setEnabled(true); } });


        radioButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                arrivalDate.setEnabled(false);
                arrivalDate.getText().clear(); } });


        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                arrivalDateCalendar.set(Calendar.YEAR, year);
                arrivalDateCalendar.set(Calendar.MONTH, monthOfYear);
                arrivalDateCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                arrivalDate.setText(sdf.format(arrivalDateCalendar.getTime())); }};

        arrivalDate.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new DatePickerDialog(getContext(), date, arrivalDateCalendar
                        .get(Calendar.YEAR), arrivalDateCalendar.get(Calendar.MONTH),
                        arrivalDateCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        final DatePickerDialog.OnDateSetListener date1 = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                departDateCalendar.set(Calendar.YEAR, year);
                departDateCalendar.set(Calendar.MONTH, monthOfYear);
                departDateCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                departDate.setText(sdf.format(departDateCalendar.getTime())); }};

        departDate.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new DatePickerDialog(getContext(), date1, departDateCalendar
                        .get(Calendar.YEAR), departDateCalendar.get(Calendar.MONTH),
                        departDateCalendar.get(Calendar.DAY_OF_MONTH)).show();

                String abc = departDate.getText().toString();
                if (abc.compareTo(currentDateandTime)<0)
                {
                    Toast.makeText(getActivity(), abc, Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    public void  arrivalCountrySpinnerFunctionality(View view)
    {
        Spinner mySpinner = (Spinner) view.findViewById(R.id.arrivalCountry);

        ArrayAdapter<String> myadpapter = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_item,getResources().getStringArray(R.array.country));

        myadpapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        mySpinner.setAdapter(myadpapter);

        mySpinner.setOnItemSelectedListener(this);
    }

    public void  departureCountrySpinnerFunctionality(View view)
    {
        Spinner mySpinner = (Spinner) view.findViewById(R.id.departureCountry);

        ArrayAdapter<String> myadpapter = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_item,getResources().getStringArray(R.array.country));

        myadpapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        mySpinner.setAdapter(myadpapter);

        mySpinner.setOnItemSelectedListener(this);
    }

    public void  departureSpinnerFunctionality(View view, ArrayList<String> result)
    {
        Spinner mySpinner = (Spinner) view.findViewById(R.id.depart);

        ArrayAdapter<String> myadpapter = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_item,result);

        myadpapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        mySpinner.setAdapter(myadpapter);

        mySpinner.setOnItemSelectedListener(this);
    }

    public void  arrivalSpinnerFunctionality(View view, ArrayList<String> result)
    {
        Spinner mySpinner = (Spinner) view.findViewById(R.id.arrival);

        ArrayAdapter<String> myadpapter = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_item,result);

        myadpapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        mySpinner.setAdapter(myadpapter);

        mySpinner.setOnItemSelectedListener(this);
    }

    class flightRetrival extends AsyncTask<String, String, ArrayList<String>>
    {
        private View view;
        public int flag;
        public flightRetrival(View view, int flag)
        {
            this.view = view;
            this.flag = flag;
        }

        protected void onPreExecute(){
        }

        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
        protected ArrayList<String> doInBackground(String... arg0)
        {
            ArrayList<String> airports = new ArrayList<>(); // Create an ArrayList object
            HttpURLConnection conn;
            try
            {
                String country = arg0[0];
                URL url = new URL("http://192.168.108.1:8080/scripts/flightRetrival.php");
                conn = (HttpURLConnection) url.openConnection();
                conn.setDoOutput(true);
                Uri.Builder builder = new Uri.Builder()
                        .appendQueryParameter("country", country);

                String query = builder.build().getEncodedQuery();
                OutputStream os = conn.getOutputStream();
                BufferedWriter writer = new BufferedWriter(
                        new OutputStreamWriter(os, StandardCharsets.UTF_8));

                assert query != null;
                writer.write( query );
                writer.flush();
                writer.close();
                os.close();
                conn.connect();
            }
            catch(Exception e)
            {
                e.printStackTrace();
                airports.add("exception");
                return airports;
            }
            try{
                int response_code = conn.getResponseCode();
                if (response_code == HttpURLConnection.HTTP_OK)
                {
                    // Read data sent from server
                    InputStream input = conn.getInputStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(input));

                    String line;
                    airports.add("Select a city");
                    while ((line = reader.readLine()) != null) {
                        airports.add(line);
                    }
                    // Pass data to onPostExecute method
                }
                else
                {
                    airports.add("unsuccessful");
                }
                return(airports);
            }
            catch(Exception e){
                e.printStackTrace();
                airports.add("exception");
                return airports;
            }
        }
        protected void onPostExecute(ArrayList<String> result)
        {
            mCompletion.onTaskCompleted(result, view, flag);
        }
    }

    @Override
    public void onTaskCompleted(ArrayList<String> value, View view, int flag)
    {
        if (flag==1)
        {
            arrivalSpinnerFunctionality(view, value);
        }
        else
        {
            departureSpinnerFunctionality(view, value);
        }
        Toast.makeText(getContext(),"Works Fine.",Toast.LENGTH_SHORT).show();
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String text = parent.getItemAtPosition(position).toString();

        if (position!=0)
        {
            Spinner ar = view.findViewById(R.id.arrival);
            Spinner dep = view.findViewById(R.id.depart);
            switch (parent.getId())
            {
                case R.id.arrivalCountry:
                    new flightRetrival(root, 1).execute(text);
                    break;
                case R.id.departureCountry:
                    new flightRetrival(root, 0).execute(text);
                    break;
                case R.id.arrival:
                case R.id.depart:
                    if (ar != null && dep!= null)
                    if (ar.getItemAtPosition(position).toString().equals(dep.getItemAtPosition(position).toString()))
                    {
                        Toast.makeText(getContext(),"Arrival and Departure can't be same.",Toast.LENGTH_SHORT).show();
                    }
                    break;

            }
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

}