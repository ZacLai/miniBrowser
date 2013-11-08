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
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import com.training.android.minibrowser.providers.MiniBookmarkProvider;

public class MainActivity extends Activity {
    private Button mBtnOpenUrl;
    private EditText mEdtUrl;
    private WebView mWebView;
    private static ContentResolver mContRes;
    private static WebSettings mWebSet;
    private static boolean mCheckClick;
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
        mWebSet.setJavaScriptEnabled(checkJS());
    }

    @SuppressLint("SetJavaScriptEnabled")
    private void setupViewComponent() {
        mBtnOpenUrl = (Button) findViewById(R.id.btnOpenUrl);
        mEdtUrl = (EditText) findViewById(R.id.edtUrl);
        mWebView = (WebView) findViewById(R.id.webView);

        mWebSet = mWebView.getSettings();
        mWebView.setInitialScale(35);
        mWebSet.setUseWideViewPort(true);
        // mWebView.getSettings().setLayoutAlgorithm(LayoutAlgorithm.SINGLE_COLUMN);
        mWebSet.setBuiltInZoomControls(true);
        mWebSet.setDisplayZoomControls(false);
        mWebView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }

            public void onLoadResource(WebView view, String url) {
                mEdtUrl.setText(view.getTitle());
                mCheckClick = true;
            }
        });
        mWebSet.setJavaScriptEnabled(true);
        mBtnOpenUrl.setOnClickListener(btnClickLisOpenUrl);
        mEdtUrl.setOnClickListener(urlClickLisOpenUrl);
    }

    private Button.OnClickListener btnClickLisOpenUrl = new Button.OnClickListener() {
        public void onClick(View v) {
            Log.d(TAG, mEdtUrl.getText().toString() + " | JS: " + checkJS());
            mWebView.loadUrl(mEdtUrl.getText().toString());
        }
    };
    private EditText.OnClickListener urlClickLisOpenUrl = new EditText.OnClickListener() {
        public void onClick(View v) {
            if (mCheckClick) {
                mEdtUrl.setText(mWebView.getUrl());
                mEdtUrl.setSelection(7, mWebView.getUrl().length());
                mCheckClick = false;
            }
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
                     * 這裡的selection相當於SQL語法中的WHERE
                     */
                    Cursor c = mContRes.query(MiniBookmarkProvider.CONTENT_URI, null, selection,
                            null, null);
                    if (c.getCount() > 0) {
                        mContRes.update(MiniBookmarkProvider.CONTENT_URI, newRow, selection, null);
                    } else {
                        mContRes.insert(MiniBookmarkProvider.CONTENT_URI, newRow);
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
            String url = data.getExtras().getString("url");
            // Log.d(TAG, "url = "+url);
            mWebView.loadUrl(url);
        }
    }
}
