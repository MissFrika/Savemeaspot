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


    private static final String TABLE_NAME_CAT = "Category";
    private static final String COLUMN_ID_CAT = "CategoryID";
    private static final String COLUMN_NAME_CAT = "category_name";
    private static final String COLUMN_ICON_CAT = "category_img";
    private static final String COLUMN_DELETABLE = "is_deletable";



    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);

        database = getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
    db.execSQL("CREATE TABLE " +TABLE_NAME_CAT+ " (" +COLUMN_ID_CAT+ " INTEGER PRIMARY KEY, " +COLUMN_NAME_CAT+ " TEXT, " +COLUMN_ICON_CAT+" TEXT," +COLUMN_DELETABLE+ " BOOLEAN)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
