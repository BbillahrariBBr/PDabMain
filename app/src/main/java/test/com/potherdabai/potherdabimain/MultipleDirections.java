package test.com.potherdabai.potherdabimain;

import android.content.Context;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.RecyclerView;
import android.widget.Adapter;
import android.widget.Toast;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.PolylineOptions;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import test.com.potherdabai.potherdabimain.DistanceAdapter;

public class MultipleDirections extends AsyncTask<Object, String, String> {
    Adapter adapter;
    Context f14c;
    String data = "";
    LatLng endLatLng;
    HttpURLConnection httpURLConnection = null;
    InputStream inputStream = null;
    GoogleMap mMap;
    RecyclerView recyclerView;
    LatLng startLatLng;
    String url;

    MultipleDirections(Context c) {
        this.f14c = c;
    }

    protected String doInBackground(Object... params) {
        this.mMap = (GoogleMap) params[0];
        this.url = (String) params[1];
        this.startLatLng = (LatLng) params[2];
        this.endLatLng = (LatLng) params[3];
        this.recyclerView = (RecyclerView) params[4];
        try {
            this.httpURLConnection = (HttpURLConnection) new URL(this.url).openConnection();
            this.httpURLConnection.connect();
            this.inputStream = this.httpURLConnection.getInputStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(this.inputStream));
            StringBuffer sb = new StringBuffer();
            String str = "";
            while (true) {
                str = bufferedReader.readLine();
                if (str == null) {
                    break;
                }
                sb.append(str);
            }
            this.data = sb.toString();
            bufferedReader.close();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e2) {
            e2.printStackTrace();
        }
        return this.data;
    }

    protected void onPostExecute(String str) {
        try {
            JSONObject jSONObject = new JSONObject(str);
            JSONArray legsArray = jSONObject.getJSONArray("routes").getJSONObject(0).getJSONArray("legs");
            JSONObject jsonObject1 = jSONObject.getJSONArray("routes").getJSONObject(0).getJSONArray("legs").getJSONObject(0);
            String distancetxt = jsonObject1.getJSONObject("distance").getString("text");
            String durationtxt = jsonObject1.getJSONObject("duration").getString("text");
            List<List<List<HashMap<String, String>>>> routes = new DirectionsJsonParser().parse(jSONObject);
            String distance = "";
            String duration = "";
            Integer size1 = Integer.valueOf(0);
            Integer size2 = Integer.valueOf(0);
            Integer size3 = Integer.valueOf(0);
            List<LatLng> aline1 = new ArrayList();
            List<LatLng> aline2 = new ArrayList();
            List<LatLng> aline3 = new ArrayList();
            if (routes != null) {
                for (int i = 0; i < routes.size(); i++) {
                    ArrayList<LatLng> points = new ArrayList();
                    List<List<HashMap<String, String>>> path1 = (List) routes.get(i);
                    for (int s = 0; s < path1.size(); s++) {
                        List<HashMap<String, String>> path = (List) path1.get(s);
                        for (int j = 0; j < path.size(); j++) {
                            PolylineOptions lineOptions1 = new PolylineOptions();
                            HashMap<String, String> point = (HashMap) path.get(j);
                            points.add(new LatLng(Double.parseDouble((String) point.get("lat")), Double.parseDouble((String) point.get("lng"))));
                        }
                    }
                    if (i == 0) {
                        size1 = Integer.valueOf(points.size());
                        aline1.addAll(points);
                    } else if (i == 1) {
                        aline2.addAll(points);
                        size2 = Integer.valueOf(points.size());
                    } else if (i == 2) {
                        aline3.addAll(points);
                        size3 = Integer.valueOf(points.size());
                    }
                }
            }
            PolylineOptions line1;
            PolylineOptions line2;
            if (size3.intValue() != 0) {
                PolylineOptions line3;
                if (size1.intValue() <= size2.intValue() || size1.intValue() <= size3.intValue()) {
                    if (size2.intValue() <= size1.intValue() || size2.intValue() <= size3.intValue()) {
                        if (size3.intValue() <= size1.intValue() || size3.intValue() <= size2.intValue()) {
                            System.out.println("ERROR!");
                        } else {
                            if (size1.intValue() > size2.intValue()) {
                                line1 = new PolylineOptions().width(8.0f).color(ViewCompat.MEASURED_STATE_MASK);
                                line2 = new PolylineOptions().width(11.0f).color(Color.parseColor("#9C27B0"));
                                line3 = new PolylineOptions().width(8.0f).color(ViewCompat.MEASURED_STATE_MASK);
                                line1.addAll(aline1);
                                line2.addAll(aline2);
                                line3.addAll(aline3);
                                this.mMap.addPolyline(line3);
                                this.mMap.addPolyline(line1);
                                this.mMap.addPolyline(line2);
                            } else {
                                line1 = new PolylineOptions().width(11.0f).color(Color.parseColor("#9C27B0"));
                                line2 = new PolylineOptions().width(8.0f).color(ViewCompat.MEASURED_STATE_MASK);
                                line3 = new PolylineOptions().width(8.0f).color(ViewCompat.MEASURED_STATE_MASK);
                                line1.addAll(aline1);
                                line2.addAll(aline2);
                                line3.addAll(aline3);
                                this.mMap.addPolyline(line3);
                                this.mMap.addPolyline(line2);
                                this.mMap.addPolyline(line1);
                            }
                        }
                    } else {
                        if (size1.intValue() > size3.intValue()) {
                            line1 = new PolylineOptions().width(8.0f).color(ViewCompat.MEASURED_STATE_MASK);
                            line2 = new PolylineOptions().width(8.0f).color(ViewCompat.MEASURED_STATE_MASK);
                            line3 = new PolylineOptions().width(11.0f).color(Color.parseColor("#9C27B0"));
                            line1.addAll(aline1);
                            line2.addAll(aline2);
                            line3.addAll(aline3);
                            this.mMap.addPolyline(line1);
                            this.mMap.addPolyline(line2);
                            this.mMap.addPolyline(line3);
                        } else {
                            line1 = new PolylineOptions().width(11.0f).color(Color.parseColor("#9C27B0"));
                            line2 = new PolylineOptions().width(8.0f).color(ViewCompat.MEASURED_STATE_MASK);
                            line3 = new PolylineOptions().width(8.0f).color(ViewCompat.MEASURED_STATE_MASK);
                            line1.addAll(aline1);
                            line2.addAll(aline2);
                            line3.addAll(aline3);
                            this.mMap.addPolyline(line2);
                            this.mMap.addPolyline(line3);
                            this.mMap.addPolyline(line1);
                        }
                    }
                } else {
                    if (size2.intValue() > size3.intValue()) {
                        line1 = new PolylineOptions().width(8.0f).color(ViewCompat.MEASURED_STATE_MASK);
                        line2 = new PolylineOptions().width(8.0f).color(ViewCompat.MEASURED_STATE_MASK);
                        line3 = new PolylineOptions().width(11.0f).color(Color.parseColor("#9C27B0"));
                        line1.addAll(aline1);
                        line2.addAll(aline2);
                        line3.addAll(aline3);
                        this.mMap.addPolyline(line1);
                        this.mMap.addPolyline(line2);
                        this.mMap.addPolyline(line3);
                    } else {
                        line1 = new PolylineOptions().width(8.0f).color(ViewCompat.MEASURED_STATE_MASK);
                        line2 = new PolylineOptions().width(11.0f).color(Color.parseColor("#9C27B0"));
                        line3 = new PolylineOptions().width(8.0f).color(ViewCompat.MEASURED_STATE_MASK);
                        line1.addAll(aline1);
                        line2.addAll(aline2);
                        line3.addAll(aline3);
                        this.mMap.addPolyline(line1);
                        this.mMap.addPolyline(line3);
                        this.mMap.addPolyline(line2);
                    }
                }
            } else if (size2.intValue() != 0) {
                if (size1.intValue() > size2.intValue()) {
                    line1 = new PolylineOptions().width(8.0f).color(ViewCompat.MEASURED_STATE_MASK);
                    line2 = new PolylineOptions().width(11.0f).color(Color.parseColor("#9C27B0"));
                    line1.addAll(aline1);
                    line2.addAll(aline2);
                    this.mMap.addPolyline(line1);
                    this.mMap.addPolyline(line2);
                } else {
                    line1 = new PolylineOptions().width(11.0f).color(Color.parseColor("#9C27B0"));
                    line2 = new PolylineOptions().width(8.0f).color(ViewCompat.MEASURED_STATE_MASK);
                    line1.addAll(aline1);
                    line2.addAll(aline2);
                    this.mMap.addPolyline(line2);
                    this.mMap.addPolyline(line1);
                }
            } else if (size1.intValue() != 0) {
                line1 = new PolylineOptions().width(11.0f).color(Color.parseColor("#9C27B0"));
                line1.addAll(aline1);
                this.mMap.addPolyline(line1);
            }
            this.adapter = (Adapter) new DistanceAdapter(this.f14c, distancetxt, durationtxt);
            this.recyclerView.setAdapter((RecyclerView.Adapter) this.adapter);
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this.f14c, e.getMessage(),Toast.LENGTH_SHORT).show();
        }
    }
}

