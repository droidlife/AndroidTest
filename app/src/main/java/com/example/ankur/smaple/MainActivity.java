package com.example.ankur.smaple;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ankur.smaple.utilities.NetworkUtilities;

import java.io.IOException;
import java.net.URL;

public class MainActivity extends AppCompatActivity {
    EditText mSearchBoxEditText;
    TextView mUrlDisplay;
    TextView mSearchResults;
    TextView errorMessageTextView;
    ProgressBar mProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mSearchBoxEditText = findViewById(R.id.et_search_box);
        mUrlDisplay = findViewById(R.id.tv_url_display);
        mSearchResults = findViewById(R.id.tv_github_search_result_json);
        errorMessageTextView = findViewById(R.id.tv_error_display);
        mProgressBar = findViewById(R.id.pb_loading_indicator);

    }

     public class GithubQueryTask extends AsyncTask<URL, Void, String> {

         @Override
         protected void onPreExecute() {
             super.onPreExecute();
             mProgressBar.setVisibility(View.VISIBLE);
         }

         @Override
        protected String doInBackground(URL... urls) {
            URL searchUrl = urls[0];
            String githubSearchResult = null;
            try {
                githubSearchResult = NetworkUtilities.getResponseFromHttpUrl(searchUrl);
            } catch (IOException e) {
                e.printStackTrace();
            }

            return githubSearchResult;
        }

        @Override
        protected void onPostExecute(String s) {
             mProgressBar.setVisibility(View.INVISIBLE);
            if (s != null && !s.equals("")) {
                mSearchResults.setText(s);
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int menuItemThatWasSelected = item.getItemId();
        if (menuItemThatWasSelected == R.id.action_search) {
            makeGithubSearchQuery();
        }
        return super.onOptionsItemSelected(item);
    }

    void makeGithubSearchQuery(){
        String githubQuery = mSearchBoxEditText.getText().toString();
        URL githubSearchUrl = NetworkUtilities.buildUrl(githubQuery);
        if (githubSearchUrl == null) {
            mUrlDisplay.setText("Please try again");
        } else {
            mUrlDisplay.setText(githubSearchUrl.toString());
            new GithubQueryTask().execute(githubSearchUrl);
        }
    }

    private void showDataJsonView() {
        errorMessageTextView.setVisibility(View.INVISIBLE);
        mSearchResults.setVisibility(View.VISIBLE);
    }

    private void showErrorMessage() {
        errorMessageTextView.setVisibility(View.VISIBLE);
        mSearchResults.setVisibility(View.INVISIBLE);
    }
}
