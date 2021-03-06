package sup.savemeaspot.Activities;

import android.arch.persistence.room.Room;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import sup.savemeaspot.DataLayer.Models.Category;
import sup.savemeaspot.DataLayer.Models.Coordinate;
import sup.savemeaspot.DataLayer.DatabaseHelper;
import sup.savemeaspot.DataLayer.Models.Spot;
import sup.savemeaspot.DataLayer.SpotDatabase;
import sup.savemeaspot.R;
import sup.savemeaspot.Adapters.TitleRecyclerViewAdapter;

public class SaveTitleActivity extends AppCompatActivity {

    private List<String> exampleTitles = new ArrayList<String>();
    private String[] exampleStrings;
    private Category chosenCategory;
    private Context context;
    private String selectedTitle ="";
    private Spot spotToSave = new Spot();
    private Coordinate coordinates;

    public SaveTitleActivity() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_save_title);
        this.context = getApplicationContext();

        //Instansiera sparaknappen
        saveSpotButton();
        checkIncomingIntents();
        //Kontrollerar vilken kategori som valts från SaveSpotCategoryActivity och instansierar "titlesToDisplay"
        if (!this.chosenCategory.getCategoryName().isEmpty() && (this.chosenCategory.getCategoryName().equals("Fruit") || this.chosenCategory.getCategoryName().equals("Mushroom")
        || this.chosenCategory.getCategoryName().equals("Fish") || this.chosenCategory.getCategoryName().equals("Berry"))) {

            switch (chosenCategory.getCategoryName()) {
                case "Mushroom":
                    exampleStrings = getResources().getStringArray(R.array.titles_example_mushroom);
                    break;
                case "Fish":
                    exampleStrings = getResources().getStringArray(R.array.titles_example_fish);
                    break;
                case "Berry":
                    exampleStrings = getResources().getStringArray(R.array.titles_example_berry);
                    break;
                case "Fruit":
                    exampleStrings = getResources().getStringArray(R.array.titles_example_fruit);
                    break;
                case "":
                    exampleStrings = new String[]{"Could not load titles"};
                    break;
            }
            for (String item : exampleStrings) {
                exampleTitles.add(item);
            }
        }
        else{
            exampleTitles.add("My Spot");
        }

        // Specifierar en adapter för RecyclerView
        RecyclerView.Adapter adapter = new TitleRecyclerViewAdapter(exampleTitles);
        RecyclerView recyclerView = findViewById(R.id.title_recycler_view);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        //LayoutManager
        recyclerView.setLayoutManager(layoutManager);
        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        recyclerView.setHasFixedSize(true);

        recyclerView.setAdapter(adapter);

    }

    /**
     * Kontrollerar om det finns intents medskickade från föregående aktivitet
     */
    private void checkIncomingIntents() {
        if (getIntent().hasExtra("EXTRA_MESSAGE_CATEGORY_ID") && getIntent().hasExtra("EXTRA_MESSAGE_CATEGORY_NAME") &&
                getIntent().hasExtra("EXTRA_MESSAGE_CATEGORY_IMG") && getIntent().hasExtra("EXTRA_MESSAGE_CATEGORY_IS_DELETABLE") && getIntent().hasExtra("EXTRA_MESSAGE_COORDINATES_LAT")
                && getIntent().hasExtra("EXTRA_MESSAGE_COORDINATES_LONG") && getIntent().hasExtra("EXTRA_MESSAGE_COORDINATES_LOCAL") && getIntent().hasExtra("EXTRA_MESSAGE_COORDINATES_COUNTRY")) {
            Bundle extra = getIntent().getExtras();

            //Category-intent message
            int id = extra.getInt("EXTRA_MESSAGE_CATEGORY_ID");
            String name = extra.getString("EXTRA_MESSAGE_CATEGORY_NAME");
            int image = extra.getInt("EXTRA_MESSAGE_CATEGORY_IMG");
            int isDeletable = extra.getInt("EXTRA_MESSAGE_CATEGORY_IS_DELETABLE");
            this.chosenCategory = new Category(id, name, image, isDeletable);
            //Coordinate-intent message
            double latitude = extra.getDouble("EXTRA_MESSAGE_COORDINATES_LAT");
            double longitude = extra.getDouble("EXTRA_MESSAGE_COORDINATES_LONG");
            String locale = extra.getString("EXTRA_MESSAGE_COORDINATES_LOCAL");
            String country = extra.getString("EXTRA_MESSAGE_COORDINATES_COUNTRY");
            this.coordinates = new Coordinate(latitude ,longitude, locale, country);

        }
    }

    /**
     * Metod för att spara en Spot till databasen vid ett OnClick-event
     */
    private void saveSpotButton() {
        //Ny button
        ImageButton btn = this.findViewById(R.id.save_title_button);
        //Ny OnClickListener för btn
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //TextView för att ange titel
                TextView textView = (TextView) findViewById(R.id.editTitle);
                Boolean hasTitle = false;

                if(!(textView.getText().toString()).isEmpty() && !(textView.getText().toString().equals(" "))){
                    //Textfältet är ifyllt
                    spotToSave.setSpotTitle(textView.getText().toString());
                    hasTitle = true;
                }
                else if(!selectedTitle.isEmpty()){
                    //Titel är markerad i listan och textfältet är tomt
                    spotToSave.setSpotTitle(selectedTitle);
                    hasTitle = true;
                }
                else {
                    //Ingen titel är angiven
                    Toast.makeText(context, "You must choose a title for your Spot", Toast.LENGTH_LONG).show();
                }

                //Om Spot har en titel
                if(hasTitle) {
                    try {

                        //Spara koordinater
                        DatabaseHelper.insertCoordinate(context, coordinates);

                        //EditText för att ange beskrivning
                        EditText editView = findViewById(R.id.addDescription);
                        String description = editView.getText().toString();
                        //Sätt beskrivning
                        spotToSave.setSpotDescription(description);
                        //Sätt kategori
                        spotToSave.setSpotCategory(chosenCategory.getCategoryId());
                        //Sätt koordinater
                        SpotDatabase database = Room.databaseBuilder(context.getApplicationContext(), SpotDatabase.class,"SpotDatabase")
                                .allowMainThreadQueries() //DO NOT !!!
                                .build();
                        int coordinateToSave = database.coordinateDao().getLastRecordCoordinates();
                        //Stäng db
                        database.close();
                        spotToSave.setSpotCoordinate(coordinateToSave);

                        //SpotDao
                        DatabaseHelper.insertSpot(context, spotToSave);
                        Toast.makeText(context, spotToSave.getSpotTitle() + " has successfully been saved!", Toast.LENGTH_LONG).show();

                        //Avsluta aktivitet och öppna MapsStart
                        Intent intent = new Intent(context, MapsStart.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        ActivityCompat.finishAffinity(SaveTitleActivity.this);
                        startActivity(intent);

                    } catch (Exception e) {

                        Toast.makeText(context, "Could not save Spot", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }
}

