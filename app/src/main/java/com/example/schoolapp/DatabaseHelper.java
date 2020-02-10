package com.example.schoolapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.Calendar;


public class DatabaseHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "SchoolApp.db";
    public static final String TABLE_NAME = "student_table";
    public static final String COL1 = "RegistrationNumber";
    public static final String COL2 = "FirstName";
    public static final String COL3 = "MiddleName";
    public static final String COL4 = "LastName";
    public static final String COL5 = "EmailAddress";
    public static final String COL6 = "Region";
    public static final String COL7 = "District";
    public static final String COL8 = "Ward";
    public static final String COL9 = "Password";
    public static final String C0L10 = "PhoneNumber";


    public static final  String STAFF_TABLE = "staff";
    public static final  String KEY_ID = "staff_id";


    SQLiteDatabase db;
    public String studentOrTeacher= "student";



    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 11);

    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String Query_Table=" CREATE table " + TABLE_NAME + "(" +COL1+ " TEXT , "+COL2+ " TEXT, "+COL3+ " TEXT," +COL4+
                " TEXT,"+COL5+ " TEXT,"+COL6+ " TEXT," +COL7+ " TEXT," +COL8+ " TEXT," +COL9+ " TEXT," +C0L10+ " TEXT);";
        db.execSQL(Query_Table);

        String Query="CREATE TABLE " + STAFF_TABLE + "(" +KEY_ID+ " TEXT , "+COL2+ " TEXT, "+COL3+ " TEXT," +COL4+
                " TEXT,"+COL5+ " TEXT,"+COL6+ " TEXT," +COL7+ " TEXT," +COL8+ " TEXT," +COL9+ " TEXT," +C0L10+ " TEXT);";
        db.execSQL(Query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + STAFF_TABLE);
        onCreate(db);

    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        super.onDowngrade(db, oldVersion, newVersion);
    }

    public long insertData(String regNum,String fName, String mName, String lName, String email, String password, String reg, String dis, String ward,String mPhone) {
        db = this.getWritableDatabase();
        String name_table= TABLE_NAME;

        ContentValues contentValues = new ContentValues();
        if (studentOrTeacher=="student"){
            contentValues.put(COL1, regNum);
        }else {
            contentValues.put(KEY_ID,regNum);
            name_table = STAFF_TABLE;
        }
        contentValues.put(COL2, fName);
        contentValues.put(COL3, mName);
        contentValues.put(COL4, lName);
        contentValues.put(COL5, email);
        contentValues.put(COL6, reg);
        contentValues.put(COL7, dis);
        contentValues.put(COL8, ward);
        contentValues.put(COL9, password);
        contentValues.put(C0L10, mPhone);


        return db.insert(name_table, null, contentValues);
    }

    public String getData() {
        db=this.getReadableDatabase();
        String[] columns = new String[]{COL1, COL2, COL3, COL4, COL6, COL5, COL7, COL8, COL9, C0L10,KEY_ID};
        Cursor cursor = db.query(TABLE_NAME, columns, null, null, null, null, null, null);
        int iFName = cursor.getColumnIndex(COL2);
        int iMName = cursor.getColumnIndex(COL3);
        int iLName = cursor.getColumnIndex(COL4);
        int iRegNum = cursor.getColumnIndex(COL1);
        int iEmail = cursor.getColumnIndex(COL5);
        int iReg = cursor.getColumnIndex(COL6);
        int iDis = cursor.getColumnIndex(COL7);
        int iWard = cursor.getColumnIndex(COL8);
        int iPassword = cursor.getColumnIndex(COL9);
        int iPhone = cursor.getColumnIndex(C0L10);
        int iStaffNum = cursor.getColumnIndex(KEY_ID);
        String result = "";

        for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
            result = result +
                    "fName: " + cursor.getString(iFName) + "\n" +
                    "lName: " + cursor.getString(iLName) + "\n" +
                    "mName: " + cursor.getString(iMName) + "\n" +
                    "regNum: " + cursor.getString(iRegNum) + "\n" +
                    "email: " + cursor.getString(iEmail) + "\n" +
                    "reg: " + cursor.getString(iReg) + "\n" +
                    "dis: " + cursor.getString(iDis) + "\n" +
                    "ward: " + cursor.getString(iWard) + "\n" +
                    "mPhone: " + cursor.getString(iPhone) + "\n" +
                    "password: " + cursor.getString(iPassword) + "\n" +
                    "staffNum: " + cursor.getString(iStaffNum) + "\n\n";
        }
        db.close();
        return result;
    }

    public void deleteData(long l) {
        db = this.getWritableDatabase();
        db.delete(TABLE_NAME, COL1 + "=" +l, null);
    }

    public void updateData(long l, String fName, String lName, String mName, String email, String reg, String dis, String ward, String password, String mPhone) {
        db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL1, fName);
        values.put(COL2, mName);
        values.put(COL3, lName);
        values.put(COL5, email);
        values.put(COL6, reg);
        values.put(COL7, dis);
        values.put(COL8, ward);
        values.put(COL9, password);
        values.put(C0L10, mPhone);
        db.update(TABLE_NAME, values, COL1+"="+l, null);
        db.close();

    }

    public String getFirstName(long l1) {
        db=this.getReadableDatabase();
        String[] columns = new String[]{COL1, COL2, COL3, COL4, COL5, COL6, COL9, COL7, COL8, C0L10};
        Cursor cursor = db.query(TABLE_NAME, columns, COL1 + "=" + l1, null, null, null, null, null);
        try {
            if (cursor != null) {
                cursor.moveToFirst();
                String fName = cursor.getString(1);
                return fName;
            }
        }catch (Exception e){

        }finally {
            cursor.close();
        }
        return null;
    }

    public String getMiddleName(long l1) {
        db=this.getReadableDatabase();
        String[] columns = new String[]{COL1, COL2, COL3, COL4, COL5, COL6, COL9, COL7, COL8, C0L10};
        Cursor cursor = db.query(TABLE_NAME, columns, COL1 + "=" +l1, null, null, null, null);
       try{
           if (cursor != null) {
               cursor.moveToFirst();
               String fName= cursor.getString(2);
               return fName;
           }
       }catch (Exception e){}
       finally {
           cursor.close();
       }

        return null;
    }

    public String getLastName(long l1) {
        db=this.getReadableDatabase();
        String[] columns = new String[]{COL1, COL2, COL3, COL4, COL5, COL6, COL9, COL7, COL8, C0L10};
        Cursor cursor = db.query(TABLE_NAME, columns, COL1 + "=" +l1, null, null, null, null);
       try {
           if (cursor != null) {
               cursor.moveToFirst();
               String fName = cursor.getString(3);
               return fName;
           }
       }catch (Exception e){}
        finally {
           cursor.close();
       }
        return null;
    }

    int assignedRegistrationNumber = 0;
    public  String registrationNumberGenerate(){
        int year = Calendar.getInstance().get(Calendar.YEAR);
        int min = 0;
        int max = 99999;
        ;
        String columns[]= {"Student"};
        columns[0] = COL1;
        String table_name = TABLE_NAME;
        int registrationNumberFromCursorString;

        if(studentOrTeacher.equalsIgnoreCase("student")){
            table_name = TABLE_NAME;
            columns[0] = COL1;
        }else {
            table_name = STAFF_TABLE;
            columns[0] = KEY_ID;
        }
        db = this.getReadableDatabase();
        int count;
        String cursorString = null;
        Cursor cursor;

        try {
            cursor = db.query(table_name,columns,null,null,null,null,null);

            count = cursor.getColumnCount();


            if (cursor.moveToFirst()) {
               cursorString = cursor.getString(count-1);
            }
            cursor.close();

        } catch (Exception e){

             } finally {

        }


        if(cursorString != null) {
            cursorString = cursorString.substring(8);
            registrationNumberFromCursorString = Integer.parseInt(cursorString);
            registrationNumberFromCursorString++;
            assignedRegistrationNumber = registrationNumberFromCursorString;

        }else {
            assignedRegistrationNumber++;
        }

        db.close();

        String regNumberString = Integer.toString(year) + "-04-" + String.format("%05d",assignedRegistrationNumber);
        return regNumberString;
    }



}

