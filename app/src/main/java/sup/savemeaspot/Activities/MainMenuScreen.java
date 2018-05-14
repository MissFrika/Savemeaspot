package sup.savemeaspot.Activities;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import sup.savemeaspot.R;

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
        //Instansiera knappar
        openMySpots();
        openCategoryCollection();
        openEditSpots();
        closeMenu();
    }

    /**
     * Öppna SpotCollectionActivity
     */
    private void openMySpots() {
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

    /**
     * Öppna CategoryCollectionActivity
     */
    private void openCategoryCollection(){
        ImageButton btn = findViewById(R.id.categories_button);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, CategoryCollectionActivity.class);
                startActivity(intent);
            }
        });
    }

    /**
     * Öppna EditSpotsActivity
     */
    private void openEditSpots(){
        ImageButton btn = findViewById(R.id.edit_spots_button);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, EditSpotsActivity.class);
                startActivity(intent);
            }
        });
    }

    /**
     * Stäng menyaktiviteten
     */
    private void closeMenu(){
        ImageButton btn = findViewById(R.id.close_button);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MainMenuScreen.this.finish();
            }
        });
    }

}
