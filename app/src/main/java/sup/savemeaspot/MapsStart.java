package sup.savemeaspot;

import android.app.ActionBar;
import android.content.Intent;
import android.content.res.Resources;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.MapStyleOptions;

import java.lang.reflect.Array;

public class MapsStart extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private static final String simpl_MS = MapsStart.class.getSimpleName();
    public static final String EXTRA_MESSAGE = "sup.savemeaspot.COORDINATES";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps_start);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    /** Detta körs vid Menu-knapptryck, öppnar huvudmenyn*/
    public void openMainMenu(View view){
        //Ny intent
        Intent intent = new Intent(this, MainMenuScreen.class);
        startActivity(intent);
    }

    /** Öppnar en ny dialogruta för att spara en Spot,
     * skickar med koordinater till den nya aktiviteten
     */
    public void saveSpotDialogueView(View view){
        Intent intent = new Intent(this, SaveSpotCategory.class);
        //Array coordinates = getCurrentCoordinates();
        //intent.putExtra("COORDINATES", coordinates);
        startActivity(intent);
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

        try {
            // Customise the styling of the base map using a JSON object defined
            // in a raw resource file.
            boolean success = googleMap.setMapStyle(
                    MapStyleOptions.loadRawResourceStyle(
                            this, R.raw.style_json));

            if (!success) {
                Log.e(simpl_MS, "Style parsing failed.");
            }
        } catch (Resources.NotFoundException e) {
            Log.e(simpl_MS, "Can't find style. Error: ", e);
        }
        // Camera position Örebro University.
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(59.254974,15.249242)));
    }
}
