package com.example.enverpelit.weather;

import android.app.ProgressDialog;
import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.appcompat.R;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.text.DecimalFormat;


public class FirstDisplay extends ActionBarActivity {


    LocationManager locman;
    Location loc;


    EditText searchT;
    Button searchB, locB;
    TextView location, temperature;
    ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_display);
        locman = (LocationManager)this.getSystemService(Context.LOCATION_SERVICE);

        initView();

    }


    public void initView(){
        searchT = (EditText)findViewById(R.id.search_text);
        searchB = (Button)findViewById(R.id.search_button);
        locB = (Button)findViewById(R.id.getLocationButton);

        searchB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadSearch();
            }
        });

        locB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onResume();
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_first_display, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    //loation listener
    private LocationListener locList = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
            loc = location;
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }

        @Override
        public void onProviderEnabled(String provider) {

        }

        @Override
        public void onProviderDisabled(String provider) {

        }
    };


    public  void getLocation(){
        if (loc == null){
            //loc_ini.setText("No location yet ...");
        }else{

            LoadAction();


        }
    }
    @Override
    protected void onResume() {
        super.onResume();
        loc = locman.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        getLocation();
        locman.requestLocationUpdates(LocationManager.GPS_PROVIDER,0 , 0, locList);

    }

    @Override
    protected void onPause() {
        super.onPause();
        locman.removeUpdates(locList);
    }


    public void LoadAction(){

        Double lat = loc.getLatitude();
        Double longi = loc.getLongitude();

        String strlon = String.format("%.2f", longi);
        String strlat = String.format("%.2f", lat);

        String[] coords = {strlat, strlon};

        LoadTheLocationData lld= new LoadTheLocationData();
        lld.execute(coords);
    }



    public void loadSearch(){
        String search = searchT.getText().toString().trim();

        new LoadTheSearchData().execute(search);

    }


    //async task used to get weather for search
    class LoadTheSearchData extends AsyncTask<String, Void, PlaceWeather>{
        @Override
        protected PlaceWeather doInBackground(String... params) {

            String  coords = params[0];

            String us = "http://api.openweathermap.org/data/2.5/find?q="+coords+"&type=like&mode=json&units=metric";

            String json = new API().getJsonFromHTTP(us);
            return new API().getFromSeatch(json);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressDialog = ProgressDialog.show(FirstDisplay.this, "Message", "loading Info...");
            progressDialog.setCancelable(true);
        }


        @Override
        protected void onPostExecute(PlaceWeather placeWeather) {
            super.onPostExecute(placeWeather);

            location =(TextView) findViewById(R.id.locationDisplay);
            temperature =(TextView) findViewById(R.id.tempDisplay);

            DecimalFormat myFormatter = new DecimalFormat("#.##");
            String dtemp = myFormatter.format(placeWeather.getTemperature());

            location.setText(placeWeather.getPlaceName()+", "+placeWeather.getPlaceCountry());
            temperature.setText(dtemp+ "°");

            progressDialog.dismiss();


        }
    }



    //async task used to get weather info for locations

    class LoadTheLocationData extends AsyncTask<String[], Void, PlaceWeather>{
        @Override
        protected PlaceWeather doInBackground(String[]... params) {

            String [] coords = params[0];

            String us = "http://api.openweathermap.org/data/2.5/weather?lat="+coords[0]+"&lon="+coords[1]+"&units=metric";

            String json = new API().getJsonFromHTTP(us);
            return new API().getPlaceWeatherFromjson(json);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressDialog = ProgressDialog.show(FirstDisplay.this, "Message", "loading Info...");
            progressDialog.setCancelable(true);
        }


        @Override
        protected void onPostExecute(PlaceWeather placeWeather) {
            super.onPostExecute(placeWeather);

            location =(TextView) findViewById(R.id.locationDisplay);
            temperature =(TextView) findViewById(R.id.tempDisplay);

            DecimalFormat myFormatter = new DecimalFormat("#.##");
            String dtemp = myFormatter.format(placeWeather.getTemperature());

            location.setText(placeWeather.getPlaceName()+", "+placeWeather.getPlaceCountry());
            temperature.setText(dtemp+ "°");

            progressDialog.dismiss();


        }
    }
}
