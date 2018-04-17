package sup.savemeaspot;

import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import sup.savemeaspot.DataLayer.Category;
import sup.savemeaspot.DataLayer.Spot;
import sup.savemeaspot.DataLayer.SpotDatabase;

public class SaveTitleActivity extends AppCompatActivity {

    private List<String> exampleTitles = new ArrayList<String>();
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private String[] exampleStrings;
    private Category chosenCategory;
    private Context context = SaveTitleActivity.this;
    private String selectedTitle ="";
    private Spot spotToSave = new Spot();

    public SaveTitleActivity() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_save_title);

        //Instansiera sparaknappen
        saveSpotButton();
        checkIncomingIntents();
        //Kontrollerar vilken kategori som valts från SaveSpotCategoryActivity och instansierar "titlesToDisplay"
        if (this.chosenCategory.getCategoryName() != null) {

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

        // Specifierar en adapter för RecyclerView
        adapter = new TitleRecyclerViewAdapter(exampleTitles);
        recyclerView = findViewById(R.id.title_recycler_view);
        layoutManager = new LinearLayoutManager(this);
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
                getIntent().hasExtra("EXTRA_MESSAGE_CATEGORY_IMG") && getIntent().hasExtra("EXTRA_MESSAGE_CATEGORY_IS_DELETABLE")) {
            Bundle extra = getIntent().getExtras();
            int id = extra.getInt("EXTRA_MESSAGE_CATEGORY_ID");
            String name = extra.getString("EXTRA_MESSAGE_CATEGORY_NAME");
            int image = extra.getInt("EXTRA_MESSAGE_CATEGORY_IMG");
            int isDeletable = extra.getInt("EXTRA_MESSAGE_CATEGORY_IS_DELETABLE");
            this.chosenCategory = new Category(id, name, image, isDeletable);
            this.spotToSave.setSpotCategory(id);
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
                //Bygg in-memory databas
                SpotDatabase database = Room.inMemoryDatabaseBuilder(context, SpotDatabase.class)
                        .allowMainThreadQueries() //DO NOT !!!
                        .build();
                TextView textView = (TextView) findViewById(R.id.editTitle);

                Boolean hasTitle = false;

                if(!(textView.getText().toString()).isEmpty() && !(textView.getText().toString() == " ")){
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
                        //SpotDao
                        database.spotDao().insertNewSpot(spotToSave);
                        Toast.makeText(context, spotToSave.getSpotTitle() + " has successfully been saved!", Toast.LENGTH_LONG).show();
                        //Stäng db
                        database.close();
                        //Avsluta aktivitet och öppna MapsStart
                        Intent intent = new Intent(context, MapsStart.class);
                        finish();

                    } catch (Exception e) {
                        database.close();
                        Toast.makeText(context, "Could not save Spot", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }
}

