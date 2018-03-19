package sup.savemeaspot.DataLayer;
import android.content.Context;
import android.content.ContentValues;
import android.database.sqlite.SQLiteCursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


/**
 * Created by Frida on 2018-03-19.
 */

public class Spot {
    private int SpotID;
    private String spotTitle;
    private String spotDescription;
    private Coordinate coordinate;

    /**
     * Constructor för Spots
     * @param title
     * @param description
     * @param coord
     */
    public Spot(String title, String description, Coordinate coord){
        spotTitle = title;
        spotDescription = description;
        coordinate = coord;
    }

    /**
     * Getters & Setters
     */
    public String getTitle(){
        return this.spotTitle;
    }
    public void setTitle(String title){
        this.spotTitle = title;
    }
    public String getDescription(){
        return this.spotDescription;
    }
    public void setDescription(String desc){
        this.spotDescription = desc;
    }
    public Coordinate getCoordinates(){
        return this.coordinate;
    }
}

