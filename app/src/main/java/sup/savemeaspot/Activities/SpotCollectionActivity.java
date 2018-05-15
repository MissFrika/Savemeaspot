package sup.savemeaspot.Activities;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;


import java.util.List;

import sup.savemeaspot.DataLayer.DatabaseHelper;
import sup.savemeaspot.DataLayer.Models.Spot;
import sup.savemeaspot.R;
import sup.savemeaspot.Adapters.SpotCollectionAdapter;

public class SpotCollectionActivity extends AppCompatActivity {

    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spot_collection);
        // Context hämtas och databas instansieras
        this.context = getApplicationContext();
        // Initierar dropDown
        // Hämtar ut lista av spots från database
        List<Spot> spots = DatabaseHelper.getAllSpots(context);

        // Nytt relativeLayout
        RecyclerView recyclerView = findViewById(R.id.recycler_container_spot_collection);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        // Set LayoutManager
        recyclerView.setLayoutManager(layoutManager);

        // Set adapter om spots finns
        if(spots != null) {
            SpotCollectionAdapter adapter = new SpotCollectionAdapter(context, spots, recyclerView);
            adapter.notifyDataSetChanged();
            recyclerView.setAdapter(adapter);
        }
    }

}


