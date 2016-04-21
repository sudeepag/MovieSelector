package edu.gatech.logitechs.movieselector.View;

import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.List;

import edu.gatech.logitechs.movieselector.R;

public class MuveeMaps extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private List<String> items;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.muvee_maps_activity);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        items = new ArrayList<>();
        items.add("simple text aaaaaaa");
        items.add("simple text aaaaaaa");
        items.add("simple text aaaaaaa");
        items.add("simple text aaaaaaa");
        items.add("simple text aaaaaaa");
        items.add("simple text aaaaaaa");
        items.add("simple text aaaaaaa");
        items.add("simple text aaaaaaa");
        items.add("simple text aaaaaaa");
        items.add("simple text aaaaaaa");
        items.add("simple text aaaaaaa");
        items.add("simple text aaaaaaa");items.add("simple text aaaaaaa");items.add("simple text aaaaaaa");items.add("simple text aaaaaaa");

        updateListView();

        final FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

            }
        });

    }

    public void updateListView() {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, items);
        adapter.notifyDataSetChanged();
        ListView list = (ListView) findViewById(R.id.listView);
        list.setAdapter(adapter);
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

        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(-34, 151);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
    }
}
