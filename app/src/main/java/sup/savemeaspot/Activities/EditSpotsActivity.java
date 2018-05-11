package sup.savemeaspot.Activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.RelativeLayout;

import java.util.List;

import sup.savemeaspot.Adapters.EditSpotAdapter;
import sup.savemeaspot.DataLayer.DatabaseHelper;
import sup.savemeaspot.DataLayer.Models.Spot;
import sup.savemeaspot.R;

/**
 * Aktivitet för att ändra information on, samt radera Spots
 */
public class EditSpotsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_spots);

        RecyclerView recyclerView = findViewById(R.id.recycler_container_edit_spots);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this.getApplicationContext());
        List<Spot> spotList = DatabaseHelper.getAllSpots(this.getApplicationContext());

        //Sätt Adapter om spotList !null
        if(spotList != null) {
            EditSpotAdapter editSpotAdapter = new EditSpotAdapter(this.getApplicationContext(), spotList);
            recyclerView.setLayoutManager(layoutManager);
            recyclerView.setAdapter(editSpotAdapter);
        }
    }
}
