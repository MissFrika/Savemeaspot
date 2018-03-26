package sup.savemeaspot.DataLayer;
import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Insert;
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
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "spotid")
    private int SpotId;
    @ColumnInfo(name = "spot_title")
    private String spotTitle;
    @ColumnInfo(name = "description")
    private String spotDescription;
    @ColumnInfo(name ="spot_category")
    private int spotCategory;
    @ColumnInfo(name = "spot_coordinate")
    private int spotCoordinate;
    @ColumnInfo(name = "spot_icon")
    private String spotIcon;
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
    public int getSpotCategory(){return this.spotCategory;}
    public void setSpotCategory(int spotCat){this.spotCategory = spotCat;}
    public int getSpotCoordinate(){return this.spotCoordinate;}
    public void setSpotCoordinate(int spotCoord){this.spotCoordinate = spotCoord;}
    public String getSpotIcon() {return this.spotIcon;}
    public void setSpotIcon(String iconImg) {this.spotIcon = iconImg;}

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

        //Hämtar alla Spots med en viss angiven titel
        @Query("SELECT * FROM SPOT WHERE SPOT_TITLE LIKE :title")
        List<Spot> getSpotByTitle(String title);
        //Hämtar alls spots med en viss kategori
        @Query("SELECT * FROM SPOT WHERE SPOT_CATEGORY LIKE :categoryId ;")
        List<Spot> getSpotsByCategory(int categoryId);
        //Lägg till ny spot
        @Insert
        void insertNewSpot(Spot... spots);

    }
}

