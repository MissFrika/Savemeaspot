package sup.savemeaspot;

import android.arch.persistence.room.Room;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.wifi.p2p.WifiP2pManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import sup.savemeaspot.DataLayer.Category;
import sup.savemeaspot.DataLayer.Coordinate;
import sup.savemeaspot.DataLayer.SpotDatabase;

public class SaveSpotCategoryActivity extends AppCompatActivity {

    private Coordinate coordinatesToSave = new Coordinate();
    private Category categoryToSave = new Category();
    private Category newCategory = new Category();
    private List<Category> categories;
    private RecyclerView recyclerView;
    private Context context;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    //Ikoner
    private int[] drawables = new int[]{R.drawable.apple,R.drawable.cherry, R.drawable.fish, R.drawable.wheat, R.drawable.water,
            R.drawable.heart, R.drawable.fire, R.drawable.building};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_save_spot_category);
        //Instansiera tillbakaknapp
        initBackButton();
        //Instansiera spara kategori-knapp
        initAddCat();
        this.context = this;
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

        //Spinner
        setSpinner();

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

            } catch (NullPointerException e) {
                //Something went wrong;
            }
        }
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
                    int resourceId = drawables[i];
                    newCategory.setCategoryImg(resourceId);
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
                        newCategory.setCategoryName(categoryField.getText().toString());
                        newCategory.setIsDeletable(1);

                        try{
                            SpotDatabase database = Room.inMemoryDatabaseBuilder(context, SpotDatabase.class)
                                    .allowMainThreadQueries() //TODO: Async
                                    .build();
                            database.categoryDao().insertCategories(newCategory);
                            database.close();
                            Toast.makeText(context, "Category: " + newCategory.getCategoryName() + " has been created", Toast.LENGTH_SHORT).show();
                        }
                        catch(Exception e){
                            Toast.makeText(context, "Could not save to database", Toast.LENGTH_SHORT).show();
                        }
                    }

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

