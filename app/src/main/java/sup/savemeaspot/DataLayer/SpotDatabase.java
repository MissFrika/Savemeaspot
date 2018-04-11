package sup.savemeaspot.DataLayer;


import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.arch.persistence.room.Query;
import java.util.List;
import java.util.concurrent.Executors;

import android.arch.persistence.room.util.TableInfo;
import android.content.Context;
import android.content.ContentValues;
import android.database.sqlite.SQLiteCursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.NonNull;


/**
 * Created by Frida on 2018-03-19. Databas. Deklarerar vilka klasser som skall finnas tillgängliga som tables i databasen. Attribut är
 * detaljerade i entiteternas enskiljda klasser. H
 */
@android.arch.persistence.room.Database(entities = {Coordinate.class, Spot.class, Category.class}, version = 1)
public abstract class SpotDatabase extends RoomDatabase {

    private static SpotDatabase INSTANCE;

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
                            .allowMainThreadQueries()
                            .build();
        }
        //Returnerar instans av databas
        return INSTANCE;
    }

    private static SpotDatabase buildDatabase(final Context context) {
        return Room.databaseBuilder(context,
                SpotDatabase.class,
                "my-database")
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

