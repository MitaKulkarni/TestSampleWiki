package com.example.samplewiki.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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
        final EditText searchEditTv = (EditText) findViewById(R.id.activity_main_search_et);
        Button searchBtn = (Button) findViewById(R.id.activity_main_search_bt);
        RecyclerView searchRv = (RecyclerView) findViewById(R.id.activity_main_search_rv);
        mProgressBar = (ProgressBar) findViewById(R.id.activity_main_progress_bar);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        searchRv.setLayoutManager(linearLayoutManager);

        mSearchAdapter = new SearchResultsAdapter(this, mSearchList);
        searchRv.setAdapter(mSearchAdapter);

        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getResultsFromServer(searchEditTv.getText().toString());
            }
        });
    }

    private void getResultsFromServer(String searchText) {

        if (Utilities.isNetworkAvailable(MainActivity.this)) {
            mProgressBar.setVisibility(View.VISIBLE);
            String url = String.format(RequestURLs.WIKI_QUERY_URL, searchText);
            JsonObjectRequest addMenuTask = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject jsonObject) {
                    mProgressBar.setVisibility(View.VISIBLE);
                    mSearchList.clear();

                    /*
                    JSONObject queryJsonObject = jsonObject.optJSONObject(JsonKeys.query);
                    JSONArray pagesArray = queryJsonObject.optJSONArray(JsonKeys.pages); */

                    Gson gson = new Gson();
                    SearchResult searchResult = gson.fromJson(jsonObject.toString(), SearchResult.class);
                    SearchResult.SearchQuery searchQuery = searchResult.getQuery();
                    mSearchList = searchQuery.getPagesList();
                    mSearchAdapter.notifyDataSetChanged();
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError volleyError) {
                    mProgressBar.setVisibility(View.VISIBLE);
                    Utilities.showToast(MainActivity.this, getString(R.string.error_connecting));
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
