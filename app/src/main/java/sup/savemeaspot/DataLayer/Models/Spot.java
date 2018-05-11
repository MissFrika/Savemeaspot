package sup.savemeaspot.DataLayer.Models;
import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.PrimaryKey;
import android.arch.persistence.room.Query;

import java.util.List;

import static android.arch.persistence.room.ForeignKey.CASCADE;
import static android.arch.persistence.room.ForeignKey.RESTRICT;


/**
 * Created by Frida on 2018-03-19. Klass för att hantera Spot objekt mot databasen och applikationen
 */
@Entity(tableName = "Spot", foreignKeys = {@ForeignKey(entity = Category.class, parentColumns = "categoryId", childColumns = "spot_category", onDelete = CASCADE, onUpdate = RESTRICT),
        @ForeignKey(entity = Coordinate.class, parentColumns = "coordinateId", childColumns = "spot_coordinate", onDelete = CASCADE, onUpdate = RESTRICT)})
public class Spot {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "spotId")
    private int SpotId;
    @ColumnInfo(name = "spot_title")
    private String spotTitle;
    @ColumnInfo(name = "spot_description")
    private String spotDescription;
    @ColumnInfo(name ="spot_category")
    private int spotCategory;
    @ColumnInfo(name = "spot_coordinate")
    private int spotCoordinate;

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
        @Query("SELECT * FROM SPOT;")
        List<Spot> getAllSpots();

        //Hämta senast inlagda spot
        @Query("SELECT * FROM SPOT WHERE SPOTID =(SELECT MAX(SPOTID) FROM SPOT)")
        Spot getSpotLast();

        //Räkna hur många Spots som finns i databasen
        @Query("SELECT COUNT(SPOTID) FROM SPOT")
        int countSpots();

        //Radera en Spot
        @Query("DELETE FROM SPOT WHERE SPOTID LIKE :spotId")
        void deleteSpot(int spotId);

        //Hämtar alla Spots med en viss angiven titel
        @Query("SELECT * FROM SPOT WHERE SPOT_TITLE LIKE :title")
        List<Spot> getSpotByTitle(String title);
        //Hämtar alls spots med en viss kategori
        @Query("SELECT * FROM SPOT WHERE SPOT_CATEGORY LIKE :categoryId")
        List<Spot> getSpotsByCategory(int categoryId);

        //Uppdatera en specifik Spots titel
        @Query("UPDATE SPOT SET SPOT_TITLE = :spotTitle WHERE SPOTID = :spotId")
        void updateSpotTitle (String spotTitle, int spotId);

        //Uppdatera en specifik Spots beskrivning
        @Query("UPDATE SPOT SET SPOT_DESCRIPTION = :spotDescription WHERE SPOTID = :spotId")
        void updateSpotDescription (String spotDescription, int spotId);

        //Uppdatera en specifik Spots kategori
        @Query("UPDATE SPOT SET SPOT_CATEGORY = :categoryId WHERE SPOTID = :spotId")
        void updateSpotCategory (int categoryId, int spotId);

        //Lägg till nya spots
        @Insert (onConflict = OnConflictStrategy.IGNORE)
        void insertNewSpots(Spot... spots);

        @Insert (onConflict = OnConflictStrategy.IGNORE)
        void insertSingleSpot(Spot spot);


    }
}

