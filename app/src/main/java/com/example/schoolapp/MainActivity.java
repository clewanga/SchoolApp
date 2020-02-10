package com.example.schoolapp;


import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity {

    TextInputLayout Username;
    TextInputLayout Password;
    private Button regBtn;
    private  Button logBtn;
    private static final Pattern PASSWORD_PATTERN = Pattern.compile("^" + "(?=.*[0-9])" + "(?=.*[a-zA-Z])" + "(?=\\S+$)" + ".{6,}" + "$");

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//login layout
        Username = (TextInputLayout) findViewById(R.id.name1);
        Password = (TextInputLayout) findViewById(R.id.password1);
        regBtn = (Button)findViewById(R.id.btn2);
        logBtn = (Button)findViewById(R.id.btn1);



        regBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);

                builder.setTitle("Who to register");

                DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which){
                            case DialogInterface.BUTTON_POSITIVE:
                                Intent student = new Intent(MainActivity.this, RegisterActivity.class);
                                startActivity(student);
                                break;

                                case DialogInterface.BUTTON_NEGATIVE:
                                    Intent teacher = new Intent(MainActivity.this, TeacherRegisterActivity.class);
                                    startActivity(teacher);
                                    break;

                        }
                    }
                };
                builder.setPositiveButton("Student", dialogClickListener);
                builder.setNegativeButton("Teacher" , dialogClickListener);

                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }
        });


            logBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v){
                    String username = Username.getEditText().getText().toString().trim();
                    String password = Password.getEditText().getText().toString().trim();

                    if(username.equalsIgnoreCase("Admin") && password.equalsIgnoreCase("chii@")){
                        Intent intent = new Intent(MainActivity.this, StudentActivity.class);
                        startActivity(intent);
                    }else if(username.equalsIgnoreCase("Caro") && password.equalsIgnoreCase("cc@")){
                        Intent tech = new Intent(MainActivity.this, TeachersActivity.class);
                        startActivity(tech);
                    }
            }

            });


        }


    private  boolean validateUsername() {
         String nameInput = Username.getEditText().getText().toString().trim();

         if(nameInput.isEmpty()){
             Username.setError("failed");
             return false;
         }else{
             Username.setError(null);
             return true;
         }
    }


    private boolean validatePassword() {
        String passwordInput = Password.getEditText().getText().toString().trim();

        if(passwordInput.isEmpty()) {
            Password.setError("failed");
            return false;
        }else if (!PASSWORD_PATTERN.matcher(passwordInput).matches()){
            Password.setError("Weak password");
            return false;
        }else {
            Password.setError(null);
            return true;
        }
    }


    }


