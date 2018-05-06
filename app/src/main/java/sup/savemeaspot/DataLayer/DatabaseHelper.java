package sup.savemeaspot.DataLayer;

import android.arch.persistence.room.Room;
import android.content.Context;

import java.util.List;

import sup.savemeaspot.DataLayer.Models.Category;
import sup.savemeaspot.DataLayer.Models.Coordinate;
import sup.savemeaspot.DataLayer.Models.Spot;

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

    /**
     * Uppdatera information om en spot
     * @param context
     * @param spot
     * @param title
     * @param description
     * @param category
     */
    public static void editSpot(Context context, Spot spot, String title, String description, Category category){

        Spot updatedSpot = spot;
        //Konrtollerar förändringar för alla fält
        if(title != spot.getSpotTitle()){
            updatedSpot.setSpotTitle(title);
        }
        if(description != spot.getSpotDescription()){
            updatedSpot.setSpotDescription(description);
        }
        if(category.getCategoryId() != spot.getSpotCategory()) {
            updatedSpot.setSpotCategory(category.getCategoryId());
        }

        //Uppdatera fält
        SpotDatabase database = Room.databaseBuilder(context.getApplicationContext(),SpotDatabase.class, "SpotDatabase")
                .allowMainThreadQueries()
                .build();
        database.spotDao().updateSpotTitle(updatedSpot.getSpotTitle(),spot.getSpotId());
        database.spotDao().updateSpotCategory(updatedSpot.getSpotCategory(),spot.getSpotId());
        database.spotDao().updateSpotDescription(updatedSpot.getSpotDescription(),spot.getSpotId());
        database.close();

    }

    /**
     * Ta bort en vald kategory från databasen
     * @param context
     * @param category
     *
     */
    public static void deleteCategory(Context context, Category category){
        SpotDatabase database = Room.databaseBuilder(context.getApplicationContext(), SpotDatabase.class, "SpotDatabase")
                .allowMainThreadQueries() // // TODO: Skapa en asynkron metod för att köra köra queries mot databasen VIKTIGT!! Denna måste hanteras på en annan tråd i release-versionen!
                .build();
        database.categoryDao().deleteCategories(category);
        database.close();

    }

    /**
     * Ändra
     * @param context
     * @param category
     * @param categoryName
     * @param categoryImage
     */
    public static void editCategory(Context context, Category category, String categoryName, int categoryImage){
        SpotDatabase database = Room.databaseBuilder(context.getApplicationContext(), SpotDatabase.class, "SpotDatabase")
                .allowMainThreadQueries() // // TODO: Skapa en asynkron metod för att köra köra queries mot databasen VIKTIGT!! Denna måste hanteras på en annan tråd i release-versionen!
                .build();
        database.categoryDao().editCategory(category.getCategoryId(), categoryName, categoryImage);

        database.close();
    }

}
