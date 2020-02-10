package com.example.schoolapp;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import  java.io.File;
import  java.io.FileOutputStream;
import  java.io.IOException;
import  java.io.InputStream;
import  java.io.OutputStream;
import java.util.ArrayList;

public class DatabaseSqlHelper extends SQLiteOpenHelper {
    private static String DB_NAME = "location.db";
    private static  String DB_PATH = "";
    private static final  int DB_VERSION = 3;

    private SQLiteDatabase  mDataBase;
    private  final Context mContext;
    private  boolean mNeedUpdate = false;

    private static final String REGION_TABLE = "Regions";
    private static final String DISTRICT_TABLE = "Districts";
    private static final String WARD_TABLE = "Wards";

    private String selectedRegion ="Arusha Region";
    private String selectedDistrict ="Karatu District";
    private  String selectedWard;

    public void setSelectedRegion(String selectedRegion){
        this.selectedRegion = selectedRegion + " Region";

    }

    public void setSelectedDistrict(String selectedDistrict){
        this.selectedDistrict = selectedDistrict;
    }



    public DatabaseSqlHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);

        if (android.os.Build.VERSION.SDK_INT >= 17)
            DB_PATH = context.getApplicationInfo().dataDir + "/databases/";
        else
            DB_PATH = "/data/data/" + context.getPackageName() + "/databases/";
        this.mContext = context;

        copyDataBase();

        this.getReadableDatabase();
    }

    public void updateDataBase() throws IOException {
        if (mNeedUpdate) {
            File dbFile = new File(DB_PATH + DB_NAME);
            if (dbFile.exists())
                dbFile.delete();

            copyDataBase();

            mNeedUpdate = false;
        }
    }

    private boolean checkDataBase() {
        File dbFile = new File(DB_PATH + DB_NAME);
        return dbFile.exists();
    }

    private void copyDataBase() {
        if (!checkDataBase()) {
            this.getReadableDatabase();
            this.close();
            try {
                copyDBFile();
            } catch (IOException mIOException) {
                throw new Error("ErrorCopyingDataBase");
            }
        }
    }

    private void copyDBFile() throws IOException {
        InputStream mInput = mContext.getAssets().open(DB_NAME);
        //InputStream mInput = mContext.getResources().openRawResource(R.raw.info);
        OutputStream mOutput = new FileOutputStream(DB_PATH + DB_NAME);
        byte[] mBuffer = new byte[1024];
        int mLength;
        while ((mLength = mInput.read(mBuffer)) > 0)
            mOutput.write(mBuffer, 0, mLength);
        mOutput.flush();
        mOutput.close();
        mInput.close();
    }

    public boolean openDataBase() throws SQLException {
        mDataBase = SQLiteDatabase.openDatabase(DB_PATH + DB_NAME, null, SQLiteDatabase.CREATE_IF_NECESSARY);
        return mDataBase != null;
    }
    public ArrayList<String> getRegion(){
        String columns[] = {"region"};

        ArrayList<String> regions = new ArrayList<>();
        mDataBase = this.getReadableDatabase();
        Cursor cursor = mDataBase.query(REGION_TABLE,columns,null,null,null,null,null);
        int count = cursor.getColumnCount();
        String cursorString;

        try{
            if(cursor.moveToFirst()) {
                 do {
                     for (int i = 0; i< count; i++){
                         cursorString = cursor.getString(i);
                         regions.add(cursorString);
                        }
                     }while (cursor.moveToNext());
                 }
            }catch (Exception e){

            }finally {
                cursor.close();

            }

        mDataBase.close();
        return regions;
    }

    public ArrayList<String> getDistricts(){
        String columns[] = {"district"};
        String whereClause = "region = ?";
        String[] whereArgs = new String[] {" "};
        whereArgs[0] = selectedRegion;

        ArrayList<String> districts = new ArrayList<>();
        mDataBase = this.getReadableDatabase();
        Cursor cursor = mDataBase.query(DISTRICT_TABLE,columns,whereClause,whereArgs,null,null,null);
        int count = cursor.getColumnCount();
        String cursorString;

        try{
            if(cursor.moveToFirst()) {
                do {
                    for (int i = 0; i< count; i++){
                        cursorString = cursor.getString(i);
                        districts.add(cursorString);
                    }
                }while (cursor.moveToNext());
            }
        }catch (Exception e){

        }finally {
            cursor.close();

        }

        mDataBase.close();
        return districts;
    }

    public ArrayList<String> getWards(){
        String columns[] = {"ward"};
        String whereClause = "district= ?";
        String[] whereArgs = new String[] {" "};
        whereArgs[0] = selectedDistrict;

        ArrayList<String> wards = new ArrayList<>();
        mDataBase = this.getReadableDatabase();

        Cursor cursor = mDataBase.query(WARD_TABLE,columns,whereClause,whereArgs,null,null,null);
        int count = cursor.getColumnCount();
        String cursorString;

        try{
            if(cursor.moveToFirst()) {
                do {
                    for (int i = 0; i< count; i++){
                        cursorString = cursor.getString(i);
                        wards.add(cursorString);
                    }
                }while (cursor.moveToNext());
            }
        }catch (Exception e){

        }finally {
            cursor.close();

        }

        mDataBase.close();
        return wards;
    }

    @Override
    public synchronized void close() {
        if (mDataBase != null)
            mDataBase.close();
        super.close();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (newVersion > oldVersion)
            mNeedUpdate = true;
    }
}