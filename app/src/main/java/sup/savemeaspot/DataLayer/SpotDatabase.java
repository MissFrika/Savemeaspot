package sup.savemeaspot.DataLayer;


import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;

import java.util.concurrent.Executors;

import android.arch.persistence.room.migration.Migration;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.support.annotation.NonNull;

import sup.savemeaspot.DataLayer.Models.Category;
import sup.savemeaspot.DataLayer.Models.Coordinate;
import sup.savemeaspot.DataLayer.Models.Spot;


/**
 * Created by Frida on 2018-03-19. Databas. Deklarerar vilka klasser som skall finnas tillgängliga som tables i databasen. Attribut är
 * detaljerade i entiteternas enskiljda klasser. H
 */
@android.arch.persistence.room.Database(entities = {Coordinate.class, Spot.class, Category.class}, version = 2)
public abstract class SpotDatabase extends RoomDatabase {

    private static SpotDatabase INSTANCE;
    static final Migration MIGRATION_1_2 = new Migration(1, 2) {
        @Override public void migrate(
                SupportSQLiteDatabase database) {
            // Since we didn’t alter the table, there’s nothing else
            // to do here.
        }
    };

    public abstract Coordinate.CoordinateDao coordinateDao();
    public abstract Spot.SpotDao spotDao();
    public abstract Category.CategoryDao categoryDao();

    /**
     * Hanterar instansiering av en databas
     * @param context
     * @return
     */
    public static SpotDatabase getDatabase(Context context) {
        if (INSTANCE == null) {
            //Skapa en ny instans av databas
            INSTANCE = Room.databaseBuilder(context.getApplicationContext(), SpotDatabase.class, "SpotDatabase")
                            // Tillåter queries på huvudtråden. Får ej köras på huvudtråden på fysisk enhet.
                            // TODO: Skapa en asynkron metod för att köra köra queries mot databasen
                            .addMigrations(MIGRATION_1_2)
                            .allowMainThreadQueries()
                            .build();
        }
        //Returnerar instans av databas
        return INSTANCE;
    }


    private static SpotDatabase buildDatabase(final Context context) {
        return Room.databaseBuilder(context,
                SpotDatabase.class,
                "SpotDatabase")
                .addCallback(new Callback() {
                    @Override
                    public void onCreate(@NonNull SupportSQLiteDatabase db) {
                        super.onCreate(db);
                        Executors.newSingleThreadScheduledExecutor().execute(new Runnable() {
                            @Override
                            public void run() {
                                getDatabase(context).categoryDao().insertCategories(Category.populateData());
                            }
                        });
                    }
                })
                .build();
    }

    //Kontrollerar om databasen existerar
    public static boolean checkDatabase(){

        SQLiteDatabase checkDB = null;
        try{
            checkDB = SQLiteDatabase.openDatabase("/data/data/sup.savemeaspot/databases/SpotDatabase.db",null,SQLiteDatabase.OPEN_READONLY);
            checkDB.close();
        }
        catch(SQLiteException e){

        }
        return checkDB != null;
    }

    public static void destroyInstance() {
        INSTANCE = null;
    }

}

