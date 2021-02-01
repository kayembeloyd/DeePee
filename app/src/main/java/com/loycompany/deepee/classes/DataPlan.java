package com.loycompany.deepee.classes;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.loycompany.deepee.MainActivity;
import com.loycompany.deepee.database.DeePeeDatabase;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class DataPlan {
    // Helpers
    // Database db;

    private DeePeeDatabase deePeeDatabase;
    private SQLiteDatabase mDb;

    private Context mContext;
    public enum DataPlanType {WIFI, MOBILE_DATA, WIFI_MOBILE_DATA}

    public int id;
    public String name;

    public float totalData;
    public float totalAssignedData;
    public float totalUsedData;
    public DataPlanType dataPlanType;

    public DateTime startDateTime;
    public DateTime endDateTime;

    public DataPlan(Context context) {
        this.mContext = context;
        deePeeDatabase = new DeePeeDatabase(context);

        try {
            deePeeDatabase.updateDataBase();
        } catch (IOException mIOException) {
            throw new Error("UnableToUpdateDatabase");
        }

        mDb = deePeeDatabase.getWritableDatabase();
    }

    boolean setStartDataTime(DateTime dateTime){return true;}
    boolean setEndDataTime(DateTime dateTime){return true;}
    boolean assignDataSize(float dataSize){return true;}

    public int save(){
        return deePeeDatabase.saveDataPlan(this);
    }

    boolean delete(){
        return deePeeDatabase.deleteDataPlan(this.id);
    }

    List<CustomApp> customApps(){return new ArrayList<>();}
    boolean setDataPlanType(DataPlanType dataPlanType){return true;}
}
