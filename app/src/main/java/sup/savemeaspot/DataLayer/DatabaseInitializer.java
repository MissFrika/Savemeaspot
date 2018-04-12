package sup.savemeaspot.DataLayer;

import android.app.Application;
import android.arch.persistence.room.Room;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Handler;

/**
 * Created by Frida on 2018-03-21.
 * Denna klass förbereder data i databasen om data saknas i tabeller som behövs för att funktionalitet skall fungera i applikationen
 */

public class DatabaseInitializer {
    private Category category = new Category();
    private Spot spot = new Spot();
    private Coordinate coordinate = new Coordinate();

    //Konstruktor
    public DatabaseInitializer() {

    }

    /**
     * Kontrollerar om Category-tabellen är tom, och om sådant är fallet, fyller den med kategorier
     * @param context
     */
    public static void populateDatabaseWithCategories(final Context context) {
        SpotDatabase database = Room.databaseBuilder(context.getApplicationContext(), SpotDatabase.class, "SpotDatabase")
                .allowMainThreadQueries() // // TODO: Skapa en asynkron metod för att köra köra queries mot databasen VIKTIGT!! Denna måste hanteras på en annan tråd i release-versionen!
                .build();
        if (database.categoryDao().getAllCategories().isEmpty()) {
            database.categoryDao().insertCategories(Category.populateData());
            database.close();
        }
    }
}
