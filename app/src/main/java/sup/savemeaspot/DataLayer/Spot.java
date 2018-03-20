package sup.savemeaspot.DataLayer;
import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.arch.persistence.room.Query;
import android.content.Context;
import android.content.ContentValues;
import android.database.sqlite.SQLiteCursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.List;


/**
 * Created by Frida on 2018-03-19. Klass för att hantera Spot objekt mot databasen och applikationen
 */
@Entity(tableName = "Spot")
public class Spot {
    @PrimaryKey
    @ColumnInfo(name = "spot_id")
    private int SpotId;
    @ColumnInfo(name = "spot_title")
    private String spotTitle;
    @ColumnInfo(name = "spot_description")
    private String spotDescription;

    /**
     * Getters & Setters
     */
    public int getSpotId(){return this.SpotId;}
    public void setSpotId(int spotId){ this.SpotId = spotId; }
    public String getSpotTitle(){
        return this.spotTitle;
    }
    public void setSpotTitle(String title){
        this.spotTitle = title;
    }
    public String getSpotDescription(){
        return this.spotDescription;
    }
    public void setSpotDescription(String desc){
        this.spotDescription = desc;
    }

    /**
     * Constructor för Spots
     */
    public Spot(){
    }
    /**
     * Interface mot databasen som kan hämta ut och ändra i data som existerar i databasen. Data Access Objects hanterar information som skickas
     * till och från databasen. DAOs deklareras i databasklassen, från vilken ett metodarop kan göras mot respektive DAO.
     */
    @Dao
    public interface SpotDao{
        //Hämtar alla Spots
        @Query("SELECT * FROM SPOT")
        List<Spot> getAllSpots();

        //Hämtar alls Spots med en viss angiven titel
        @Query("SELECT Spot_ID FROM SPOT WHERE SPOT_TITLE LIKE :title ;")
        List<Spot> getSpotByTitle(String title);
    }
}

