package sup.savemeaspot;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.MapStyleOptions;

import java.io.IOException;
import java.util.List;

import sup.savemeaspot.DataLayer.Coordinate;
import sup.savemeaspot.DataLayer.DatabaseHandler;

/**
 * Main Activity. Visar en karta med markörer för användarens nuvarande position och sparade Spots. Kod för Google Maps är pre-made och hämtat från https://developers.google.com/maps/documentation/android-api/start
 */
public class MapsStart extends FragmentActivity implements OnMapReadyCallback{

    private static final int PERMISSION_REQUEST_ACCESS_FINE_LOCATION = 0;
    private GoogleMap mMap;
    private LocationManager locationManager;
    private boolean permissionsGranted = false;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1234;

    //Koordinatobjekt
    private Coordinate currentCoordinate = new Coordinate();

    private static final int ERROR_DIALOG_REQUEST = 9001;

    private static final String simpl_MS = MapsStart.class.getSimpleName();
    public static final String EXTRA_MESSAGE_COORDINATES = "sup.savemeaspot.COORDINATES";



    @Override
    protected void onCreate(Bundle savedInstanceState) {

        DatabaseHandler db = new DatabaseHandler(this);
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_maps_start);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        //Kontrollerar permissions
        checkPermissions();
        mapFragment.getMapAsync(this);

        //Hämtar en användares position och uppdaterar positionen på kartan
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
        //Kollar om det finns tillgång till en nätverkstjänst
        if (locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)){
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 1, new LocationListener() {
                @Override
                public void onLocationChanged(Location location) {
                    //Hämta latitud och longitud
                    double latitude = location.getLatitude();
                    double longitude = location.getLongitude();
                    //Ny instans av LatLng, håller doubles med latitud och longitud
                    LatLng latLng = new LatLng(latitude,longitude);

                    Geocoder geocoder = new Geocoder(getApplicationContext());
                    //En lista av adresser som skall hämtas från en användares positionen
                    try {
                        List<Address> adresses = geocoder.getFromLocation(latitude,longitude,1);

                        //För att hämta ut adresserna, i detta fall lokal adress och land
                        String adr = adresses.get(0).getLocality() + ", ";
                        adr += adresses.get(0).getCountryName();

                        currentCoordinate.setLatitude(latLng.latitude);
                        currentCoordinate.setLongitude(latLng.longitude);
                        currentCoordinate.setLocalAddress(adresses.get(0).getLocality());
                        currentCoordinate.setCountryName(adresses.get(0).getCountryName());

                        //Markör på användarens position.
                        mMap.addMarker(new MarkerOptions().position(latLng).title(adr));

                        //Kameraposition på markör
                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15.2f));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }

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
        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER))
        {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, new LocationListener() {
                @Override
                public void onLocationChanged(Location location) {
                    //Hämta latitud och longitud
                    double latitude = location.getLatitude();
                    double longitude = location.getLongitude();
                    //Ny instans av objekt LatLing,
                    LatLng latLng = new LatLng(latitude,longitude);
                    Geocoder geocoder = new Geocoder(getApplicationContext());
                    //En lista av adresser som skall hämtas från en användares positionen
                    try {
                        List<Address> adresses = geocoder.getFromLocation(latitude,longitude,1);

                        //För att hämta ut adresserna, i detta fall lokal adress och land
                        String adr = adresses.get(0).getLocality() + ", ";
                        adr += adresses.get(0).getCountryName();
                        currentCoordinate.setLatitude(latLng.latitude);
                        currentCoordinate.setLongitude(latLng.longitude);
                        currentCoordinate.setLocalAddress(adresses.get(0).getLocality());
                        currentCoordinate.setCountryName(adresses.get(0).getCountryName());

                        //Markör på användarens position.
                        mMap.addMarker(new MarkerOptions().position(latLng).title(adr));

                        //Kameraposition på markör
                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 12.2f));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }

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
    private void checkPermissions(){
        String[] permissions = {Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION};
        if(ContextCompat.checkSelfPermission(this.getApplicationContext(),
                Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            if (ContextCompat.checkSelfPermission(this.getApplicationContext(),
                    Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                permissionsGranted = true;
            } else {
                ActivityCompat.requestPermissions(this, permissions, LOCATION_PERMISSION_REQUEST_CODE);
            }
        }
        else{ActivityCompat.requestPermissions(this, permissions,LOCATION_PERMISSION_REQUEST_CODE);
        }
    }

    /**
     * Kontrollerar om Google Services fungerar korrekt
     * @return
     */
    public boolean checkGoogleService(){
        int isAvailable = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(MapsStart.this);

        //Google Services fungerar
        if(isAvailable == ConnectionResult.SUCCESS){
            return true;
        }
        //Google Services fungerar ej, men kan åtgärdas
        else if(GoogleApiAvailability.getInstance().isUserResolvableError(isAvailable)){
            Dialog googleServiceDialog = GoogleApiAvailability.getInstance().getErrorDialog(MapsStart.this, isAvailable, ERROR_DIALOG_REQUEST);
            googleServiceDialog.show();
        }
        else{
            Toast.makeText(this, "Unable to request map", Toast.LENGTH_SHORT).show();
        }
        return false;
    }

    /** Detta körs vid Menu-knapptryck, öppnar huvudmenyn*/
    public void openMainMenu(View view){
        //Ny intent
        Intent intent = new Intent(this, MainMenuScreen.class);
        //Nytt koordinatobjet från nuvarande koordinater
        double lat = currentCoordinate.getLatitude();
        double lon = currentCoordinate.getLongitude();

        //Skickar med ett koordinatobjekt, konverterat till String
        intent.putExtra("EXTRA_MESSAGE_COORDINATES_LAT", lat);
        intent.putExtra("EXTRA_MESSAGE_COORDINATES_LONG", lon);


        startActivity(intent);
    }

    /** Öppnar en ny dialogruta för att spara en Spot,
     * skickar med koordinater till den nya aktiviteten
     */
    public void saveSpotDialogueView(View view){
        checkGoogleService();

        Intent intent = new Intent(this, SaveSpotCategoryActivity.class);

        //Nytt koordinatobjet från nuvarande koordinater
        double lat = currentCoordinate.getLatitude();
        double lon = currentCoordinate.getLongitude();
        String locality = currentCoordinate.getLocalAddress();
        String country = currentCoordinate.getCountryName();

        //Skickar med ett koordinatobjekt, konverterat till String
        intent.putExtra("EXTRA_MESSAGE_COORDINATES_LAT", lat);
        intent.putExtra("EXTRA_MESSAGE_COORDINATES_LONG", lon);
        intent.putExtra("EXTRA_MESSAGE_COORDINATES_LOCAL", locality);
        intent.putExtra("EXTRA_MESSAGE_COORDINATES_COUNTRY", country);

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
        mMap = googleMap;
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
}