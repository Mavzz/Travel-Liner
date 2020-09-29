package com.example.travelliner;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class signUpActivity extends AppCompatActivity {

    TextView login;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        context = getApplicationContext();
        login = findViewById(R.id.link_login);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        });
        calanderFunctionality(context);
    }

    public  void calanderFunctionality(final Context context)
    {
        final EditText birthDate = findViewById(R.id.birthDate);
        final Calendar arrivalDateCalendar = Calendar.getInstance();
        final SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yy", Locale.US);

        final DatePickerDialog.OnDateSetListener signUpDate = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                arrivalDateCalendar.set(Calendar.YEAR, year);
                arrivalDateCalendar.set(Calendar.MONTH, monthOfYear);
                arrivalDateCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                birthDate.setText(sdf.format(arrivalDateCalendar.getTime())); }};

        birthDate.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new DatePickerDialog(context, signUpDate, arrivalDateCalendar
                        .get(Calendar.YEAR), arrivalDateCalendar.get(Calendar.MONTH),
                        arrivalDateCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
    }
}