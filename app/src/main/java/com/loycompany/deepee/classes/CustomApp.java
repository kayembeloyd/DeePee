package com.loycompany.deepee.classes;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.loycompany.deepee.database.DeePeeDatabase;

import java.io.IOException;

public class CustomApp {
    // Helpers
    private DeePeeDatabase deePeeDatabase;
    private SQLiteDatabase mDb;

    private Context mContext;

    public int id;
    public String name;
    public int dataPlanID;

    public boolean isEnabled;
    public boolean isUnlimited;

    public float totalData;
    public float totalUsedData;

    public CustomApp(Context context) {
        this.mContext = context;

        deePeeDatabase = new DeePeeDatabase(context);

        try {
            deePeeDatabase.updateDataBase();
        } catch (IOException mIOException) {
            throw new Error("UnableToUpdateDatabase");
        }

        mDb = deePeeDatabase.getWritableDatabase();
    }

    boolean enable(){return true;}
    boolean disable(){return true;}

    boolean addData(float dataSize){return true;}
    boolean removeData(float dataSize){return true;}
    boolean setUnlimited(boolean isUnlimited){return true;}
    boolean resetData(){return true;}
    boolean startRecording(){return true;}
    boolean stopRecording(){return true;}

    int getDataPlan(){return 0;}
    boolean setDataPlan(int dataPlanID){return true;}

    public void save(){
        deePeeDatabase.saveCustomApp(this);
    }
    boolean delete(){
        return deePeeDatabase.deleteCustomApp(this.id);
    }

    boolean save(Context context){return true;}
}
