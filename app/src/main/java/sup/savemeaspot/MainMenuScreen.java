package sup.savemeaspot;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainMenuScreen extends AppCompatActivity {


    /**
     * Skapar aktiviteten MainMenuScreen
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu_screen);


        if(getIntent().hasExtra("EXTRA_MESSAGE_COORDINATES_LAT")&& getIntent().hasExtra("EXTRA_MESSAGE_COORDINATES_LONG")) {

            Bundle extras = getIntent().getExtras();
            double lat = extras.getDouble("EXTRA_MESSAGE_COORDINATES_LAT");
            double lon = extras.getDouble("EXTRA_MESSAGE_COORDINATES_LONG");



        }
        else{

        }

    }
}
