package com.rainbow.trakmovies;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;

import com.rainbow.trakmovies.API.TrakApiService;
import com.rainbow.trakmovies.model.DataModel;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import retrofit.Call;
import retrofit.GsonConverterFactory;
import retrofit.Response;
import retrofit.Retrofit;


public class MainActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {


    //json request url
    private static String API_URL = "http://www.json-generator.com/api/json/get/";//cpLylIjqWa?indent=2";

    private static String TAGKEY = "Trak";

    private boolean firstTime = true;

    ProgressDialog progress;
    AlertDialog alert;

    ListView listView;

    //List of movie data from json
    List<DataModel> dmList = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        System.out.println("called on create");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        listView = (ListView) findViewById(R.id.listMain);

        //On click move to next activity
        listView.setOnItemClickListener(this);
        listView.setOnScrollListener(new ListScrollListener(getApplicationContext(), TAGKEY));

        //Check network connectivity first
        if (isOnline()) {
            //If online, get the data
            startTask();

        } else {
            //else alert
            showAlert();
        }

    }

    //Async Task to get JSON response
    public void startTask() {
        if (firstTime) {
            BackgroundTask task = new BackgroundTask();
            task.execute();
        }
        firstTime = false;
    }


    //Alert to connect to internet
    private void showAlert() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(getResources().getString(R.string.connectionAlertMsg))
                .setCancelable(false)
                .setPositiveButton(getResources().getString(R.string.yes), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        //Show internet connection setting dialog
                        startActivity(new Intent(Settings.ACTION_WIFI_SETTINGS));
                    }
                })
                .setNegativeButton(getResources().getString(R.string.no), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        finish();
                    }
                });
        alert = builder.create();
        alert.show();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (isOnline()) {
            //start task if connected now
            startTask();
        } else {
            //Dont finish activity if alert dialog is being shown, wait for user interaction
            if (alert != null && !alert.isShowing()) {
                finish();
            }
        }
    }

    //Check if connected to internet
    private boolean isOnline() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        if (activeNetworkInfo == null) return false;
        return true;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        Intent intent = new Intent(getApplicationContext(), MovieDetail.class);
        DataModel dm = dmList.get(position);

        //Pass data to second activity

        intent.putExtra("image", dm.movie.images.poster.full);
        intent.putExtra("trailer", dm.movie.trailer);
        intent.putExtra("overview", dm.movie.overview);
        intent.putExtra("genres", Arrays.toString(dm.movie.genres));
        intent.putExtra("tagline", dm.movie.tagline);
        intent.putExtra("releasedate", dm.movie.released);
        intent.putExtra("rating", dm.movie.getRating() + "/10");
        intent.putExtra("url", dm.movie.images.poster.full);
        intent.putExtra("title", dm.movie.title);
        startActivity(intent);

    }

    @Override
    protected void onStop() {
        dismissProgressDialog();
        super.onStop();

    }

    public void dismissProgressDialog() {

        if (progress != null && progress.isShowing()) {
            progress.dismiss();
            progress = null;
        }
    }


    private class BackgroundTask extends AsyncTask<Void, Void, List<DataModel>> {
        Retrofit retrofit;
        TrakApiService service;


        @Override
        protected void onPreExecute() {
            progress = ProgressDialog.show(MainActivity.this,
                    getResources().getString(R.string.loadData), getResources().getString(R.string.waitMsg));

            GsonConverterFactory factory = GsonConverterFactory.create();
            retrofit = new Retrofit.Builder()
                    .baseUrl(API_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            service = retrofit.create(TrakApiService.class);
        }

        @Override
        protected List<DataModel> doInBackground(Void... params) {

            Call<List<DataModel>> call = service.getMovie();
            Response<List<DataModel>> response = null;
            try {
                //Getting movie data
                response = call.execute();
            } catch (IOException ioe) {
                System.out.println("IOException *********  " + ioe.getMessage());
                return null;
            }
            List<DataModel> list = response.body();
            return response.body();
        }

        @Override
        protected void onPostExecute(List<DataModel> dataModelList) {

            dismissProgressDialog();
            if (dataModelList == null) {
                //Some error getting JSON response
                showErrorAlert();
                return;
                //finish();
            }
            //progress.
            // Set the data received
            dmList = dataModelList;
            //  Picasso.with(getApplicationContext()).setIndicatorsEnabled(true);

            /*
            int count = 0;
            //load to cache all required images
            for (DataModel dm : dmList) {
                String url = dm.movie.images.poster.thumb;
                Picasso.with(getApplicationContext()).load(url).fetch();
            }
            */

            //Create new list adaptor and set data on it
            CustomListAdapter listAdapter = new CustomListAdapter(getApplicationContext(), dmList);
            listView.setAdapter(listAdapter);

        }

        private void showErrorAlert() {
            String message = getResources().getString(R.string.errorMsg);
            new AlertDialog.Builder(MainActivity.this)
                    .setTitle(getResources().getString(R.string.errorTitle))
                    .setMessage(message)
                    .setPositiveButton(getResources().getString(R.string.ok), new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            // if this button is clicked, close
                            // current activity
                            MainActivity.this.finish();
                        }
                    })
                    .show();
        }
    }
}

class ListScrollListener implements AbsListView.OnScrollListener {

    Context context;
    String TAGKEY;

    public ListScrollListener(Context context, String tag) {
        this.context = context;
        this.TAGKEY = tag;
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {

        Picasso picasso = Picasso.with(context);
        //This is to avoid unnecessary fetching while flinging
        if (scrollState == SCROLL_STATE_IDLE ||
                scrollState == SCROLL_STATE_TOUCH_SCROLL) {
            picasso.resumeTag(TAGKEY);
        } else {
            picasso.pauseTag(TAGKEY);
        }
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
    //No updates
    }
}
