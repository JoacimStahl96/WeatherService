package com.example.weatherservice;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.android.volley.toolbox.StringRequest;

import java.util.ArrayList;

public class DBSetupHelper extends SQLiteOpenHelper {

    private static int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "weather.db";

    // Static values for table
    private static final String TABLE_NAME = "weather_table";
    private static final String COLUMN_DATE = "date";
    private static final String COLUMN_CITY = "city";
    private static final String COLUMN_TEMP = "temperature";
    private static final String COLUMN_DESC = "description";

    public DBSetupHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // only runs when the db is created
    @Override
    public void onCreate(SQLiteDatabase db) {

        String createTableQuery = "CREATE TABLE "
                + TABLE_NAME +
                " (value_id INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COLUMN_DATE + " TEXT, "
                + COLUMN_CITY + " TEXT, "
                + COLUMN_TEMP + " TEXT, "
                + COLUMN_DESC + " TEXT);";

        db.execSQL(createTableQuery);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }


    public boolean addValues(String date, String city, String temperature, String description) {
        SQLiteDatabase db = this.getWritableDatabase();
        // sort of an intent for databases
        ContentValues contentValues = new ContentValues();

        contentValues.put("COLUMN_DATE", date);
        contentValues.put("COLUMN_CITY", city);
        contentValues.put("COLUMN_TEMP", temperature);
        contentValues.put("COLUMN_DESC", description);

        long valueId = db.insert(TABLE_NAME, null, contentValues);

        if (valueId == -1) {
            Log.d("DB", "addValues: That didn't work");
            return false;
        } else {
            Log.d("DB", "addValues: values inserted");
            return true;
        }
    }

    public ArrayList<Weatherbean> getValues(ArrayList<Weatherbean> valuesInDb) {
        SQLiteDatabase db = this.getWritableDatabase();

        String selectAllColumns = "SELECT " + COLUMN_DATE + ", " + COLUMN_CITY + ", " + COLUMN_TEMP + ", " + COLUMN_DESC + " FROM " + TABLE_NAME;

        Cursor cursor = db.rawQuery(selectAllColumns, null);

        while (cursor.moveToNext()) {
            String firstValue = cursor.getString(0);
            String secondValue = cursor.getString(1);
            String thirdValue = cursor.getString(2);
            String fourthValue = cursor.getString(3);

            Weatherbean bean = new Weatherbean();

            bean.setDate(firstValue);
            bean.setCity(secondValue);
            bean.setTemperature(thirdValue);
            bean.setDescription(fourthValue);

            valuesInDb.add(bean);
        }
        cursor.close();
        db.close();
        return valuesInDb;
    }

    public void entireTable() {
        SQLiteDatabase db = this.getWritableDatabase();
        String selectEntireTable = "SELECT * FROM " + TABLE_NAME;

        Cursor cursor = db.rawQuery(selectEntireTable, null);

        if (cursor.moveToFirst()) {


            while (cursor.moveToNext()) {

                int value_id = cursor.getInt(0);
                String firstValue = cursor.getString(1);
                String secondValue = cursor.getString(2);
                String thirdValue = cursor.getString(3);
                String fourthValue = cursor.getString(4);

            }

            cursor.close();
            db.close();
        }
    }

    public void deleteEverythingFromTable() {
        SQLiteDatabase db = this.getWritableDatabase();

        String deleteQuery = "DELETE from " + TABLE_NAME;
        db.delete(TABLE_NAME, null, null);
        db.execSQL(deleteQuery);
        db.close();
    }

}
