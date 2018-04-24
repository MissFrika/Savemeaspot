package sup.savemeaspot.DataLayer;

import android.app.Application;
import android.arch.persistence.room.Room;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Handler;

import java.util.List;

/**
 * Created by Frida on 2018-03-21.
 * Denna klass hanterar data som skall sparas till och hämtas från databasen via Activities, samt fyller databasen med startdata som behövs för att funktionalitet skall fungera i applikationen
 */

public class DatabaseHelper {

    //Konstruktor
    public DatabaseHelper() {

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
        database.close();
    }

    /**
     * Ny Category
     * @param context
     * @param category
     */
    public static void insertCategory(final Context context, Category category) {
        SpotDatabase database = Room.databaseBuilder(context.getApplicationContext(), SpotDatabase.class, "SpotDatabase")
                .allowMainThreadQueries() // // TODO: Skapa en asynkron metod för att köra köra queries mot databasen VIKTIGT!! Denna måste hanteras på en annan tråd i release-versionen!
                .build();
        database.categoryDao().insertCategory(category);
        database.close();
    }

    /**
     * Metod för att spara Spot till databas.
     * @param context
     * @param spot
     */
    public static void insertSpot(final Context context, Spot spot){
        SpotDatabase database = Room.databaseBuilder(context.getApplicationContext(), SpotDatabase.class, "SpotDatabase")
                .allowMainThreadQueries() // // TODO: Skapa en asynkron metod för att köra köra queries mot databasen VIKTIGT!! Denna måste hanteras på en annan tråd i release-versionen!
                .build();
        database.spotDao().insertSingleSpot(spot);
        database.close();
    }

    /**
     * Metod för att spara coordinate till databas.
     * @param context
     * @param coordinate
     */
    public static void insertCoordinate(final Context context, Coordinate coordinate){
        SpotDatabase database = Room.databaseBuilder(context.getApplicationContext(), SpotDatabase.class, "SpotDatabase")
                .allowMainThreadQueries() // // TODO: Skapa en asynkron metod för att köra köra queries mot databasen VIKTIGT!! Denna måste hanteras på en annan tråd i release-versionen!
                .build();
        database.coordinateDao().insertSingleCoordinate(coordinate);
        database.close();
    }

    /**
     * Kontrollerar om Spots existerar i databasen
     * @param context
     * @return
     */
    public static boolean checkIfSpotsExist(final Context context){
        boolean result = false;
        SpotDatabase database = Room.databaseBuilder(context.getApplicationContext(), SpotDatabase.class, "SpotDatabase")
                .allowMainThreadQueries() // // TODO: Skapa en asynkron metod för att köra köra queries mot databasen VIKTIGT!! Denna måste hanteras på en annan tråd i release-versionen!
                .build();
        int totalSpots = database.spotDao().countSpots();
        database.close();
        if(totalSpots >= 1)
        {
            result = true;
        }
        return result;
    }

    /**
     * Hämtar en spots koordinater från databasen
     * @param spot
     * @return
     */
    public static Coordinate getSpotCoordinates(Context context,Spot spot){
        Coordinate coordinate = new Coordinate();
        int spotCoordinate = spot.getSpotCoordinate();
        SpotDatabase database = Room.databaseBuilder(context.getApplicationContext(), SpotDatabase.class, "SpotDatabase")
                .allowMainThreadQueries() // // TODO: Skapa en asynkron metod för att köra köra queries mot databasen VIKTIGT!! Denna måste hanteras på en annan tråd i release-versionen!
                .build();
        coordinate = database.coordinateDao().getCoordinateByID(spotCoordinate);
        database.close();
        return coordinate;
    }

    /**
     * Hämta alla spots ur databasen
     * @param context
     * @return
     */
    public static List<Spot> getAllSpots(Context context) {
        List<Spot> spotList;
        if(checkIfSpotsExist(context)){
            SpotDatabase database = Room.databaseBuilder(context.getApplicationContext(), SpotDatabase.class, "SpotDatabase")
                    .allowMainThreadQueries() // // TODO: Skapa en asynkron metod för att köra köra queries mot databasen VIKTIGT!! Denna måste hanteras på en annan tråd i release-versionen!
                    .build();
            spotList = database.spotDao().getAllSpots();
            database.close();
            return spotList;
        }
        else{
            return null;
        }

    }
}
