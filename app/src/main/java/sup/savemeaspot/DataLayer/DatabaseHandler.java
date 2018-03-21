package sup.savemeaspot.DataLayer;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Frika on 2018-03-20.
 */

public class DatabaseHandler extends SQLiteOpenHelper {

    //Database
    SQLiteDatabase database;
    //Database version
    private static final int DATABASE_VERSION = 1;
    //Database name
    private static final String DATABASE_NAME = "SpotDatabase.db";

    //Category
    private static final String TABLE_NAME_CAT = "Category";
    private static final String COLUMN_ID_CAT = "CategoryID";
    private static final String COLUMN_NAME_CAT = "category_name";
    private static final String COLUMN_ICON_CAT = "category_img";
    private static final String COLUMN_DELETABLE = "is_deletable";
    //Coordinate
    private static final String TABLE_NAME_COR = "Coordinate";
    private static final String COLUMN_ID_COR = "CoordinateID";
    private static final String COLUMN_LAT = "latitude";
    private static final String COLUMN_LON = "longitude";
    private static final String COLUMN_ADDRESS = "local_address";
    private static final String COLUMN_COUNTRY = "country";
    //SPOT
    private static final String TABLE_NAME_SPOT = "Spot";
    private static final String COLUMN_ID_SPOT = "SpotID";
    private static final String COLUMN_TITLE = "spot_title";
    private static final String COLUMN_DESCRIPTION = "description";
    private static final String COLUMN_CATEGORY = "spot_category";
    private static final String COLUMN_COORDINATE = "spot_coordinate";



    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);

        database = getWritableDatabase();
    }

    //Skapa databas
    @Override
    public void onCreate(SQLiteDatabase db) {
    //Category table
    db.execSQL("CREATE TABLE " +TABLE_NAME_CAT+
            " (" +COLUMN_ID_CAT+ " INTEGER PRIMARY KEY NOT NULL, " +COLUMN_NAME_CAT+ " TEXT NOT NULL, " +COLUMN_ICON_CAT+" TEXT NOT NULL," +COLUMN_DELETABLE+ " BOOLEAN NOT NULL)");

    //Coordinate table
    db.execSQL("CREATE TABLE " +TABLE_NAME_COR+
            " (" +COLUMN_ID_COR+ " INTEGER PRIMARY KEY NOT NULL, " +COLUMN_LAT+ " DOUBLE NOT NULL, " +COLUMN_LON+" DOUBLE NOT NULL," +COLUMN_ADDRESS+ " TEXT ," +COLUMN_COUNTRY+ " TEXT)");

    //Spot table
    db.execSQL("CREATE TABLE " +TABLE_NAME_SPOT+
            " (" +COLUMN_ID_SPOT+ " INTEGER PRIMARY KEY NOT NULL, " +COLUMN_TITLE+ " TEXT NOT NULL, " +COLUMN_DESCRIPTION+" TEXT, " +COLUMN_CATEGORY+ " INTEGER NOT NULL, " +COLUMN_COORDINATE+
            " INTEGER NOT NULL, FOREIGN KEY(" +COLUMN_CATEGORY+ ") REFERENCES Category(categoryid), FOREIGN KEY ("+ COLUMN_COORDINATE + ") REFERENCES Coordinate(coordinateid))");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        }
}
