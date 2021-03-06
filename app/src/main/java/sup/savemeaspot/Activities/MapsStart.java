package sup.savemeaspot.Activities;

import android.Manifest;
import android.arch.persistence.room.Room;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import sup.savemeaspot.Adapters.CustomMapInfoWindowAdapter;
import sup.savemeaspot.DataLayer.Models.Coordinate;
import sup.savemeaspot.DataLayer.DatabaseHandler;
import sup.savemeaspot.DataLayer.DatabaseHelper;
import sup.savemeaspot.DataLayer.Models.MarkerInfo;
import sup.savemeaspot.DataLayer.Models.Spot;
import sup.savemeaspot.DataLayer.SpotDatabase;
import sup.savemeaspot.R;

/**
 * Main Activity. Visar en karta med markörer för användarens nuvarande position och sparade Spots. Kod för Google Maps är pre-made och hämtat från https://developers.google.com/maps/documentation/android-api/start
 */
public class MapsStart extends FragmentActivity implements OnMapReadyCallback {

    private static final int PERMISSION_REQUEST_ACCESS_FINE_LOCATION = 0;
    private static final int PERMISSION_REQUEST_ACCESS_NETWORK_STATE = 0;
    private GoogleMap mMap;
    private Context context;
    private boolean activityStarted = true;
    private static final int zoomLevel = 15;
    private LocationManager locationManager;
    private boolean permissionsGranted = false;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1234;

    //Koordinatobjekt
    private Coordinate currentCoordinate = new Coordinate();

    private FusedLocationProviderClient mFusedLocationClient;

    private static final String simpl_MS = MapsStart.class.getSimpleName();


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        //Instansiera databas
        DatabaseHandler db = new DatabaseHandler(this);
        super.onCreate(savedInstanceState);
        context = this;

        setContentView(R.layout.activity_maps_start);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        //Kontrollerar permissions
        checkPermissions();
        //Instansiera knappar
        openMainMenu();
        saveSpotDialogueView();
        refreshSpotButton();

        ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                PERMISSION_REQUEST_ACCESS_FINE_LOCATION);
        String context = Context.LOCATION_SERVICE;
        locationManager = (LocationManager) getSystemService(context);
        //Hur ofta plats skall hämtas
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }


        //Hämtar en användares position och uppdaterar positionen på kartan
        //Kollar om det finns tillgång till en nätverkstjänst
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_NETWORK_STATE},
                PERMISSION_REQUEST_ACCESS_NETWORK_STATE);
        if (locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 1, new LocationListener() {
                @Override
                public void onLocationChanged(Location location) {
                    //Hämta latitud och longitud
                    double latitude = location.getLatitude();
                    double longitude = location.getLongitude();
                    //Ny instans av LatLng, håller doubles med latitud och longitud
                    LatLng latLng = new LatLng(latitude, longitude);

                    Geocoder geocoder = new Geocoder(getApplicationContext());
                    //En lista av adresser som skall hämtas från en användares positionen
                    try {
                        List<Address> addresses = geocoder.getFromLocation(latitude, longitude, 1);

                        //För att hämta ut adresserna, i detta fall lokal adress och land
                        currentCoordinate.setLatitude(latLng.latitude);
                        currentCoordinate.setLongitude(latLng.longitude);
                        if (addresses != null && addresses.size() > 0) {
                            currentCoordinate.setLocalAddress(addresses.get(0).getLocality());
                            currentCoordinate.setCountryName(addresses.get(0).getCountryName());
                        }

                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    if (activityStarted) {
                        do {
                            CameraUpdate userLocation = CameraUpdateFactory.newLatLngZoom(latLng, zoomLevel);
                            mMap.animateCamera(userLocation);
                            activityStarted = false;
                        } while (activityStarted);
                    }
                }

                //Auto-genererade metoder
                @Override
                public void onStatusChanged(String s, int i, Bundle bundle) {

                }

                @Override
                public void onProviderEnabled(String s) {

                }

                @Override
                public void onProviderDisabled(String s) {

                }
            });
        }
        //Om inget nätverk är tillgängligt, kontrollera GPS-tillgänglighet
        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 1, new LocationListener() {
                @Override
                public void onLocationChanged(Location location) {
                    //Hämta latitud och longitud
                    double latitude = location.getLatitude();
                    double longitude = location.getLongitude();
                    //Ny instans av objekt LatLing,
                    LatLng latLng = new LatLng(latitude, longitude);
                    Geocoder geocoder = new Geocoder(getApplicationContext());
                    //En lista av adresser som skall hämtas från en användares positionen
                    try {
                        List<Address> addresses = geocoder.getFromLocation(latitude, longitude, 1);

                        //För att hämta ut adresserna, i detta fall lokal adress och land
                        currentCoordinate.setLatitude(latLng.latitude);
                        currentCoordinate.setLongitude(latLng.longitude);
                        if (addresses != null && addresses.size() > 0) {
                            currentCoordinate.setLocalAddress(addresses.get(0).getLocality());
                            currentCoordinate.setCountryName(addresses.get(0).getCountryName());
                        }

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    if(activityStarted) {
                        do {
                            CameraUpdate userLocation = CameraUpdateFactory.newLatLngZoom(latLng, zoomLevel);
                            mMap.animateCamera(userLocation);
                            activityStarted = false;
                        } while (activityStarted);
                    }
                }

                //Auto-genererade metoder
                @Override
                public void onStatusChanged(String s, int i, Bundle bundle) {

                }

                @Override
                public void onProviderEnabled(String s) {

                }

                @Override
                public void onProviderDisabled(String s) {

                }
            });

        }
    }

    /**
     * Kontrollerar att permissions finns
     */
    private void checkPermissions() {
        String[] permissions = {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION};
        if (ContextCompat.checkSelfPermission(this.getApplicationContext(),
                Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            if (ContextCompat.checkSelfPermission(this.getApplicationContext(),
                    Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                permissionsGranted = true;
            } else {
                ActivityCompat.requestPermissions(this, permissions, LOCATION_PERMISSION_REQUEST_CODE);
            }
        } else {
            ActivityCompat.requestPermissions(this, permissions, LOCATION_PERMISSION_REQUEST_CODE);
        }
    }


    /** Detta körs vid Menu-knapptryck, öppnar huvudmenyn*/
    private void openMainMenu() {
        //Instansiera knapp
        Button btn = findViewById(R.id.menuButton);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Ny intent
                Intent intent = new Intent(getApplicationContext(), MainMenuScreen.class);
                //Nytt koordinatobjet från nuvarande koordinater
                double lat = currentCoordinate.getLatitude();
                double lon = currentCoordinate.getLongitude();

                //Skickar med ett koordinatobjekt, konverterat till String
                intent.putExtra("EXTRA_MESSAGE_COORDINATES_LAT", lat);
                intent.putExtra("EXTRA_MESSAGE_COORDINATES_LONG", lon);

                startActivity(intent);
            }
        });
    }

    /**
     * Kontrollerar senast kända position...OBS! GPS måste vara aktiv
     */
    public void checkLastLocation(){
        //Geocoder
        final Geocoder geocoder = new Geocoder(getApplicationContext());
        //Senaste kända platsen
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        mFusedLocationClient.getLastLocation().addOnSuccessListener(this, new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                currentCoordinate.setLatitude(location.getLatitude());
                currentCoordinate.setLongitude(location.getLongitude());

                try {
                    List<Address> adresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                    currentCoordinate.setCountryName(adresses.get(0).getCountryName());
                    currentCoordinate.setLocalAddress(adresses.get(0).getLocality());

                } catch (IOException e) {
                    e.printStackTrace();
                }
                LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoomLevel));
            }
        });
    }

    /** Öppnar en ny dialogruta för att spara en Spot,
     * skickar med koordinater till den nya aktiviteten
     */
    private void saveSpotDialogueView() {
        Button btn = findViewById(R.id.map_Savebtn);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, SaveSpotCategoryActivity.class);
                //Put extra
                putExtraCoordinateIntent(intent);
                startActivity(intent);
            }
        });
    }

    /**
     * Rensa kartan och lägg till alla Spots igen
     */
    private void refreshSpotButton(){
        ImageButton btn = findViewById(R.id.refreshMapButton);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    mMap.clear();
                    List<Spot> spots;
                    if (DatabaseHelper.checkIfSpotsExist(getApplicationContext())) {
                        spots = DatabaseHelper.getAllSpots(getApplicationContext());

                        for (final Spot spot : spots) {
                            addSpotMarker(mMap, spot);
                        }
                    }
                }
                catch (Exception e){
                    e.printStackTrace();
                }
        }
        });
    }

    /**
     * Extra intent-messages för Coordinate
     */
    private void putExtraCoordinateIntent(Intent intent){
        //Nytt koordinatobjet från nuvarande koordinater
        double lat = currentCoordinate.getLatitude();
        double lon = currentCoordinate.getLongitude();
        String locality;
        String country;
        //Om lokal adress ej finns
        if (currentCoordinate.getLocalAddress() != null) {
             locality = currentCoordinate.getLocalAddress();
        }
        else{
            locality = "Unknown";
        }
        //Om land ej finns
        if(currentCoordinate.getCountryName() != null){
            country = currentCoordinate.getCountryName();
        }
        else{
            country = "Unknown";
        }
        //Skickar med information om ett koordinatobjekt
        intent.putExtra("EXTRA_MESSAGE_COORDINATES_LAT", lat);
        intent.putExtra("EXTRA_MESSAGE_COORDINATES_LONG", lon);
        intent.putExtra("EXTRA_MESSAGE_COORDINATES_LOCAL", locality);
        intent.putExtra("EXTRA_MESSAGE_COORDINATES_COUNTRY", country);
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
        //Markör på användarens position.
        if (ActivityCompat.checkSelfPermission(MapsStart.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(MapsStart.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            return;
        }
        mMap.setMyLocationEnabled(true);


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
        //Skapa markörer på kartan om Spots finns
        try{
            List<Spot> spots;
            if (DatabaseHelper.checkIfSpotsExist(getApplicationContext())) {
                if(checkIncomingSpotIntent() != null){
                    spots = checkIncomingSpotIntent();
                }
                else {
                    spots = DatabaseHelper.getAllSpots(getApplicationContext());
                }

                for (final Spot spot : spots) {
                    addSpotMarker(mMap, spot);
                }
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }

        //Zooma till en spot om intent skickas från SpotCollectionActivity
        if(getIntent().hasExtra("EXTRA_MESSAGE_COORDINATES_LAT") && getIntent().hasExtra("EXTRA_MESSAGE_COORDINATES_LONG")){
            zoomToSpot(mMap);
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        permissionsGranted = false;
        switch (requestCode) {
            case LOCATION_PERMISSION_REQUEST_CODE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0){
                    for(int i =0; i < grantResults.length; i++){
                        if(grantResults[i] != PackageManager.PERMISSION_GRANTED){
                            permissionsGranted = false;
                            return;
                        }
                    }
                    permissionsGranted = true;
                }
                else {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.

                }
                return;
            }
            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

    /**
     * Lägg till en markör för en Spot på kartan
     * @param googleMap
     * @param spot
     */
    private void addSpotMarker(GoogleMap googleMap, Spot spot){
        if(DatabaseHelper.checkIfSpotsExist(getApplicationContext())){
            String spotTitle = spot.getSpotTitle();
            String spotDescription = spot.getSpotDescription();

            //Koordinater
            Coordinate coordinate = DatabaseHelper.getSpotCoordinates(getApplicationContext(), spot);
            LatLng latlng = new LatLng(coordinate.getLatitude(),coordinate.getLongitude());

            //Kontrollera typ av kategori för att ändra markörens ikon
            int drawableMarker = R.drawable.map_marker_default;
            SpotDatabase database = Room.databaseBuilder(getApplicationContext(), SpotDatabase.class, "SpotDatabase")
                    .allowMainThreadQueries()
                    .build();
            int categoryDrawableId = database.categoryDao().getSpotCategory(spot.getSpotCategory()).getCategoryImg();
            String categoryName = database.categoryDao().getSpotCategory(spot.getSpotCategory()).getCategoryName();
            database.close();
            switch(categoryDrawableId){
                case R.drawable.apple :
                    drawableMarker = R.drawable.apple_marker;
                    break;
                case R.drawable.building :
                    drawableMarker = R.drawable.buildings_marker;
                    break;
                case R.drawable.cherry :
                    drawableMarker = R.drawable.cherry_marker;
                    break;
                case R.drawable.fish :
                    drawableMarker = R.drawable.fish_marker;
                    break;
                case R.drawable.fire :
                    drawableMarker = R.drawable.fire_marker;
                    break;
                case R.drawable.water :
                    drawableMarker = R.drawable.water_marker;
                    break;
                case R.drawable.wheat :
                    drawableMarker = R.drawable.wheat_marker;
                    break;
                case R.drawable.heart :
                    drawableMarker = R.drawable.heart_marker;
                    break;
                case R.drawable.mushroom :
                    drawableMarker = R.drawable.mushroom_marker;
                    break;
                case R.drawable.star :
                    drawableMarker = R.drawable.star_marker;
                    break;
                case R.drawable.bus :
                    drawableMarker = R.drawable.bus_marker;
                    break;
                case R.drawable.train :
                    drawableMarker = R.drawable.train_marker;
                    break;
                case R.drawable.boat :
                    drawableMarker = R.drawable.boat_marker;
                    break;
                case R.drawable.plane :
                    drawableMarker = R.drawable.plane_marker;
                    break;
                }
            //Markör, skapa nytt objekt med snippetinfo
            //GSON-biblioteket konverterar ett javaobjekt till JSON
            Gson gson = new Gson();
            MarkerInfo markerInfo = new MarkerInfo();
            markerInfo.setTitle(spotTitle);
            markerInfo.setCategory(categoryName);
            markerInfo.setDescription(spotDescription);
            markerInfo.setLocale(coordinate.getLocalAddress());
            markerInfo.setCountry(coordinate.getCountryName());
            String markerInfoString = gson.toJson(markerInfo);
            MarkerOptions markerOptions = new MarkerOptions().position(latlng).title(spotTitle).snippet(markerInfoString).icon(BitmapDescriptorFactory.fromResource(drawableMarker));
            Marker marker = googleMap.addMarker(markerOptions);
            googleMap.setInfoWindowAdapter(new CustomMapInfoWindowAdapter(this, marker));
        }
    }


    /**
     * Tar ett intent om detta finns och zoomar in på en Spots koordinater
     * @param googleMap
     */
    private void zoomToSpot(GoogleMap googleMap){
        if(getIntent().hasExtra("EXTRA_MESSAGE_COORDINATES_LAT")
                && getIntent().hasExtra("EXTRA_MESSAGE_COORDINATES_LONG")){
            GoogleMap mMap = googleMap;
            Bundle extras = getIntent().getExtras();
            double latitude = extras.getDouble("EXTRA_MESSAGE_COORDINATES_LAT");
            double longitude = extras.getDouble("EXTRA_MESSAGE_COORDINATES_LONG");

            //Flytta kamera till latlng
            LatLng latLng = new LatLng(latitude,longitude);
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoomLevel));
            //Aktiviteten körs inte från Launch, autouppdatera inte plats
            activityStarted = false;
        }
    }

    /**
     * Kontrollera om intent skickas från CategoryCollection
     * @return
     */
    private ArrayList<Spot> checkIncomingSpotIntent(){
        ArrayList<Spot> spots = null;
        if(getIntent().hasExtra("EXTRA_MESSAGE_SPOTS_IN_CATEGORY")){
            Bundle extras = getIntent().getExtras();
            spots = (ArrayList<Spot>) extras.get("EXTRA_MESSAGE_SPOTS_IN_CATEGORY");
        }
        return spots;
    }
}