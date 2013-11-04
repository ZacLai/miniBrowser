package com.training.android.minibrowser.providers;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.util.Log;

public class MiniBookmarkProvider extends ContentProvider {
    private static final String TAG = "ContentProvider";
    private static final String AUTHORITY = "com.training.android.minibrowser.providers";
    private static final String DB_FILE = "minibookmarks.db", DB_TABLE = "minibookmarks";
    private static final int DB_TABLE_MINIBOOKMARKS = 1;
    public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + DB_TABLE);
    private static final UriMatcher mUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
    static {
        mUriMatcher.addURI(AUTHORITY, DB_TABLE, DB_TABLE_MINIBOOKMARKS);
    }
    private SQLiteDatabase mBookmarkDb;

    @Override
    public boolean onCreate() {
        // TODO Auto-generated method stub
        BookmarkDbHelper bookmarkDbHp = new BookmarkDbHelper(getContext(), DB_FILE, null, 1);

        bookmarkDbHp.mCreateTableCommand = "CREATE TABLE " + DB_TABLE + "(_id INTEGER PRIMARY KEY AUTOINCREMENT,"
                + "title TEXT," + "URL varchar(500));";
        
        mBookmarkDb = bookmarkDbHp.getWritableDatabase();
        
        return true;
    }

    @Override
    public String getType(Uri uri) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        // TODO Auto-generated method stub
        if (mUriMatcher.match(uri) != DB_TABLE_MINIBOOKMARKS) {
            throw new IllegalArgumentException("Unknown URI " + uri);
        }
        //mBookmarkDb.delete(DB_TABLE, selection, selectionArgs);
        Log.d(TAG, "1: "+DB_TABLE+", 2: "+selection+" , 3: "+selectionArgs);
        return mBookmarkDb.delete(DB_TABLE, selection, selectionArgs);
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs,
            String sortOrder) {
        // TODO Auto-generated method stub
        if(mUriMatcher.match(uri) != DB_TABLE_MINIBOOKMARKS){
            throw new IllegalArgumentException("Unknown Uri " +uri);
        }
        
        /*
         *   query的第一個參數boolean distinct
         *   true: 代表過濾掉相同的值, false反之
         *   所以當distinct為true
         *   即使在相同網頁連續add兩次
         *   因為rowId不同，所以在sqlite看會有兩筆data
         *   但query出來卻只會有一筆data
         */
        Cursor c = mBookmarkDb.query(true, DB_TABLE, projection, selection, null, null, null, null, null);        
        c.setNotificationUri(getContext().getContentResolver(), uri);
        
        return c;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        //Log.d(TAG, "this is insert!!!");
        // TODO Auto-generated method stub
        if (mUriMatcher.match(uri) != DB_TABLE_MINIBOOKMARKS) {
            throw new IllegalArgumentException("Unknown URI " + uri);
        }

        long rowId = mBookmarkDb.insert(DB_TABLE, null, values);
        Uri insertedRowUri = ContentUris.withAppendedId(CONTENT_URI, rowId);

        getContext().getContentResolver().notifyChange(insertedRowUri, null);

        return insertedRowUri;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        //Log.d(TAG, "this is update!!!");
        // TODO Auto-generated method stub
        return mBookmarkDb.update(DB_TABLE, values, selection, selectionArgs);
        //return 0;
    }

}
