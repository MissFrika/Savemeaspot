package sup.savemeaspot;

import android.arch.persistence.room.Room;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import java.util.List;

import sup.savemeaspot.DataLayer.Category;
import sup.savemeaspot.DataLayer.DatabaseHandler;
import sup.savemeaspot.DataLayer.DatabaseInitializer;
import sup.savemeaspot.DataLayer.SpotDatabase;

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
