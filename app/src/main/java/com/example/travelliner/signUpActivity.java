package com.example.travelliner;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.regex.Pattern;

public class signUpActivity extends AppCompatActivity {

    TextView login;
    EditText firstName, lastName, userName, emailId, password, retype_password, birthDate;
    Button createAccount;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        context = getApplicationContext();
        createAccount = findViewById(R.id.create_account);
        firstName = findViewById(R.id.firstName);
        lastName = findViewById(R.id.lastName);
        userName = findViewById(R.id.userName);
        emailId = findViewById(R.id.input_email);
        password  = findViewById(R.id.input_password);
        retype_password = findViewById(R.id.retype_password);
        birthDate = findViewById(R.id.birthDate);
        login = findViewById(R.id.link_login);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });
        calanderFunctionality();
    }

    public void signUp(View view)
    {
        String firstname = firstName.getText().toString();
        String lastname = lastName.getText().toString();
        String username = userName.getText().toString();
        String emailid = emailId.getText().toString();
        String passwd = password.getText().toString();
        String retypePassword = retype_password.getText().toString();
        String birthdate = birthDate.getText().toString();
        String regex = "^[\\w!#$%&'*+/=?`{|}~^-]+(?:\\.[\\w!#$%&'*+/=?`{|}~^-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,6}$";
        Pattern pattern = Pattern.compile(regex);

        if(firstname.isEmpty() || lastname.isEmpty() || username.isEmpty() || emailid.isEmpty() || passwd.isEmpty() || birthdate.isEmpty())
        {
            Toast.makeText(context,"Enter all the values",Toast.LENGTH_SHORT).show();
        }
        else
        {
            if (retypePassword.equals(passwd))
            {
                if (pattern.matcher(emailid).matches())
                {
                    new mysqlSaveActivity(context).execute(firstname, lastname, username, emailid, passwd, birthdate);
                }
                else
                {
                    Toast.makeText(context,"Email Format entered is wrong",Toast.LENGTH_SHORT).show();
                }
            }
            else
            {
                Toast.makeText(context,"Both passwords are not same",Toast.LENGTH_SHORT).show();
            }

        }
    }
    public  void calanderFunctionality()
    {
        final EditText birthDate = findViewById(R.id.birthDate);
        final Calendar birthDateCalender = Calendar.getInstance();
        final SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yy", Locale.US);
        final String currentDateandTime = sdf.format(new Date());

        final DatePickerDialog.OnDateSetListener signUpDate = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                birthDateCalender.set(Calendar.YEAR, year);
                birthDateCalender.set(Calendar.MONTH, monthOfYear);
                birthDateCalender.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                birthDate.setText(sdf.format(birthDateCalender.getTime())); }};

        birthDate.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new DatePickerDialog(signUpActivity.this, signUpDate, birthDateCalender
                        .get(Calendar.YEAR), birthDateCalender.get(Calendar.MONTH),
                        birthDateCalender.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
    }
}