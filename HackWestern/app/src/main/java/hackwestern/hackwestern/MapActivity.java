package hackwestern.hackwestern;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Point;
import android.location.Address;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.widget.EditText;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.Date;
import java.util.List;

/**
 * Created by Rebecca on 3/29/2015.
 */
public class MapActivity extends FragmentActivity implements OnMapReadyCallback {
    EditText searchbar;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location);

        MapFragment mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

    }



    @Override
    public void onMapReady(final GoogleMap map) {
        map.setMapType(GoogleMap.MAP_TYPE_HYBRID);

        map.setOnMapClickListener(new GoogleMap.OnMapClickListener() {

            @Override
            public void onMapClick(LatLng point) {
                //myMap.addMarker(new MarkerOptions().position(point).title(point.toString()));

                //The code below demonstrate how to convert between LatLng and Location

                //Convert LatLng to Location
                Location location = new Location("Test");
                String lat = String.valueOf(point.latitude);
                String lng = String.valueOf(point.longitude);

                searchbar = (EditText) findViewById(R.id.mapSearchBox);
                searchbar.setText(lat + " " + lng);

                //Convert Location to LatLng
                LatLng newLatLng = new LatLng(location.getLatitude(), location.getLongitude());

                MarkerOptions markerOptions = new MarkerOptions()
                        .position(newLatLng)
                        .title(newLatLng.toString());

                map.addMarker(markerOptions);

                Intent resultData = new Intent();
                resultData.putExtra("longitude", lng);
                resultData.putExtra("latitude", lat);
                setResult(Activity.RESULT_OK, resultData);
                finish();

            }
        });

    }
}
