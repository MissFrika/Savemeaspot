package sup.savemeaspot;

import android.arch.persistence.db.SupportSQLiteOpenHelper;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.DatabaseConfiguration;
import android.arch.persistence.room.InvalidationTracker;
import android.arch.persistence.room.Room;
import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import java.util.List;

import sup.savemeaspot.DataLayer.Category;
import sup.savemeaspot.DataLayer.Coordinate;
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
        SpotDatabase db = Room.databaseBuilder(context, SpotDatabase.class,"SpotDatabase")
        .allowMainThreadQueries()
        .build();
        //Spot från databas
        Spot titleToAdd = db.spotDao().getSpotLast();
        db.close();

        TextView titleTextView = (TextView) findViewById(R.id.spotTitleTextView);
        TextView descTextView = (TextView) findViewById(R.id.spotDescTextView);
        TextView catTextView = (TextView) findViewById(R.id.spotCatNameTextView);
        TextView coordTextView = (TextView) findViewById(R.id.spotCoordsTextView);

        String title = titleToAdd.getSpotTitle();
        String desc = titleToAdd.getSpotDescription();
        String cat = (String.valueOf(titleToAdd.getSpotCategory()));
        String coords = (String.valueOf(titleToAdd.getSpotCoordinate()));

        titleTextView.setText(title);
        descTextView.setText(desc);
        catTextView.setText(cat);
        coordTextView.setText(coords);

    }

}


