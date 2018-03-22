package sup.savemeaspot.DataLayer;

import android.app.Application;
import android.arch.persistence.room.Room;
import android.content.Context;

/**
 * Created by Frida on 2018-03-21.
 */

public class DatabaseInitializer {
    private Category category = new Category();
    private Spot spot = new Spot();
    private Coordinate coordinate = new Coordinate();



    public DatabaseInitializer(){

    }

    public void populateDatabaseWithCategories(Context context){
        SpotDatabase database = Room.inMemoryDatabaseBuilder(context.getApplicationContext(), SpotDatabase.class)

                .allowMainThreadQueries()
                .build();
        if(database.categoryDao().getAllCategories().isEmpty()){
            category.setCategoryId(1);
            category.setCategoryName("Fish");
            category.setCategoryImg("/Path");
            category.setDeletable(false);
            database.categoryDao().insertCategories(category);
        }
    }
}
