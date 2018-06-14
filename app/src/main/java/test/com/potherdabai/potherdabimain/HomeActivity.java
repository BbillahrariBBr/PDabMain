package test.com.potherdabai.potherdabimain;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.Toast;


import com.karan.churi.PermissionManager.PermissionManager;
import com.rom4ek.arcnavigationview.ArcNavigationView;

import java.util.ArrayList;

public class HomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    PermissionManager permissionManager;
    Toolbar toolbar;
    DrawerLayout drawerLayout;
    NavigationView navigationView;
     ActionBarDrawerToggle toggle;
    ArcNavigationView arcNavigationView;
    Intent intent;
//    ImageView toilet;
//    ImageView mosque;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.nav_drawer);
        setUpUIMain();
        setSupportActionBar(toolbar);
        toggle = new ActionBarDrawerToggle(HomeActivity.this,drawerLayout,
                toolbar,R.string.open_drawer,R.string.close_drawer);
        //navigationView.setNavigationItemSelectedListener(HomeActivity.this);
        arcNavigationView.setNavigationItemSelectedListener(HomeActivity.this);
        drawerLayout.setDrawerListener(toggle);
        toggle.syncState();
    }

    public void setUpUIMain() {
        permissionManager = new PermissionManager() {
        };
        permissionManager.checkAndRequestPermissions(this);

        toolbar = (android.support.v7.widget.Toolbar)findViewById(R.id.toolbar);
        drawerLayout = (DrawerLayout)findViewById(R.id.drawer_layout);
        //navigationView = (NavigationView)findViewById(R.id.navigation_view);
        arcNavigationView =(ArcNavigationView)findViewById(R.id.nav_view);
        //toilet = (ImageView)findViewById(R.id.img_toilet);
        //mosque = (ImageView)findViewById(R.id.mosque);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        permissionManager.checkResult(requestCode,permissions,grantResults);
        ArrayList<String> granted = permissionManager.getStatus().get(0).granted;
        ArrayList<String> denied = permissionManager.getStatus().get(0).denied;
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)){
            drawerLayout.closeDrawer(GravityCompat.START);
        }
        else{
            super.onBackPressed();
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        switch (id){
            case R.id.contribution:
//                intent = new Intent(HomeActivity.this,HomeActivity.class);
//                startActivity(intent);
                Toast.makeText(HomeActivity.this, "Contribution", Toast.LENGTH_LONG).show();
                break;

            case R.id.mylocation:
                intent = new Intent(HomeActivity.this,MyLocationActivity.class);
                startActivity(intent);

//                Toast.makeText(HomeActivity.this, "My Location", Toast.LENGTH_LONG).show();
//                break;

            case R.id.routefinder:
                intent = new Intent(HomeActivity.this,RouteFinderActivity.class);
                startActivity(intent);
//                Toast.makeText(HomeActivity.this, "Route Finder", Toast.LENGTH_LONG).show();
//                break;

            case R.id.streetview:
//                intent = new Intent(MainActivity.this,BRAPrimarySchoolActivity.class);
//                startActivity(intent);
                Toast.makeText(HomeActivity.this, "Street View", Toast.LENGTH_LONG).show();
                break;

            case R.id.about:
//                intent = new Intent(MainActivity.this,BRAcPreprimaryActivity.class);
//                startActivity(intent);
                Toast.makeText(HomeActivity.this, "About us", Toast.LENGTH_LONG).show();
                break;
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }
}
