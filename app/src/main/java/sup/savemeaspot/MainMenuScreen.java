package sup.savemeaspot;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

public class MainMenuScreen extends AppCompatActivity {

    private Context context;

    /**
     * Skapar aktiviteten MainMenuScreen
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu_screen);
        this.context = getApplicationContext();
        openMySpots();

        if(getIntent().hasExtra("EXTRA_MESSAGE_COORDINATES_LAT")&& getIntent().hasExtra("EXTRA_MESSAGE_COORDINATES_LONG")) {

            Bundle extras = getIntent().getExtras();
            double lat = extras.getDouble("EXTRA_MESSAGE_COORDINATES_LAT");
            double lon = extras.getDouble("EXTRA_MESSAGE_COORDINATES_LONG");

        }
        else{

        }

    }
    public void openMySpots() {
        //Ny intent
        ImageButton btn = findViewById(R.id.my_spots_button);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, SpotCollectionActivity.class);
                startActivity(intent);
            }
        });

    }
}
