package com.example.schoolapp;

import android.app.DatePickerDialog;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.icu.util.Calendar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import java.io.IOException;
import  java.util.ArrayList;


public class RegisterActivity extends AppCompatActivity  implements View.OnClickListener{

    DatabaseHelper db;
    RadioButton radioBtn1, radioBtn2;
    RadioGroup mRadioGroup;
    EditText dateInput;
    Button submit;
    EditText editFirstName;
    EditText editMiddleName;
    EditText editLastName;
    EditText regNumberId;
    EditText editMail;
    EditText phoneNumber;
    EditText editPassword;
    EditText regionText, districtText, wardText;
    Spinner rSpinner;
    Spinner dSpinner;
    Spinner wSpinner;


    private DatabaseSqlHelper mDBHelper;
    private SQLiteDatabase mDb;
    DatePickerDialog datePickerDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        dateInput = (EditText)findViewById(R.id.date);
         final Calendar calendar = Calendar.getInstance();
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH);
                int day = calendar.get(Calendar.DAY_OF_MONTH);

                datePickerDialog = new DatePickerDialog(RegisterActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        dateInput.setText(dayOfMonth + "/" + month + "/" + year);
                    }
                }, year,month,day);

                dateInput.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        datePickerDialog.show();
                    }
                });


        submit = (Button) findViewById(R.id.btn3);


        // location spinners
        rSpinner = (Spinner) findViewById(R.id.reg_spinner);
        dSpinner = (Spinner) findViewById(R.id.dist_spinner);
        wSpinner = (Spinner) findViewById(R.id.ward_spinner);

        editFirstName = (EditText)findViewById(R.id.firstNam);
        editMiddleName = (EditText)findViewById(R.id.midNam);
        editLastName = (EditText)findViewById(R.id.lastNam);
        regNumberId = (EditText)findViewById(R.id.regNum);
        editMail = (EditText)findViewById(R.id.email);
        phoneNumber = (EditText)findViewById(R.id.phone);
        editPassword = (EditText)findViewById(R.id.password2);
        regionText = (EditText)findViewById(R.id.region);
        districtText = (EditText)findViewById(R.id.district);
        wardText = (EditText)findViewById(R.id.ward);


        //Database for region,ward and district
        mDBHelper = new DatabaseSqlHelper(this);
        db = new DatabaseHelper(this);

        try {
            mDBHelper.updateDataBase();
        }
            catch (IOException mIOException) {
                throw new Error("UnableToUpdateDatabase");
        }

        try {
            mDb = mDBHelper.getWritableDatabase();
        } catch (SQLException mSQLException) {
            throw mSQLException;
        }

        // input variables



        // radio button for male and female options
        mRadioGroup = (RadioGroup)findViewById(R.id.radio);
        radioBtn1 = (RadioButton)findViewById(R.id.radioB1);
        radioBtn2 = (RadioButton)findViewById(R.id.radioB2);

        //spinner for location
        final ArrayList<String> regionArray = new ArrayList<>();
        regionArray.addAll(mDBHelper.getRegion());

        final ArrayList<String> districtArray = new ArrayList<>();
        districtArray.addAll(mDBHelper.getDistricts());

        final ArrayList<String> wardArray = new ArrayList<>();
        wardArray.addAll(mDBHelper.getWards());

        final ArrayAdapter<String> wardAdapter = new ArrayAdapter<String>(this,R.layout.support_simple_spinner_dropdown_item,wardArray);
        wSpinner.setAdapter(wardAdapter);
        wSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        final ArrayAdapter<String> districtAdapter = new ArrayAdapter<String>(this,R.layout.support_simple_spinner_dropdown_item,districtArray);
             dSpinner.setAdapter(districtAdapter);
             dSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    String selectedDistrict = districtArray.get(position);
                    mDBHelper.setSelectedDistrict(selectedDistrict);
                    wardArray.clear();
                    wardArray.addAll(mDBHelper.getWards());
                    wardAdapter.notifyDataSetChanged();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        final ArrayAdapter<String> regionAdapter = new ArrayAdapter<String>(this,R.layout.support_simple_spinner_dropdown_item,regionArray);
        rSpinner.setAdapter(regionAdapter);
        rSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedRegion = regionArray.get(position);
                mDBHelper.setSelectedRegion(selectedRegion);
                districtArray.clear();
                districtArray.addAll(mDBHelper.getDistricts());
                districtAdapter.notifyDataSetChanged();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        submit.setOnClickListener(this);

// connecting with the databaseHelper class for register
        DatabaseHelper db= new DatabaseHelper(this);
        regNumberId.setText(db.registrationNumberGenerate());
        regNumberId.setEnabled(false);
        regNumberId.setTextColor(Color.BLACK);

        //Radio Button for male and Female selection
        mRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId){
                                    case R.id.radioB1:
                        Toast.makeText(getApplicationContext(),"Male", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.radioB2:
                        Toast.makeText(getApplicationContext(), "Female", Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        });

    }


    @Override
    public void  onClick(View v){

        String fName;
        String mName;
        String lName;
        String email;
        String mPhone;
        String password;
        String regNum;
        String reg;
        String dis;
        String ward;
        regNumberId.setText(db.registrationNumberGenerate());

// button submit for inserting data in the register activity
        switch (v.getId()) {
            case R.id.btn3:
                regNum = regNumberId.getText().toString();
                fName = editFirstName.getText().toString();
                lName = editLastName.getText().toString();
                mName = editMiddleName.getText().toString();
                email = editMail.getText().toString();
                password = editPassword.getText().toString();
                reg = regionText.getText().toString();
                dis = districtText.getText().toString();
                ward = wardText.getText().toString();
                mPhone = phoneNumber.getText().toString();

                if (regNum.equals("") | fName.equals("") | lName.equals("") | mName.equals("") | email.equals("") | mPhone.equals("") |  password.equals("") | reg.equals("") | dis.equals("") | ward.equals("")) {
                    Toast.makeText(this, "Please fill the fields", Toast.LENGTH_SHORT).show();
                } else {
                    db.insertData(regNum, fName, mName, lName, email, password, reg, dis, ward, mPhone);

                    editFirstName.setText("");
                    editLastName.setText("");
                    editMiddleName.setText("");
                    editMail.setText("");
                    editPassword.setText("");
                    regionText.setText("");
                    districtText.setText("");
                    wardText.setText("");
                    phoneNumber.setText("");
                    Toast.makeText(this,"saved successfully", Toast.LENGTH_SHORT).show();
                }break;
            }
        }
    }

