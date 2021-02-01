package com.loycompany.deepee.database;

import android.content.Context;
import android.database.Cursor;
import android.widget.Toast;

import com.loycompany.deepee.classes.CustomApp;
import com.loycompany.deepee.classes.DataPlan;
import com.loycompany.deepee.classes.DateTime;

import java.util.ArrayList;
import java.util.List;

public class DeePeeDatabase  extends DatabaseHelper{
    Context context;
    String sql_statement;

    public DeePeeDatabase(Context context) {
        super(context);
        this.context = context;
    }

    public DataPlan dataPlan(int id){
        String sql_statement = "SELECT * FROM DataPlans WHERE id = " + id;
        Cursor cursor =  getReadableDatabase().rawQuery(sql_statement, null);
        cursor.moveToFirst();

        if (cursor.getCount() > 0){
            DataPlan dataPlan = new DataPlan(this.context);

            dataPlan.name = cursor.getString(1);
            dataPlan.totalData = cursor.getFloat(2);
            dataPlan.totalAssignedData = cursor.getFloat(3);
            dataPlan.totalUsedData = cursor.getFloat(4);

            if (cursor.getString(5).equals("MOBILE_DATA")){
                dataPlan.dataPlanType = DataPlan.DataPlanType.MOBILE_DATA;
            } else if (cursor.getString(5).equals("WIFI")){
                dataPlan.dataPlanType = DataPlan.DataPlanType.WIFI;
            } else {
                dataPlan.dataPlanType = DataPlan.DataPlanType.WIFI_MOBILE_DATA;
            }

            DateTime dateTime = new DateTime();

            dataPlan.startDateTime = DateTime.parseData(cursor.getString(6));
            dataPlan.endDateTime = DateTime.parseData(cursor.getString(7));

            cursor.close();
            return dataPlan;
        } else {
            cursor.close();
            return null;
        }
    }

    public CustomApp customApp(int id){
        String sql_statement = "SELECT * FROM CustomApps WHERE id = " + id;
        Cursor cursor =  getReadableDatabase().rawQuery(sql_statement, null);
        cursor.moveToFirst();

        if (cursor.getCount() > 0){
            CustomApp customApp = new CustomApp(this.context);

            customApp.name = cursor.getString(1);
            customApp.dataPlanID = cursor.getInt(2);

            customApp.isEnabled = cursor.getInt(3) == 1;
            customApp.isUnlimited = cursor.getInt(4) == 1;

            customApp.totalData = cursor.getFloat(5);
            customApp.totalUsedData = cursor.getFloat(6);

            cursor.close();
            return customApp;
        } else {
            cursor.close();
            return null;
        }
    }

    public List<CustomApp> getCustomApps(){
        String sql_statement = "SELECT * FROM CustomApps";
        List<CustomApp> customApps = new ArrayList<>();

        Cursor cursor =  getReadableDatabase().rawQuery(sql_statement, null);
        cursor.moveToFirst();

        if (cursor.getCount() > 0){
            while(!cursor.isAfterLast()){
                CustomApp customApp = new CustomApp(this.context);

                customApp.name = cursor.getString(1);
                customApp.dataPlanID = cursor.getInt(2);

                customApp.isEnabled = cursor.getInt(3) == 1;
                customApp.isUnlimited = cursor.getInt(4) == 1;

                customApp.totalData = cursor.getFloat(5);
                customApp.totalUsedData = cursor.getFloat(6);

                customApps.add(customApp);
                cursor.moveToNext();
            }
        }

        cursor.close();
        return customApps;
    }

    public List<DataPlan> getDataPlans(){
        String sql_statement = "SELECT * FROM DataPlans";
        List<DataPlan> dataPlans = new ArrayList<>();

        Cursor cursor =  getReadableDatabase().rawQuery(sql_statement, null);
        cursor.moveToFirst();

        if (cursor.getCount() > 0){
            while(!cursor.isAfterLast()){
                DataPlan dataPlan = new DataPlan(this.context);

                dataPlan.name = cursor.getString(1);
                dataPlan.totalData = cursor.getFloat(2);
                dataPlan.totalAssignedData = cursor.getFloat(3);
                dataPlan.totalUsedData = cursor.getFloat(4);

                if (cursor.getString(5).equals("MOBILE_DATA")){
                    dataPlan.dataPlanType = DataPlan.DataPlanType.MOBILE_DATA;
                } else if (cursor.getString(5).equals("WIFI")){
                    dataPlan.dataPlanType = DataPlan.DataPlanType.WIFI;
                } else {
                    dataPlan.dataPlanType = DataPlan.DataPlanType.WIFI_MOBILE_DATA;
                }

                dataPlan.startDateTime = DateTime.parseData(cursor.getString(6));
                dataPlan.endDateTime = DateTime.parseData(cursor.getString(7));

                dataPlans.add(dataPlan);
                cursor.moveToNext();
            }

            cursor.close();
            return dataPlans;
        } else {
            cursor.close();
            return null;
        }
    }

    public boolean deleteCustomApp(int id){
        return true;
    }

    public boolean deleteDataPlan(int id){
        return true;
    }

    public boolean saveCustomApp(CustomApp customApp){

        sql_statement = "SELECT * FROM 'CustomApps' WHERE id = " + customApp.id;

        Cursor cursor = getReadableDatabase().rawQuery(sql_statement, null);

        int isEnabled;
        int isUnlimited;

        if (customApp.isEnabled) isEnabled = 1; else isEnabled = 0;
        if (customApp.isUnlimited) isUnlimited = 1; else isUnlimited = 0;

        if (cursor.getCount() > 0) {
            sql_statement = "UPDATE DataPlans SET " +
                    "name = '" + customApp.name + "'" +
                    "dataPlanID = " + customApp.dataPlanID +
                    "isEnabled = " + isEnabled +
                    "isUnlimited = " + isUnlimited +
                    "totalData = " + customApp.totalData + "" +
                    "totalUsedData = " + customApp.totalUsedData + "" +
                    "";
        } else {
            sql_statement = "INSERT INTO DataPlans (name, totalData, totalAssignedData, totalUsedData, dataPlanType, startDateTime, endDateTime) VALUES " +
                    "("
                    + customApp.name + ","
                    + customApp.dataPlanID + ","
                    + customApp.isEnabled + ","
                    + customApp.isUnlimited + ","
                    + customApp.totalData + ","
                    + customApp.totalUsedData +
                    ")";
        }
        getWritableDatabase().execSQL(sql_statement);

        cursor.close();
        return true;
    }

    public int saveDataPlan(DataPlan dataPlan){
        // Check if exists
        int lastInsertID;

        sql_statement = "SELECT * FROM 'DataPlans' WHERE id = " + dataPlan.id;

        Cursor cursor = getReadableDatabase().rawQuery(sql_statement, null);

        String dataPlanTypeString;
        if (dataPlan.dataPlanType == DataPlan.DataPlanType.MOBILE_DATA){
            dataPlanTypeString = "MOBILE_DATA";
        } else if(dataPlan.dataPlanType == DataPlan.DataPlanType.WIFI){
            dataPlanTypeString = "WIFI";
        } else {
            dataPlanTypeString = "WIFI_MOBILE_DATA";
        }

        if (cursor.getCount() > 0){
            cursor.close();
            // It exist
            // Update it
            sql_statement = "UPDATE DataPlans SET " +
                    "name = '" + dataPlan.name + "'" +
                    "totalData = " + dataPlan.totalData + "" +
                    "totalAssignedData = " + dataPlan.totalAssignedData + "" +
                    "totalUsedData = " + dataPlan.totalUsedData + "" +
                    "dataPlanType = '" + dataPlanTypeString + "'" +
                    "startDateTime = '" + dataPlan.startDateTime.toString() + "'" +
                    "endDateTime = '" + dataPlan.endDateTime.toString() + "'" +
                    "WHERE id = " + dataPlan.id;

            getWritableDatabase().execSQL(sql_statement);

            lastInsertID = dataPlan.id;
        } else {
            cursor.close();

            sql_statement = "INSERT INTO DataPlans (name, totalData, totalAssignedData, totalUsedData, dataPlanType, startDateTime, endDateTime) VALUES " +
                    "("
                    + "'" + dataPlan.name + "',"
                    + dataPlan.totalData + ","
                    + dataPlan.totalAssignedData + ","
                    + dataPlan.totalUsedData + ","
                    + "'" + dataPlanTypeString + "',"
                    + "'" + dataPlan.startDateTime.toString() + "',"
                    + "'" + dataPlan.endDateTime.toString() + "'" +
                    ")";

            getWritableDatabase().execSQL(sql_statement);

            sql_statement = "SELECT * FROM sqlite_sequence;";
            Cursor cursor1 = getReadableDatabase().rawQuery(sql_statement, null);

            if (cursor1.getCount() > 0){
                cursor1.moveToFirst();
                lastInsertID = cursor1.getInt(1);
            } else {
                lastInsertID = -1;
            }

            cursor1.close();

        }

        return lastInsertID;
    }
}
