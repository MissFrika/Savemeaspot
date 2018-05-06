package sup.savemeaspot.Activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import sup.savemeaspot.R;

/**
 * Aktivitet för att ändra information on, samt radera Spots
 */
public class EditSpotsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_spots);
    }
}
