package sup.savemeaspot;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import sup.savemeaspot.DataLayer.DatabaseHandler;
import sup.savemeaspot.DataLayer.SpotDatabase;

public class MainMenuScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu_screen);
        TextView t = (TextView)findViewById(R.id.dbtest);
        if(SpotDatabase.checkDatabase()){
            t.setText("Database exists");
        }
        else{
            t.setText("No database found");
        }
    }
}
