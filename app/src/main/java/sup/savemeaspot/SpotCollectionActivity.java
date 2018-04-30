package sup.savemeaspot;

import android.arch.persistence.db.SupportSQLiteOpenHelper;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.DatabaseConfiguration;
import android.arch.persistence.room.InvalidationTracker;
import android.arch.persistence.room.Room;
import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;


import java.util.List;

import sup.savemeaspot.DataLayer.Category;
import sup.savemeaspot.DataLayer.Coordinate;
import sup.savemeaspot.DataLayer.DatabaseHelper;
import sup.savemeaspot.DataLayer.Spot;
import sup.savemeaspot.DataLayer.SpotDatabase;

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
        RelativeLayout dropDown = findViewById(R.id.collection_drop_down_layout);
        // Nytt relativeLayout
        RecyclerView recyclerView = findViewById(R.id.recycler_container_spot_collection);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        // Set LayoutManager
        recyclerView.setLayoutManager(layoutManager);

        // Set adapter om spots finns
        if(spots != null) {
            SpotCollectionAdapter adapter = new SpotCollectionAdapter(context, spots, dropDown);
            adapter.notifyDataSetChanged();
            recyclerView.setAdapter(adapter);
        }


    }




}


