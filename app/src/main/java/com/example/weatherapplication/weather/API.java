package com.example.enverpelit.weather;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by enverpelit on 02/04/15.
 */
public class API {



    HttpURLConnection uconn;

    InputStream is;


    public API(){

    }

    

    public PlaceWeather getPlaceWeatherFromjson(String json){

        PlaceWeather plcW = new PlaceWeather();

        try {


            JSONObject jsonRead = new JSONObject(json);

            //get the cityname

            String cname = jsonRead.getString("name");

            System.out.println("City name is: "+cname);

            plcW.setPlaceName(cname);


            Long date = jsonRead.getLong("dt");

            plcW.setDateStamp(date);


            //get coordinates object
            JSONObject coords = jsonRead.getJSONObject("coord");
            Double lat = coords.getDouble("lat");
            Double longitude = coords.getDouble("lon");


            //get city info
            JSONObject sys = jsonRead.getJSONObject("sys");
            String country_name = sys.getString("country");
            plcW.setPlaceCountry(country_name);


            //get main weather Info:

            JSONObject main_weather = jsonRead.getJSONObject("main");
            Double temp = main_weather.getDouble("temp");
            plcW.setTemperature(temp);


            //get temp desc array


            JSONArray ret_array = jsonRead.getJSONArray("weather");
            JSONObject desc_obj = ret_array.getJSONObject(0);
            plcW.setDesc(desc_obj.getString("description"));







            //get weather desc


            System.out.println(country_name);





        }catch (Exception e){

        }

        return plcW;

    }


    public PlaceWeather getFromSeatch(String json){

        PlaceWeather plcW = new PlaceWeather();

        try{
            JSONObject jsonFeed = new JSONObject(json);

            JSONArray list = jsonFeed.getJSONArray("list");
            //Log.d("Found: ", Integer.toString(list.length()));

//
            JSONObject list_suggestion = list.getJSONObject(0);



            String cname = list_suggestion.getString("name");

            //Log.d("City name is: ", cname);

            plcW.setPlaceName(cname);


            long date = list_suggestion.getLong("dt");
            //String strDate = Integer.toString(date);
            //Long long_date = Long.parseLong(strDate);

            //String date_str = retDateString(long_date);
            plcW.setDateStamp(date);


            //get coordinates object
            JSONObject coords = list_suggestion.getJSONObject("coord");
            Double lat = coords.getDouble("lat");
            Double longitude = coords.getDouble("lon");


            //get city info
            JSONObject sys = list_suggestion.getJSONObject("sys");
            String country_name = sys.getString("country");
            plcW.setPlaceCountry(country_name);


            //get main weather Info:

            JSONObject main_weather = list_suggestion.getJSONObject("main");
            Double temp = main_weather.getDouble("temp");
            plcW.setTemperature(temp);

            //get temp desc array

            JSONArray ret_array = list_suggestion.getJSONArray("weather");
            JSONObject desc_obj = ret_array.getJSONObject(0);
            plcW.setDesc(desc_obj.getString("description"));




            //get weather desc


            Log.d("Country: ", country_name);






        }catch(Exception e){
            Log.d("Error", e.getMessage());
        }


        return plcW;
    }



    
    public String  getJsonFromHTTP(String urlString){
        String returned_data = "";
        try{
            URL url = new URL(urlString);
            uconn = (HttpURLConnection)url.openConnection();
            uconn.setReadTimeout(10000);
            uconn.setConnectTimeout(15000);
            uconn.setRequestMethod("GET");
            uconn.setDoInput(true);
            uconn.connect();

            is = uconn.getInputStream();
            returned_data = getStringInputStream(is);




        }catch (Exception e){

        }
        return returned_data;
    }



    private String getStringInputStream(InputStream is) {

        BufferedReader br = null;

        StringBuilder sb = new StringBuilder();

        String jString;

        try{
            br = new BufferedReader(new InputStreamReader(is));
            while ((jString = br.readLine())!= null){
                sb.append(jString);
            }
        }catch(Exception e){

        }finally {
            if(br != null){
                try{
                    br.close();
                }catch(Exception e){

                }
            }
        }

        return sb.toString();
    }
}
