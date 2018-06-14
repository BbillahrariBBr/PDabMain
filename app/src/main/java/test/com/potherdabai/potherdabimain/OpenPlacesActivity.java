package test.com.potherdabai.potherdabimain;

import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.Adapter;
import android.support.v7.widget.RecyclerView.LayoutManager;
import android.widget.Toast;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.Builder;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import java.util.ArrayList;

public class OpenPlacesActivity extends AppCompatActivity implements ConnectionCallbacks, OnConnectionFailedListener, LocationListener, OnMapReadyCallback {
    Adapter adapter;
    GoogleApiClient client;
    Marker currentMarker;
    LatLng latLngStart;
    ArrayList<String> lat_list;
    LayoutManager layoutManager;
    ArrayList<String> lng_list;
    GoogleMap mMap;
    ArrayList<String> namesList;
    RecyclerView recyclerView;
    LocationRequest request;
    ArrayList<String> vicinity_list;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_open_places);
        this.recyclerView = (RecyclerView) findViewById(R.id.recyclerView1);
        this.layoutManager = new LinearLayoutManager(this);
        this.recyclerView.setLayoutManager(this.layoutManager);
        this.recyclerView.setHasFixedSize(true);
        ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.mylocMap)).getMapAsync(this);
        this.namesList = (ArrayList) getIntent().getSerializableExtra("names_list");
        this.lat_list = (ArrayList) getIntent().getSerializableExtra("lat_list");
        this.lng_list = (ArrayList) getIntent().getSerializableExtra("lng_list");
        this.vicinity_list = (ArrayList) getIntent().getSerializableExtra("vicinity_list");
    }

    public void onMapReady(GoogleMap googleMap) {
        this.mMap = googleMap;
        this.client = new Builder(this).addApi(LocationServices.API).
                addConnectionCallbacks(this).addOnConnectionFailedListener(this).build();
        this.client.connect();
    }

    public void onConnected(@Nullable Bundle bundle) {
        LocationRequest locationRequest = new LocationRequest();
        this.request = LocationRequest.create();
        this.request.setPriority(100);
        this.request.setInterval(7000);
        if (ContextCompat.checkSelfPermission(this, "android.permission.ACCESS_FINE_LOCATION") == 0 || ContextCompat.checkSelfPermission(this, "android.permission.ACCESS_COARSE_LOCATION") == 0) {
            LocationServices.FusedLocationApi.requestLocationUpdates(this.client, this.request, (LocationListener) this);
        }
    }

    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
    }

    public void onConnectionSuspended(int i) {
    }

    public void onLocationChanged(Location location) {
        if (location == null) {
            Toast.makeText(getApplicationContext(), "Location could not be found", Toast.LENGTH_SHORT).show();
            return;
        }
        LocationServices.FusedLocationApi.removeLocationUpdates(this.client, (LocationListener) this);
        this.latLngStart = new LatLng(location.getLatitude(), location.getLongitude());
        MarkerOptions options = new MarkerOptions();
        options.position(this.latLngStart);
        options.title("Current Location");
        options.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));
        this.currentMarker = this.mMap.addMarker(options);
        this.mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(this.latLngStart, 14.0f));
        put_list(this.namesList, this.lat_list, this.lng_list, this.vicinity_list);
    }

    public void put_list(ArrayList<String> name_List, ArrayList<String> latList, ArrayList<String> lngList, ArrayList<String> vicinity_list) {
        try {
            String source_lat = String.valueOf(this.latLngStart.latitude);
            String source_lng = String.valueOf(this.latLngStart.longitude);
            ArrayList<SingleRow> mylist = new ArrayList();
            if (name_List.size() == 0) {
                Toast.makeText(this, "No place found nearby.", Toast.LENGTH_LONG).show();
                return;
            }
            int i;
            for (i = 0; i < name_List.size(); i++) {
                mylist.add(new SingleRow((String) name_List.get(i), (String) vicinity_list.get(i), (String) latList.get(i), (String) lngList.get(i)));
            }
            this.adapter = new CountryAdapter(this, mylist, name_List, vicinity_list, source_lat, source_lng);
            this.recyclerView.setAdapter(this.adapter);
            for (i = 0; i < name_List.size(); i++) {
                MarkerOptions markerOptions = new MarkerOptions();
                markerOptions.title((String) vicinity_list.get(i));
                markerOptions.position(new LatLng(Double.parseDouble((String) latList.get(i)), Double.parseDouble((String) lngList.get(i))));
                markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));
                this.mMap.addMarker(markerOptions);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
