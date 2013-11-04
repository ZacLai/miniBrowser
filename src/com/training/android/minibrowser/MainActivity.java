package com.training.android.minibrowser;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import com.training.android.minibrowser.providers.MiniBookmarkProvider;

public class MainActivity extends Activity {
    private static ContentResolver mContRes;

    private Button mBtnOpenUrl;
    private EditText mEdtUrl;
    private WebView mWebView;
    private static final int MENU_SETTING = Menu.FIRST;
    private static final int MENU_ADD = Menu.FIRST + 1;
    private static final int MENU_BOOKMARK = Menu.FIRST + 2;
    private static final int MENU_QUIT = Menu.FIRST + 3;
    private static final String TAG = "MainDebug";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setupViewComponent();
        mContRes = getContentResolver();
    }

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onResume() {
        super.onResume();
        mWebView.getSettings().setJavaScriptEnabled(checkJS());
    }

    @SuppressLint("SetJavaScriptEnabled")
    private void setupViewComponent() {
        mBtnOpenUrl = (Button) findViewById(R.id.btnOpenUrl);
        mEdtUrl = (EditText) findViewById(R.id.edtUrl);
        mWebView = (WebView) findViewById(R.id.webView);
        mWebView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });
        mWebView.getSettings().setJavaScriptEnabled(true);
        mBtnOpenUrl.setOnClickListener(btnClickLisOpenUrl);
    }

    private Button.OnClickListener btnClickLisOpenUrl = new Button.OnClickListener() {
        public void onClick(View v) {
            Log.d(TAG, mEdtUrl.getText().toString() + " | JS: " + checkJS());
            mWebView.loadUrl(mEdtUrl.getText().toString());
        }
    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add(0, MENU_SETTING, 0, "Setting");
        menu.add(0, MENU_ADD, 0, "Add Bookmark");
        menu.add(0, MENU_BOOKMARK, 0, "Show Bookmarks");
        menu.add(0, MENU_QUIT, 0, "Exit");
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case MENU_SETTING:
                Intent intent1 = new Intent(MainActivity.this, PreActivity.class);
                startActivity(intent1);
                break;
            case MENU_ADD:
                if (mWebView.getTitle() != null) {
                    String selection = "title=\'" + mWebView.getTitle() + "\'";
                    // Log.d(TAG, "selection = "+selection);
                    ContentValues newRow = new ContentValues();
                    newRow.put("title", mWebView.getTitle());
                    newRow.put("URL", mWebView.getUrl());
                    
                    /*
                     *   這裡的selection相當於SQL語法中的WHERE
                     */
                    Cursor c = mContRes.query(MiniBookmarkProvider.CONTENT_URI, null, selection,
                            null, null);
                    if (c.getCount() > 0) {
                        mContRes.update(MiniBookmarkProvider.CONTENT_URI, newRow, selection, null);
                        // Log.d(TAG, "go update = "+c.getCount());
                    } else {
                        mContRes.insert(MiniBookmarkProvider.CONTENT_URI, newRow);
                        // Log.d(TAG, "go insert = "+c.getCount());
                        //nononono
                    }
                }
                break;
            case MENU_BOOKMARK:
                Intent intent2 = new Intent(MainActivity.this, BookmarkActivity.class);
                startActivityForResult(intent2, 1);
                break;
            case MENU_QUIT:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private boolean checkJS() {
        return MyFragment.getCheckboxPref(this);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != 0) {
            String result = data.getExtras().getString("uri");
            String uri = result.substring(result.indexOf("\n") + 5);
            // Log.i(TAG, uri);
            mEdtUrl.setText(uri);
            mWebView.loadUrl(uri);
        }
    }
}
