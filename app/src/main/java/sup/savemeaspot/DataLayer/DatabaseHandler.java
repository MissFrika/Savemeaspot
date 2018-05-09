package sup.savemeaspot.DataLayer;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Denna klass hanterar skapandet och uppdatering av databasen vid arkitekturändringar
 * Created by Frika on 2018-03-20.
 */

public class DatabaseHandler extends SQLiteOpenHelper {

    //Databas
    SQLiteDatabase database;
    //Databas version
    private static final int DATABASE_VERSION = 1;
    //Databasnamn
    private static final String DATABASE_NAME = "SpotDatabase.db";

    //Kategori
    private static final String TABLE_NAME_CAT = "Category";
    private static final String COLUMN_ID_CAT = "CategoryID";
    private static final String COLUMN_NAME_CAT = "category_name";
    private static final String COLUMN_ICON_CAT = "category_img";
    private static final String COLUMN_DELETABLE = "is_deletable";
    //Koordinat
    private static final String TABLE_NAME_COR = "Coordinate";
    private static final String COLUMN_ID_COR = "CoordinateID";
    private static final String COLUMN_LAT = "latitude";
    private static final String COLUMN_LON = "longitude";
    private static final String COLUMN_ADDRESS = "local_address";
    private static final String COLUMN_COUNTRY = "country";
    //Spot
    private static final String TABLE_NAME_SPOT = "Spot";
    private static final String COLUMN_ID_SPOT = "SpotID";
    private static final String COLUMN_TITLE = "spot_title";
    private static final String COLUMN_DESCRIPTION = "spot_description";
    private static final String COLUMN_CATEGORY = "spot_category";
    private static final String COLUMN_COORDINATE = "spot_coordinate";


    /**
     * Konstruktor...Hämtar nuvarande databas utifrån context (Applikation, aktivitet etc.)
     * @param context
     */
    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);

        database = getWritableDatabase();
        DatabaseHelper.populateDatabaseWithCategories(context);
    }

    //Skapa databas
    @Override
    public void onCreate(SQLiteDatabase db) {
    //Category table
    db.execSQL("CREATE TABLE " +TABLE_NAME_CAT+
            " (" +COLUMN_ID_CAT+ " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +COLUMN_NAME_CAT+ " TEXT NOT NULL, " +COLUMN_ICON_CAT+" INTEGER NOT NULL," +COLUMN_DELETABLE+ " INT NOT NULL)");

    //Coordinate table
    db.execSQL("CREATE TABLE " +TABLE_NAME_COR+
            " (" +COLUMN_ID_COR+ " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +COLUMN_LAT+ " DOUBLE NOT NULL, " +COLUMN_LON+" DOUBLE NOT NULL," +COLUMN_ADDRESS+ " TEXT ," +COLUMN_COUNTRY+ " TEXT)");

    //Spot table
    db.execSQL("CREATE TABLE " +TABLE_NAME_SPOT+
            " (" +COLUMN_ID_SPOT+ " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +COLUMN_TITLE+ " TEXT NOT NULL, " +COLUMN_DESCRIPTION+" TEXT, " +COLUMN_CATEGORY+ " INTEGER NOT NULL, " +COLUMN_COORDINATE+
            " INTEGER NOT NULL, FOREIGN KEY(" +COLUMN_CATEGORY+ ") REFERENCES Category(categoryid) ON DELETE CASCADE, FOREIGN KEY ("+ COLUMN_COORDINATE + ") REFERENCES Coordinate(coordinateid) ON DELETE CASCADE)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.w(DatabaseHandler.class.getName(),
                "Upgrading database from version " + oldVersion + " to "
                        + newVersion + ", which will destroy all old data");
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_CAT);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_COR);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_SPOT);
        onCreate(db);
    }
}
