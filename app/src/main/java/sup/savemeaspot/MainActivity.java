package sup.savemeaspot;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;

public class MainActivity extends AppCompatActivity {

    private static final int PERMISSION_REQUEST_ACCESS_FINE_LOCATION = 0;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1234;
    private static final int ERROR_DIALOG_REQUEST = 9001;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                PERMISSION_REQUEST_ACCESS_FINE_LOCATION);
        if(checkGoogleService()) {
            openMapsStart();
        }

    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case LOCATION_PERMISSION_REQUEST_CODE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0){
                    for(int i =0; i < grantResults.length; i++){
                        if(grantResults[i] != PackageManager.PERMISSION_GRANTED){

                            return;
                        }
                    }

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
     * Kontrollerar om Google Services fungerar korrekt
     * @return
     */
    private boolean checkGoogleService() {
        int isAvailable = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(MainActivity.this);

        //Google Services fungerar
        if (isAvailable == ConnectionResult.SUCCESS) {
            return true;
        }
        //Google Services fungerar ej, men kan åtgärdas
        else if (GoogleApiAvailability.getInstance().isUserResolvableError(isAvailable)) {
            Dialog googleServiceDialog = GoogleApiAvailability.getInstance().getErrorDialog(MainActivity.this, isAvailable, ERROR_DIALOG_REQUEST);
            googleServiceDialog.show();
        } else {
            Toast.makeText(this, "Unable to request map", Toast.LENGTH_SHORT).show();
        }
        return false;
    }

    private void openMapsStart() {
        Button btnMap = (Button) findViewById(R.id.launch_button);
        btnMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Ny intent
                Intent intent = new Intent(MainActivity.this, MapsStart.class);

                startActivity(intent);
            }
        });
    }

}
