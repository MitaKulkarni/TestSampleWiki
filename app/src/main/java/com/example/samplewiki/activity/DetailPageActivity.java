package com.example.samplewiki.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.samplewiki.Constants.JsonKeys;
import com.example.samplewiki.Constants.RequestURLs;
import com.example.samplewiki.Constants.StringConstants;
import com.example.samplewiki.R;
import com.example.samplewiki.Utilities;
import com.example.samplewiki.controller.AppController;

import org.json.JSONException;
import org.json.JSONObject;

public class DetailPageActivity extends AppCompatActivity {

    private static final String TAG = DetailPageActivity.class.getSimpleName();
    private ProgressBar mProgressBar;
    private WebView mWebView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_page);

        String pageId = getIntent().getStringExtra(StringConstants.PAGE_ID);
        mProgressBar = (ProgressBar) findViewById(R.id.activity_detail_page_progress_bar);
        mWebView = (WebView) findViewById(R.id.activity_detail_page_webview);
        mWebView.getSettings().setLoadsImagesAutomatically(true);
        mWebView.setWebViewClient(new WebViewClient(){
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                return true;
            }
        });

        getDetailPageUrl(pageId);

    }

    private void getDetailPageUrl(final String pageId) {
        if (Utilities.isNetworkAvailable(this)) {
            mProgressBar.setVisibility(View.VISIBLE);
            String url = String.format(RequestURLs.WIKI_PAGE_URL, pageId);
            JsonObjectRequest addMenuTask = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject jsonObject) {
                    mProgressBar.setVisibility(View.VISIBLE);

                    try {
                        JSONObject queryJsonObject = jsonObject.getJSONObject(JsonKeys.QUERY);
                        JSONObject pagesJsonObject = queryJsonObject.getJSONObject(JsonKeys.PAGES);
                        JSONObject customJsonObject = pagesJsonObject.getJSONObject(pageId);
                        String fullUrl = customJsonObject.optString(JsonKeys.FULL_URL);
                        mWebView.loadUrl(fullUrl);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError volleyError) {
                    mProgressBar.setVisibility(View.VISIBLE);
                    Utilities.showToast(DetailPageActivity.this, getString(R.string.error_connecting));
                }
            });

            addMenuTask.setTag(TAG);
            addMenuTask.setShouldCache(false);
            AppController.getInstance().setRetryPolicy(addMenuTask);
            AppController.getInstance().addToRequestQueue(addMenuTask);
        } else {
            Utilities.showToast(this, getString(R.string.no_internet_connection_error));
        }
    }

    @Override
    protected void onDestroy() {
        AppController.getInstance().cancelPendingRequests(TAG);
        super.onDestroy();
    }
}
