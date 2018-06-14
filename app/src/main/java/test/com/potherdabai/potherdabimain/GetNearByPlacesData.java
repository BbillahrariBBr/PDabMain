package test.com.potherdabai.potherdabimain;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class GetNearByPlacesData extends AsyncTask<Object, String, String> {
    Context f13c;
    private String googlePlacesData;
    private String url;

    GetNearByPlacesData(Context c) {
        this.f13c = c;
    }

    protected String doInBackground(Object... params) {
        this.url = (String) params[0];
        try {
            this.googlePlacesData = new DownloadUrl().readUrl(this.url);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return this.googlePlacesData;
    }

    protected void onPostExecute(String s) {
        try {
            showNearByPlaces(new DataParser().parse(s));
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this.f13c, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void showNearByPlaces(List<HashMap<String, String>> nearbyPlaceList) {
        try {
            String place_name = "";
            String vicinity = "";
            ArrayList<String> names_list = new ArrayList();
            ArrayList<String> lat_list = new ArrayList();
            ArrayList<String> lng_list = new ArrayList();
            ArrayList<String> vicinity_list = new ArrayList();
            for (int i = 0; i < nearbyPlaceList.size(); i++) {
                MarkerOptions options = new MarkerOptions();
                HashMap<String, String> googlePlace = (HashMap) nearbyPlaceList.get(i);
                place_name = (String) googlePlace.get("place_name");
                names_list.add(place_name);
                vicinity = (String) googlePlace.get("vicinity");
                vicinity_list.add(vicinity);
                double lat = Double.parseDouble((String) googlePlace.get("lat"));
                double lng = Double.parseDouble((String) googlePlace.get("lng"));
                String lat_send = String.valueOf(lat);
                String lng_send = String.valueOf(lng);
                lat_list.add(lat_send);
                lng_list.add(lng_send);
                options.position(new LatLng(lat, lng));
                options.title(place_name + ": " + vicinity);
            }
            Intent myIntent = new Intent(this.f13c, OpenPlacesActivity.class);
            myIntent.putExtra("names_list", names_list);
            myIntent.putExtra("lat_list", lat_list);
            myIntent.putExtra("lng_list", lng_list);
            myIntent.putExtra("vicinity_list", vicinity_list);
            myIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            this.f13c.startActivity(myIntent);
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this.f13c, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
}

