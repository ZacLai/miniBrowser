package com.training.android.minibrowser;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.widget.SimpleCursorAdapter;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import com.training.android.minibrowser.providers.MiniBookmarkProvider;

public class BookmarkActivity extends Activity {
    private static String TAG = "BookmarkActivity";
    private static ContentResolver mContRes;
    private SimpleCursorAdapter myAdapter = null;

    private ListView mBookmarkList;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bookmark);
        setupViewComponent();
        mContRes = getContentResolver();
    }

    private void setupViewComponent() {
        mBookmarkList = (ListView) findViewById(R.id.edtBookmark);
        mBookmarkList.setOnItemClickListener(listViewRegionOnItemClick);
        mBookmarkList.setOnItemLongClickListener(listViewRegionOnItemLongClick);
    }

    protected void onResume() {
        super.onResume();

        String[] projection = new String[] {
            "_id", "title", "URL"
        };

        Cursor c = mContRes.query(MiniBookmarkProvider.CONTENT_URI, projection, null, null, null);

        if (myAdapter == null) {
            myAdapter = new SimpleCursorAdapter(this, R.layout.listview, c, new String[] {
                "title", "URL"
            }, new int[] {
                R.id.title, R.id.url
            }, 0);
            mBookmarkList.setAdapter(myAdapter);
        } else {
            myAdapter.changeCursor(c);
        }
    }

    private AdapterView.OnItemClickListener listViewRegionOnItemClick = new AdapterView.OnItemClickListener() {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            // TODO Auto-generated method stub
            Intent intent = new Intent();
            TextView url = (TextView) view.findViewById(R.id.url);
            intent.putExtra("url", url.getText());
            BookmarkActivity.this.setResult(RESULT_OK, intent);
            finish();
        }
    };
    private AdapterView.OnItemLongClickListener listViewRegionOnItemLongClick = new AdapterView.OnItemLongClickListener() {

        @Override
        public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
            // TODO Auto-generated method stub
            //final View vw = view;
            final long ID = id;
            
            new AlertDialog.Builder(BookmarkActivity.this).setTitle("Delete Bookmark")
                    .setMessage("Are you sure?")
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            mContRes.delete(MiniBookmarkProvider.CONTENT_URI, "_id=\"" + ID + "\"",
                                    null);
                            // Log.d(TAG, "getActionBar()=" + getActionBar() +
                            // ", getTaskId()="
                            // + getTaskId() + ", getApplication()=" +
                            // getApplication()
                            // + ", vw.getId()=" + vw.getId() + ", id=" + ID);
                            Cursor c = mContRes.query(MiniBookmarkProvider.CONTENT_URI, null, null,
                                    null, null);
                            myAdapter.changeCursor(c);
                        }
                    }).setNegativeButton("No", new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // TODO Auto-generated method stub

                        }
                    }).show();
            return true;
        }
    };
}
