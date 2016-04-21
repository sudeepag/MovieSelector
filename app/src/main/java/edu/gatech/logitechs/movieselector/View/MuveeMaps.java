package edu.gatech.logitechs.movieselector.View;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.provider.Settings;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;


import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.List;

import edu.gatech.logitechs.movieselector.Controller.Place;
import edu.gatech.logitechs.movieselector.Controller.PlacesService;
import edu.gatech.logitechs.movieselector.R;

public class MuveeMaps extends FragmentActivity implements OnMapReadyCallback, LocationListener {

    private GoogleMap mMap;
    private List<String> items;
    private SupportMapFragment mapFragment;
    private LocationManager m_LocationManager;
    private Location m_Location;


    public final int MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 6;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.muvee_maps_activity);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        items = new ArrayList<>();
        items.add("No Data");

        updateListView();

        checkPhoneLocationProvided();

        updateLocation();
        m_Location = getLocation();
        zoomToCurrLocation();

        final FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                updateLocation();
                m_Location = getLocation();
                zoomToCurrLocation();
            }
        });

    }

    private void updateListView() {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, items);
        adapter.notifyDataSetChanged();
        ListView list = (ListView) findViewById(R.id.listView);
        list.setAdapter(adapter);
    }

    private void updateLocation() {
        if (mMap == null) {
            // Try to obtain the map from the SupportMapFragment.
            mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map))
                    .getMap();

            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {
                mMap.setMyLocationEnabled(true);
            } else {
                // Show rationale and request permission.
                ActivityCompat.requestPermissions(MuveeMaps.this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
            }

            // Check if we were successful in obtaining the map.
            if (mMap != null) {
                mMap.setOnMyLocationChangeListener(new GoogleMap.OnMyLocationChangeListener() {
                    @Override
                    public void onMyLocationChange(Location arg0) {
                        // TODO Auto-generated method stub
//                        mMap.addMarker(new MarkerOptions().position(new LatLng(arg0.getLatitude(), arg0.getLongitude())).title("It's Me!"));
                        m_Location = arg0;
                    }
                });

            }
        }
    }

    public Location getLocation() {
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED ) {
            ActivityCompat.requestPermissions(MuveeMaps.this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
        }
        m_LocationManager= (LocationManager) getSystemService(LOCATION_SERVICE);
        Criteria criteria = new Criteria();
        String bestProvider = m_LocationManager.getBestProvider(criteria, true);
        Location location = m_LocationManager.getLastKnownLocation(bestProvider);
//        m_LocationManager.requestLocationUpdates(bestProvider, 20000, 0, this);
//        m_Map.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(location.getLatitude(), location.getLongitude())));
//        m_Map.animateCamera(CameraUpdateFactory.zoomTo(14));
        Log.d("Map", (location == null) ? "Location is null" : location.toString());
        return location;
    }

    private void zoomToCurrLocation() {
//        mMap.addMarker(new MarkerOptions().position(new LatLng(0, 0)).title("Marker").snippet("Snippet"));
//        Criteria criteria = new Criteria();
//        String provider = m_LocationManager.getBestProvider(criteria, true);
        if (m_Location != null) {
            LatLng target = new LatLng(m_Location.getLatitude(), m_Location.getLongitude());
//            CameraPosition position = this.mMap.getCameraPosition();
            CameraPosition.Builder builder = new CameraPosition.Builder();
            builder.zoom(12);
            builder.target(target);
            this.mMap.animateCamera(CameraUpdateFactory.newCameraPosition(builder.build()));
//            mMap.addMarker(new MarkerOptions().position(new LatLng(m_Location.getLatitude(), m_Location.getLongitude())).title("You are here!").snippet("Consider yourself located"));
        }
    }

    private void checkPhoneLocationProvided() {
        LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        boolean gps_enabled = false;
        boolean network_enabled = false;

        try {
            gps_enabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
        } catch(Exception ex) {}

        try {
            network_enabled = lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        } catch(Exception ex) {}

        if(!gps_enabled && !network_enabled) {
            // notify user
            AlertDialog.Builder dialog = new AlertDialog.Builder(MuveeMaps.this);
            dialog.setTitle("You location required");
            dialog.setMessage("Your GPS or Network is not enabled");
            dialog.setPositiveButton("Open Location Settings", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                    // TODO Auto-generated method stub
                    Intent myIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    startActivity(myIntent);
                    //get gps
                }
            });
            dialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                    // TODO Auto-generated method stub

                }
            });
            dialog.show();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.d("Map", "Permission granted");
                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                    updateLocation();
                    m_Location = getLocation();
                    zoomToCurrLocation();
                } else {
                    Log.d("Map", "Permission denied");
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }
            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

    @Override
    public void onLocationChanged(Location location) {
//        TextView locationTv = (TextView) findViewById(R.id.Text);
//        boolean bPass = (items.size() == 0);
        Log.d("Map", "Location has changed!!");
        m_Location = location;
//        double latitude = location.getLatitude();
//        double longitude = location.getLongitude();
//        if (bPass) {
//            //new GetPlaces(this).execute();
//            updateListView();
//        }
//        LatLng latLng = new LatLng(latitude, longitude);
        //m_Map.addMarker(new MarkerOptions().position(latLng));
//        m_Map.moveCamera(CameraUpdateFactory.newLatLng(latLng));
//        m_Map.animateCamera(CameraUpdateFactory.zoomTo(14));
//        locationTv.setText("Latitude:" + latitude + ", Longitude:" + longitude);
    }

    @Override
    public void onProviderDisabled(String provider) {
        // TODO Auto-generated method stub
    }

    @Override
    public void onProviderEnabled(String provider) {
        // TODO Auto-generated method stub
        Log.d("Map", "Provider enabled: " + provider);
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        // TODO Auto-generated method stub
        Log.d("Map", "Status changed: " + status);
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        Location location = getLocation();
        if (location != null) {
            onLocationChanged(location);
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(location.getLatitude(), location.getLongitude()), 12));
        }
//        // Add a marker in Sydney and move the camera
//        LatLng sydney = new LatLng(-34, 151);
//        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
//        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
    }

    public ArrayList<Place> findNearLocation()   {

        PlacesService service = new PlacesService("AIzaSyCmTPzZzpXu_rgORBjOFMZshx9EZ6xpnTw");
        /*
        Here you should call the method find nearest place near to central park new delhi
        then we pass the lat and lang of central park. here you can pass your current location lat and long.
        The third argument is used to set the specific place if you pass the atm the it will return the list of nearest atm list.
        If you want to get the every thing then you should be pass "" only
        */
        /* here you should be pass the you current location latitude and longitude, */
        List<Place> findPlaces = new ArrayList<>();
        if (null != m_Location) {
            findPlaces = service.findPlaces(m_Location.getLatitude(), m_Location.getLongitude(), "cinema");

//            m_Places = new String[findPlaces.size()];
//            m_URL = new String[findPlaces.size()];

            for (int i = 0; i < findPlaces.size(); i++) {

                Place placeDetail = findPlaces.get(i);
                placeDetail.getIcon();

                System.out.println(placeDetail.getName());
//                m_Places[i] =placeDetail.getName();

//                m_URL[i] =placeDetail.getIcon();
            }
        }
        return (ArrayList<Place>)findPlaces;
    }
}
