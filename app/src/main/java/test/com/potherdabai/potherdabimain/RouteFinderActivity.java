package test.com.potherdabai.potherdabimain;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper.Callback;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.Builder;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.google.android.gms.location.places.ui.PlaceAutocomplete.IntentBuilder;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnCameraIdleListener;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import java.util.List;
import java.util.Locale;

public class RouteFinderActivity extends AppCompatActivity implements OnMapReadyCallback,
        LocationListener, ConnectionCallbacks, OnConnectionFailedListener {
    Button b2;
    GoogleApiClient client;
    Marker currentMarker;
    Marker destinationMarker;
    EditText e1;
    ImageView i1;
    LatLng latLngEnd;
    LatLng latLngStart;
    private GoogleMap mMap;
    LocationRequest request;

    class C03201 implements OnClickListener {
        C03201() {
        }

        public void onClick(View v) {
            if (RouteFinderActivity.this.latLngStart == null) {
                Toast.makeText(RouteFinderActivity.this.getApplicationContext(),
                        "First select starting position", Toast.LENGTH_SHORT).show();
                return;
            }
            try {
                RouteFinderActivity.this.startActivityForResult(new IntentBuilder(2)
                        .build(RouteFinderActivity.this), Callback.DEFAULT_DRAG_ANIMATION_DURATION);
            } catch (GooglePlayServicesNotAvailableException e) {
                e.printStackTrace();
            } catch (GooglePlayServicesRepairableException e2) {
                e2.printStackTrace();
            }
        }
    }

    class C04772 implements OnCameraIdleListener {
        C04772() {
        }

        public void onCameraIdle() {
            LatLng center = RouteFinderActivity.this.mMap.getCameraPosition().target;
            if (!RouteFinderActivity.this.b2.getText().toString().equals("") || RouteFinderActivity.this.currentMarker == null) {
                RouteFinderActivity.this.i1.setVisibility(View.GONE);
                return;
            }
            RouteFinderActivity.this.currentMarker.remove();
            RouteFinderActivity.this.currentMarker = RouteFinderActivity.this.mMap.addMarker(new MarkerOptions().title("New Location").position(center).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));
            RouteFinderActivity.this.latLngStart = RouteFinderActivity.this.currentMarker.getPosition();
            RouteFinderActivity.this.e1.setText(RouteFinderActivity.this.getStringAddress(Double.valueOf(RouteFinderActivity.this.currentMarker.getPosition().latitude), Double.valueOf(RouteFinderActivity.this.currentMarker.getPosition().longitude)));
        }
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView((int) R.layout.activity_route_finder);
        this.e1 = (EditText) findViewById(R.id.editText);
        this.b2 = (Button) findViewById(R.id.editText3);
        ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.mylocMap)).getMapAsync(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.myToolbar);
        toolbar.setTitle((CharSequence) "Route Finder");
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
        this.b2.setOnClickListener(new C03201());
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == Callback.DEFAULT_DRAG_ANIMATION_DURATION && resultCode == -1) {
            this.latLngEnd = PlaceAutocomplete.getPlace(this, data).getLatLng();
            Intent myIntent;
            if (this.destinationMarker == null) {
                this.destinationMarker = this.mMap.addMarker(new MarkerOptions().position(this.latLngEnd).title("Destination"));
                this.b2.setText(getStringAddress(Double.valueOf(this.latLngEnd.latitude), Double.valueOf(this.latLngEnd.longitude)));
                myIntent = new Intent(this, MapDetailActivity.class);
                myIntent.putExtra("source", this.latLngStart);
                myIntent.putExtra("destination", this.latLngEnd);
                startActivity(myIntent);
                return;
            }
            this.destinationMarker.remove();
            this.destinationMarker = this.mMap.addMarker(new MarkerOptions().position(this.latLngEnd).title("Destination"));
            this.b2.setText(getStringAddress(Double.valueOf(this.latLngEnd.latitude), Double.valueOf(this.latLngEnd.longitude)));
            myIntent = new Intent(this, MapDetailActivity.class);
            myIntent.putExtra("source", this.latLngStart);
            myIntent.putExtra("destination", this.latLngEnd);
            startActivity(myIntent);
        }
    }

    public void onMapReady(GoogleMap googleMap) {
        this.mMap = googleMap;
        this.client = new Builder(this).addApi(LocationServices.API).addConnectionCallbacks(this).addOnConnectionFailedListener(this).build();
        this.client.connect();
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
        options.icon(BitmapDescriptorFactory.fromResource(R.drawable.markersmall));
        options.title("Current Location");
        this.currentMarker = this.mMap.addMarker(options);
        this.e1.setText(getStringAddress(Double.valueOf(this.latLngStart.latitude), Double.valueOf(this.latLngStart.longitude)));
        this.mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(this.latLngStart, 15.0f));
        this.mMap.setOnCameraIdleListener(new C04772());
    }

    public String getStringAddress(Double lat, Double lng) {
        String address = "";
        String city = "";
        try {
            List<Address> addresses = new Geocoder(this, Locale.getDefault()).getFromLocation(lat.doubleValue(), lng.doubleValue(), 1);
            address = ((Address) addresses.get(0)).getAddressLine(0);
            city = ((Address) addresses.get(0)).getLocality();
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
        }
        return address + " " + city;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == 16908332) {
            finish();
        }
        return super.onOptionsItemSelected(item);
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

    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
    }

    public void onConnectionSuspended(int i) {
    }
}
