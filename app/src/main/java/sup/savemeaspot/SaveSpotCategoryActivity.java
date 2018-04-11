package sup.savemeaspot;

import android.arch.persistence.room.Room;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;

import java.util.List;

import sup.savemeaspot.DataLayer.Category;
import sup.savemeaspot.DataLayer.Coordinate;
import sup.savemeaspot.DataLayer.SpotDatabase;

public class SaveSpotCategoryActivity extends AppCompatActivity {

    private Coordinate coordinatesToSave = new Coordinate();
    private Category categoryToSave = new Category();
    private List<Category> categories;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_save_spot_category);
        initBackButton();
        recyclerView = findViewById(R.id.recycler_container_save);

        SpotDatabase database = Room.inMemoryDatabaseBuilder(this.getApplicationContext(), SpotDatabase.class)
                .allowMainThreadQueries() //DO NOT !!!
                .build();
        try {
            List<Category> aCat = database.getDatabase(this).categoryDao().getAllCategories();
            this.categories = aCat;
            database.close();
        } catch (Exception e) {

        }
        // Specifierar en adapter för RecyclerView
        adapter = new CategoryListViewAdapter(categories, this);
        //LayoutManager
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        recyclerView.setHasFixedSize(true);

        recyclerView.setAdapter(adapter);



        //*TextView coordinates = (TextView) findViewById(R.id.coordinates);
        Bundle extra = getIntent().getExtras();
        //Kontrollerar om intent är null
        if (extra != null) {
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
            } catch (NullPointerException e) {
                //coordinates.setText("Something went wrong");
            }
        }
    }

        private void initBackButton() {
            Button back = this.findViewById(R.id.category_cancel);
            back.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View view) {
                    finish();
                }
            });
        }

    /**
     * Öppnar aktiviteten SaveTitleActivity för att spara en titel till en spot
      * @return
     */

    public void openViewSaveTitle(View view){
        Intent intent = new Intent(this, SaveTitleActivity.class);
        if(categoryToSave != null) {
            int catId = categoryToSave.getCategoryId();
            String catName = categoryToSave.getCategoryName();
            int catImg = categoryToSave.getCategoryImg();
            int catDel = categoryToSave.getIsDeletable();

            intent.putExtra("EXTRA_MESSAGE_CATEGORY_ID", catId);
            intent.putExtra("EXTRA_MESSAGE_CATEGORY_NAME", catName);
            intent.putExtra("EXTRA_MESSAGE_CATEGORY_IMG", catImg);
            intent.putExtra("EXTRA_MESSAGE_CATEGORY_IS_DELETABLE", catDel);
        }
        startActivity(intent);

    }

}

