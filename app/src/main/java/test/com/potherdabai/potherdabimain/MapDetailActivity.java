package test.com.potherdabai.potherdabimain;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Adapter;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.Locale;

public class MapDetailActivity extends AppCompatActivity implements OnMapReadyCallback {

    Adapter adapter;
    Marker currentMarker;
    Marker destinationMarker;
    LatLng latLngEnd;
    LatLng latLngStart;
    RecyclerView.LayoutManager layoutManager;
    GoogleMap mMap;
    RecyclerView recyclerView;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView((int) R.layout.activity_map_detail);
        ((SupportMapFragment) getSupportFragmentManager().findFragmentById(
                R.id.mylocMap)).getMapAsync(this);
        this.recyclerView = (RecyclerView) findViewById(R.id.recyclerView2);
        this.layoutManager = new LinearLayoutManager(this);
        this.recyclerView.setLayoutManager(this.layoutManager);
        this.recyclerView.setHasFixedSize(true);
        Intent myIntent = getIntent();
        this.latLngStart = (LatLng) myIntent.getParcelableExtra("source");
        this.latLngEnd = (LatLng) myIntent.getParcelableExtra("destination");
    }

    public void navigate(View v) {
        try {
            if (this.latLngStart != null && this.latLngEnd != null) {
                Intent intent = new Intent("android.intent.action.VIEW",
                        Uri.parse(String.format(Locale.ENGLISH, "http://maps.google.com/maps?saddr=%f,%f(%s)&daddr=%f,%f (%s)",
                                new Object[]{Double.valueOf(this.latLngStart.latitude),
                                        Double.valueOf(this.latLngStart.longitude),
                                        "Source", Double.valueOf(this.latLngEnd.latitude),
                                        Double.valueOf(this.latLngEnd.longitude), "Destination"})));
                intent.setPackage("com.google.android.apps.maps");
                startActivity(intent);
            }
        } catch (ActivityNotFoundException e) {
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), "Google Maps is not installed in your phone. Please install.",
                    Toast.LENGTH_SHORT).show();
        }
    }

    public void requestDirections() {
        try {
            Object[] dataTransfer = new Object[5];
            StringBuilder sb = new StringBuilder();
            sb.append("https://maps.googleapis.com/maps/api/directions/json?");
            sb.append("origin=" + this.latLngStart.latitude + "," + this.latLngStart.longitude);
            sb.append("&destination=" + this.latLngEnd.latitude + "," + this.latLngEnd.longitude);
            sb.append("&key=" + getResources().getString(R.string.directions_api));
            sb.append("&alternatives=true");
            MultipleDirections directions = new MultipleDirections(this);
            dataTransfer[0] = this.mMap;
            dataTransfer[1] = sb.toString();
            dataTransfer[2] = new LatLng(this.latLngStart.latitude, this.latLngStart.longitude);
            dataTransfer[3] = new LatLng(this.latLngEnd.latitude, this.latLngEnd.longitude);
            dataTransfer[4] = this.recyclerView;
            directions.execute(dataTransfer);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void onMapReady(GoogleMap googleMap) {
        this.mMap = googleMap;
        if (this.latLngStart != null && this.latLngEnd != null) {
            MarkerOptions options = new MarkerOptions();
            options.position(this.latLngStart);
            options.title("Current Location");
            options.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));
            this.currentMarker = this.mMap.addMarker(options);
            this.destinationMarker = this.mMap.addMarker(new MarkerOptions().position(this.latLngEnd).title("Destination Location").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));
            this.mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(this.latLngStart, 15.0f));
            requestDirections();
        }
    }
}

