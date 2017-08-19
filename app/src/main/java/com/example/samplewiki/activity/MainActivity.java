package com.example.samplewiki.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.ProgressBar;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.samplewiki.Constants.RequestURLs;
import com.example.samplewiki.R;
import com.example.samplewiki.Utilities;
import com.example.samplewiki.adapter.SearchResultsAdapter;
import com.example.samplewiki.controller.AppController;
import com.example.samplewiki.model.SearchResult;
import com.example.samplewiki.model.SearchResult.SearchQuery.Pages;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();
    private ArrayList<Pages> mSearchList;
    private ProgressBar mProgressBar;
    private SearchResultsAdapter mSearchAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mSearchList = new ArrayList<>();
        final AutoCompleteTextView searchAutoCompleteTv = (AutoCompleteTextView) findViewById(R.id.activity_main_search_at);
        RecyclerView searchRv = (RecyclerView) findViewById(R.id.activity_main_search_rv);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        searchRv.setLayoutManager(linearLayoutManager);

        mSearchAdapter = new SearchResultsAdapter(this, mSearchList);
        searchRv.setAdapter(mSearchAdapter);

        mProgressBar = (ProgressBar) findViewById(R.id.activity_main_progress_bar);

        searchAutoCompleteTv.setThreshold(4);
        searchAutoCompleteTv.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                getResultsFromServer(searchAutoCompleteTv.getText().toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    private void getResultsFromServer(String searchText) {

        String newSearchString = searchText.replace(" ", "+");
        if (Utilities.isNetworkAvailable(MainActivity.this)) {
            mProgressBar.setVisibility(View.VISIBLE);
            String url = RequestURLs.WIKI_QUERY_URL + newSearchString + "&gpslimit=10";
            JsonObjectRequest searchResultsTask = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject jsonObject) {
                    mProgressBar.setVisibility(View.GONE);
                    mSearchList.clear();

                    Gson gson = new Gson();
                    SearchResult searchResult = gson.fromJson(jsonObject.toString(), SearchResult.class);
                    SearchResult.SearchQuery searchQuery = searchResult.getQuery();
                    if(searchQuery != null && searchQuery.getPagesList().size() > 0) {
                        mSearchList.addAll(searchQuery.getPagesList());
                        mSearchAdapter.notifyDataSetChanged();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError volleyError) {
                    mProgressBar.setVisibility(View.GONE);
                    Utilities.showToast(MainActivity.this, getString(R.string.error_connecting));
                }
            });

            searchResultsTask.setTag(TAG);
            searchResultsTask.setShouldCache(false);
            AppController.getInstance().setRetryPolicy(searchResultsTask);
            AppController.getInstance().addToRequestQueue(searchResultsTask);
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
