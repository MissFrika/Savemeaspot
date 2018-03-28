package sup.savemeaspot;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.arch.persistence.room.Room;
import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import sup.savemeaspot.DataLayer.Category;
import sup.savemeaspot.DataLayer.Coordinate;
import sup.savemeaspot.DataLayer.DatabaseInitializer;
import sup.savemeaspot.DataLayer.SpotDatabase;

public class SaveSpotActivity extends AppCompatActivity implements CategoryItemFragment.OnListFragmentInteractionListener {

    private Coordinate coordinatesToSave = new Coordinate();
    private Category categoryToSave = new Category();
    private List<Category> categories;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_save_spot);

        recyclerView = findViewById(R.id.recycler_container_save);


        SpotDatabase database = Room.inMemoryDatabaseBuilder(this.getApplicationContext(), SpotDatabase.class)
                .allowMainThreadQueries() //DO NOT !!!
                .build();
        try {
            List<Category> aCat = database.getDatabase(this).categoryDao().getAllCategories();
            this.categories = aCat;
            database.close();
        }
        catch(Exception e){

        }
        // specify an adapter (see also next example)
        adapter = new CategoryListViewAdapter(categories);
        // use a linear layout manager
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        recyclerView.setHasFixedSize(true);

        recyclerView.setAdapter(adapter);

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
    public void onArticleSelected(int position) {
        // The user selected the headline of an article from the HeadlinesFragment
        // Do something here to display that article
    }


}
