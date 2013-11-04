package com.training.android.minibrowser.providers;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class BookmarkDbHelper extends SQLiteOpenHelper {
    
    public String mCreateTableCommand;
    public BookmarkDbHelper(Context context, String name, CursorFactory factory, int version){
        super(context, name, factory, version);
        mCreateTableCommand = "";
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // TODO Auto-generated method stub
        if(mCreateTableCommand.isEmpty())   return;
        
        db.execSQL(mCreateTableCommand);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // TODO Auto-generated method stub
    }

}
