package test.com.potherdabai.potherdabimain;

import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
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

public class MyLocationActivity extends AppCompatActivity implements OnMapReadyCallback, LocationListener, ConnectionCallbacks, OnConnectionFailedListener {
    GoogleApiClient client;
    Marker currentMarker;
    LatLng latLngStart;
    private GoogleMap mMap;
    public boolean normalMap = true;
    LocationRequest request;
    public boolean traffic = false;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_location);
        ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.mylocMap))
                .getMapAsync(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.myToolbar);
        toolbar.setTitle((CharSequence) "Current Location");
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
    }

    public void convertMap(View v) {
        if (this.normalMap) {
            this.mMap.setMapType(2);
            this.normalMap = false;
        } else if (!this.normalMap) {
            this.mMap.setMapType(1);
            this.normalMap = true;
        }
    }

    public void trafficSet(View v) {
        if (!this.traffic && this.mMap != null) {
            this.mMap.setTrafficEnabled(true);
            this.traffic = true;
        } else if (!this.traffic || this.mMap == null) {
            Toast.makeText(getApplicationContext(), "Wait for the map to load properly.", Toast.LENGTH_SHORT)
                    .show();
        } else {
            this.mMap.setTrafficEnabled(false);
            this.traffic = false;
        }
    }

    public void onMapReady(GoogleMap googleMap) {
        this.mMap = googleMap;
        this.client = new GoogleApiClient.Builder(this).addApi(LocationServices.API).addConnectionCallbacks(this).addOnConnectionFailedListener(this).build();
        this.client.connect();
    }

    public void onConnected(@Nullable Bundle bundle) {
        LocationRequest locationRequest = new LocationRequest();
        this.request = LocationRequest.create();
        this.request.setPriority(100);
        this.request.setInterval(1000);
        if (ContextCompat.checkSelfPermission(this, "android.permission.ACCESS_FINE_LOCATION") == 0 || ContextCompat.checkSelfPermission(this, "android.permission.ACCESS_COARSE_LOCATION") == 0) {
            LocationServices.FusedLocationApi.requestLocationUpdates(this.client, this.request, (LocationListener) this);
        }
    }

    public void onLocationChanged(Location location) {
        LocationServices.FusedLocationApi.removeLocationUpdates(this.client, (LocationListener) this);
        if (location == null) {
            Toast.makeText(getApplicationContext(), "Location could not be found", Toast.LENGTH_LONG).show();
            return;
        }
        this.latLngStart = new LatLng(location.getLatitude(), location.getLongitude());
        MarkerOptions options = new MarkerOptions();
        options.position(this.latLngStart);
        options.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));
        options.title("Current Location");
        this.currentMarker = this.mMap.addMarker(options);
        this.mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(this.latLngStart, 15.0f));
    }

    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
    }

    public void onConnectionSuspended(int i) {
    }

}
