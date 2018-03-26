package sup.savemeaspot;

import android.arch.persistence.room.Room;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import java.util.List;

import sup.savemeaspot.DataLayer.Category;
import sup.savemeaspot.DataLayer.DatabaseHandler;
import sup.savemeaspot.DataLayer.DatabaseInitializer;
import sup.savemeaspot.DataLayer.SpotDatabase;

public class MainMenuScreen extends AppCompatActivity {


    /**
     * Skapar aktiviteten MainMenuScreen
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu_screen);
        TextView t = (TextView)findViewById(R.id.dbtest);
        TextView d = (TextView)findViewById(R.id.dataTest);
        if(SpotDatabase.checkDatabase()){
            t.setText("Database exists");
        }
        else{
            t.setText("No database found");
        }
        SpotDatabase database = Room.inMemoryDatabaseBuilder(this.getApplicationContext(), SpotDatabase.class)
                .allowMainThreadQueries()
                .build();
        try {
            DatabaseInitializer init = new DatabaseInitializer();
            init.populateDatabaseWithCategories(this);
            List<Category> aCat = database.getDatabase(this).categoryDao().getAllCategories();
            d.setText(aCat.get(0).getCategoryName());
        }
        catch(Exception e){
            d.setText("Something went wrong");
        }

    }
}
