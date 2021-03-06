package sup.savemeaspot.Activities;

import android.arch.persistence.room.Room;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import sup.savemeaspot.Adapters.CategoryRecyclerViewAdapter;
import sup.savemeaspot.Adapters.CustomSpinnerAdapter;
import sup.savemeaspot.DataLayer.Models.Category;
import sup.savemeaspot.DataLayer.Models.Coordinate;
import sup.savemeaspot.DataLayer.DatabaseHelper;
import sup.savemeaspot.DataLayer.SpotDatabase;
import sup.savemeaspot.R;

public class SaveSpotCategoryActivity extends AppCompatActivity {

    private Coordinate coordinatesToSave = new Coordinate();
    private Category categoryToSave = new Category();
    private List<Category> categories;
    private static RecyclerView recyclerView;
    private Context context;
    private int resourceId;
    private static CategoryRecyclerViewAdapter adapter;
    private static RecyclerView.LayoutManager layoutManager;
    //Ikoner
    private final int[] drawables = new int[]{R.drawable.apple,R.drawable.cherry, R.drawable.fish, R.drawable.wheat, R.drawable.water,
            R.drawable.heart,R.drawable.star, R.drawable.fire, R.drawable.building, R.drawable.bus, R.drawable.boat, R.drawable.plane, R.drawable.train};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_save_spot_category);
        //Instansiera tillbakaknapp
        initBackButton();
        //Instansiera spara kategori-knapp
        initAddCat();
        this.context = getApplicationContext();

        Bundle extra = getIntent().getExtras();
        //Kontrollerar om intent är null
        if (extra != null) {
            try {
                //Hämtar ut intents och instansierar ett nytt Coordinate objekt
                coordinatesToSave = getIncomingCoordinateExtras(getIntent());

            } catch (NullPointerException e) {
                //Någonting gick snett
            }
        }

        recyclerView = findViewById(R.id.recycler_container_save);

        SpotDatabase database = Room.inMemoryDatabaseBuilder(this.getApplicationContext(), SpotDatabase.class)
                .allowMainThreadQueries()
                .build();
        try {
            List<Category> aCat = database.getDatabase(this).categoryDao().getAllCategories();
            this.categories = aCat;
            database.close();
        } catch (Exception e) {

        }
        // Specifierar en adapter för RecyclerView
        adapter = new CategoryRecyclerViewAdapter(this, categories, coordinatesToSave, SaveSpotCategoryActivity.this);
        //LayoutManager
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        recyclerView.setHasFixedSize(true);

        recyclerView.setAdapter(adapter);

        //Spinner
        setSpinner();

    }

    /**
     * Ta emot inkommande Coordinata-intent och skapa extras
     */
    private Coordinate getIncomingCoordinateExtras(Intent intent){
        //Hämtar ut intents
        Bundle extras = intent.getExtras();
        double latitude = extras.getDouble("EXTRA_MESSAGE_COORDINATES_LAT");
        double longitude = extras.getDouble("EXTRA_MESSAGE_COORDINATES_LONG");
        String locale = extras.getString("EXTRA_MESSAGE_COORDINATES_LOCAL");
        String country = extras.getString("EXTRA_MESSAGE_COORDINATES_COUNTRY");
        return new Coordinate(latitude,longitude,locale,country);
    }


    /**
     * Cancel activity
     */
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
     * Instantierar en spinner som visar kategoriikoner
     */
    private void setSpinner(){
            //Spinner med anpassad adapter
            CustomSpinnerAdapter customAdapter = new CustomSpinnerAdapter(SaveSpotCategoryActivity.this, drawables);
            customAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

            Spinner spinner = (Spinner) findViewById(R.id.category_spinner);
            spinner.setAdapter(customAdapter);

            //On Select listener
            spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    resourceId = drawables[i];
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }
            });
        }

    /**
     * Add new category-button
     */
    private void initAddCat() {
            Button addCatBtn = this.findViewById(R.id.buttonAddCategory);
            //OnClick-listener
            addCatBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    TextView categoryField = findViewById(R.id.addNewCategory);
                    if(!categoryField.getText().toString().isEmpty()){
                        Category newCategory = new Category();
                        newCategory.setCategoryName(categoryField.getText().toString());
                        newCategory.setIsDeletable(1);
                        newCategory.setCategoryImg(resourceId);

                        try{
                            DatabaseHelper.insertCategory(context, newCategory);
                            //Dataset ändrad
                            SpotDatabase database = Room.databaseBuilder(SaveSpotCategoryActivity.this.getApplicationContext(), SpotDatabase.class, "SpotDatabase")
                                    .allowMainThreadQueries() //DO NOT !!!
                                    .build();
                            int lastIdInsert = database.categoryDao().getLastRecordCategory();
                            database.close();
                            Category addedCategory = new Category(lastIdInsert, newCategory.getCategoryName(), newCategory.getCategoryImg(),newCategory.getIsDeletable());
                            adapter.insertCategory(addedCategory);
                            Toast.makeText(context, "Category: " + newCategory.getCategoryName() + " has been created", Toast.LENGTH_SHORT).show();
                            categoryField.setText("");
                        }
                        catch(Exception e){
                            Toast.makeText(context, "Could not save to database", Toast.LENGTH_SHORT).show();
                        }
                    }
                    else{
                        Toast.makeText(context, "Category name is required", Toast.LENGTH_SHORT).show();

                    }

                }
            });
        }

    /**
     * Öppnar aktiviteten SaveTitleActivity för att spara en titel till en spot, skickar med extrameddelanden för Category och Coordinate
      * @return
     */
    public void openViewSaveTitle(View view){
        Intent intent = new Intent(this, SaveTitleActivity.class);

        //Kategori
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
        //Koordinater
        Coordinate coordinate = new Coordinate();
        coordinate.putExtraMessageCoordinate(intent, coordinatesToSave);

        startActivity(intent);
    }

}

