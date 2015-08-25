package com.snumap.snumapversion1;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;

import java.util.ArrayList;

/**
 * Created by rukeon01 on 2015-07-14.
 */
public class MyDatabase extends SQLiteAssetHelper {
    private static final String DATABASE_NAME = "Data.db";
    private static final int DATABASE_VERSION = 1;

    // table details
    public String tableName = "build";
    public String fieldObjectId = "_id";
    public String fieldObjectName = "name";
    public String fieldObjectNumber = "number";
    public String fieldObjectLatitude = "latitude";
    public String fieldObjectLongitude = "longitude";

    public MyDatabase(Context context)
    {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
//        setForcedUpgrade();
    }

    // Read records related to the search term
    public ArrayList<String> read(String searchTerm) {

        // select query
        String sql = "";
        sql += "SELECT * FROM " + tableName;
        sql += " WHERE " + fieldObjectId + " LIKE '%" + searchTerm + "%'";
        sql += " ORDER BY " + fieldObjectName + " ASC";
        sql += " LIMIT 0,5";

        SQLiteDatabase db = this.getWritableDatabase();

        // execute the query
        Cursor cursor = db.rawQuery(sql, null);

        ArrayList<String> recordsList = new ArrayList<String>();

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            int x = 0;
            do {
                // int productId = Integer.parseInt(cursor.getString(cursor.getColumnIndex(fieldProductId)));
                String objectName = cursor.getString(cursor.getColumnIndex(fieldObjectId));

                // add to list
                recordsList.add(x, objectName);
                x++;
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();

        // return the list of records
        Log.e("크기다!!!!!!!!", Integer.toString(recordsList.size()));
        return recordsList;
    }

    public DataSet selectDatabyId(String index){
        String sql = "select * from " + tableName+ " where _id = ?; ";

        SQLiteDatabase db = this.getWritableDatabase();

        // execute the query
        Cursor cursor = db.rawQuery(sql, new String[] {index});

        DataSet result = new DataSet();

        // result(Cursor 객체)가 비어 있으면 false 리턴
        if(cursor.moveToFirst()){
            result.set_id(cursor.getString(cursor.getColumnIndex(fieldObjectId)));
            Log.e("과연 있을까?", cursor.getString(cursor.getColumnIndex(fieldObjectId)));
            result.setName(cursor.getString(cursor.getColumnIndex(fieldObjectName)));
            result.setNumber(cursor.getString(cursor.getColumnIndex(fieldObjectNumber)));
            Log.e("과연 있을까?", cursor.getString(cursor.getColumnIndex(fieldObjectLatitude)));
            result.setLatitude(cursor.getString(cursor.getColumnIndex(fieldObjectLatitude)));
            result.setLongitude(cursor.getString(cursor.getColumnIndex(fieldObjectLongitude)));
        }
        cursor.close();
        db.close();

        return result;
    }

    public ArrayList<String> insertBuildingName()
    {
        ArrayList<String> result = new ArrayList<String>();

        String sql = "select _id from " + tableName + "; ";

        SQLiteDatabase db = this.getWritableDatabase();

        // execute the query
        Cursor cursor = db.rawQuery(sql, new String[] {});

        // result(Cursor 객체)가 비어 있으면 false 리턴
        if(cursor.moveToFirst()){
            do {
                result.add(cursor.getString(cursor.getColumnIndex(fieldObjectId)));
//            Log.e("과연 있을까?", cursor.getString(cursor.getColumnIndex(fieldObjectId)));
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();

        return result;
    }



//    public Cursor getEmployees() {
//
//        SQLiteDatabase db = getReadableDatabase();
//        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
//
//        String [] sqlSelect = {"_id", "name", "number"};
//        String sqlTables = "Build";
//
//        qb.setTables(sqlTables);
//        Cursor c = qb.query(db, sqlSelect, null, null,
//                null, null, null);
//
//        c.moveToFirst();
//        return c;
//    }


}


