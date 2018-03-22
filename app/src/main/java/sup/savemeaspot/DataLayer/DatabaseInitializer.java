package sup.savemeaspot.DataLayer;

import android.app.Application;
import android.arch.persistence.room.Room;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Handler;

/**
 * Created by Frida on 2018-03-21.
 */

public class DatabaseInitializer {
    private Category category = new Category();
    private Spot spot = new Spot();
    private Coordinate coordinate = new Coordinate();


    public DatabaseInitializer() {

    }

    /**
     * Checks if the category table is empty, and if it is, populates it
     *     * @param context
     */
    public void populateDatabaseWithCategories(final Context context) {
        SpotDatabase database = Room.databaseBuilder(context.getApplicationContext(), SpotDatabase.class, "SpotDatabase")
                .allowMainThreadQueries()
                .build();
        if (database.categoryDao().getAllCategories().isEmpty()) {
            database.categoryDao().insertCategories(Category.populateData());
            database.close();
        }
    }
}
