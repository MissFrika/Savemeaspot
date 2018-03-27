package sup.savemeaspot;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import java.util.ArrayList;

import sup.savemeaspot.DataLayer.Category;
import sup.savemeaspot.DataLayer.Coordinate;

public class SaveSpotActivity extends AppCompatActivity {

    private Coordinate coordinatesToSave = new Coordinate();
    public static FragmentManager fragmentManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_save_spot);
        fragmentManager = getSupportFragmentManager();

        if(findViewById(R.id.frame_container) != null){
            if(savedInstanceState != null){
                return;
            }
            fragmentManager.beginTransaction().add(R.id.frame_container, new CategoryListFragment()).commit();
        }



        //*TextView coordinates = (TextView) findViewById(R.id.coordinates);

        Bundle extra = getIntent().getExtras();
       //Kontrollerar om intent är null
        if(extra != null) {
            try {
                //Hämtar ut alla strings i intent
                double latitude = extra.getDouble("EXTRA_MESSAGE_COORDINATES_LAT");
                double longitude = extra.getDouble("EXTRA_MESSAGE_COORDINATES_LONG");
                String locale = extra.getString("EXTRA_MESSAGE_COORDINATES_LOCAL");
                String country = extra.getString("EXTRA_MESSAGE_COORDINATES_COUNTRY");

                //Konverterar Strings som skickats med intent till double
                coordinatesToSave.setLatitude(latitude);
                coordinatesToSave.setLongitude(longitude);
                coordinatesToSave.setLocalAddress(locale);
                coordinatesToSave.setCountryName(country);


                //coordinates.setText(coordinatesToSave.getCountryName());
            }
            catch(NullPointerException e)
            {
                //coordinates.setText("Something went wrong");
            }
        }

    }


}
