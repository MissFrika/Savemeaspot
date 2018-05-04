package sup.savemeaspot.DataLayer;
import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.PrimaryKey;
import android.arch.persistence.room.Query;
import java.util.List;
import java.util.Queue;

import android.arch.persistence.room.Update;
import android.arch.persistence.room.util.TableInfo;
import android.content.Context;
import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteCursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


/**
 * Created by Frida on 2018-03-07.
 */

/** Klass för att hantera koordinater GPS**/
@Entity(tableName = "Coordinate")
public class Coordinate {
    @PrimaryKey(autoGenerate = true)
    private int coordinateId;
    @ColumnInfo(name = "latitude")
    private double latitude;
    @ColumnInfo(name = "longitude")
    private double longitude;
    @ColumnInfo(name = "local_address")
    private String localAddress;
    @ColumnInfo(name = "country")
    private String countryName;

    /**
     * Konstruktor för Coordinate
     */

    public Coordinate(){
    }

    /**
     * Konstruktor för Coordinate, object-instansiering
     * @return
     */
    public Coordinate(double latitude, double longitude, String locale, String country){
        this.latitude = latitude;
        this.longitude = longitude;
        this.localAddress = locale;
        this.countryName = country;
    }

    /**
     * Skapar extra intent-meddelanden för Coordinate
     * @param intent
     * @return
     */
    public Intent putExtraMessageCoordinate(Intent intent, Coordinate coordinate){
        intent.putExtra("EXTRA_MESSAGE_COORDINATES_LAT", coordinate.getLatitude());
        intent.putExtra("EXTRA_MESSAGE_COORDINATES_LONG", coordinate.getLongitude());
        intent.putExtra("EXTRA_MESSAGE_COORDINATES_LOCAL", coordinate.getLocalAddress());
        intent.putExtra("EXTRA_MESSAGE_COORDINATES_COUNTRY", coordinate.getCountryName());
        return intent;
    }

    /**
     * Getters and setters
     * @return
     */
    public int getCoordinateId () {return this.coordinateId;}
    public void setCoordinateId(int coordId) { this.coordinateId = coordId; }
    public double getLatitude(){
        return this.latitude;
    }
    public void setLatitude(double lat){
        this.latitude = lat;
    }
    public double getLongitude(){
        return this.longitude;
    }
    public void setLongitude(double lon){
        this.longitude = lon;
    }
    public String getLocalAddress(){
        return this.localAddress;
    }
    public void setLocalAddress(String address){
        this.localAddress = address;
    }
    public String getCountryName(){
        return this.countryName;
    }
    public void setCountryName(String country){
        this.countryName = country;
    }

    @Dao
    public interface CoordinateDao{
        //Nya Coordinates
        @Insert(onConflict = OnConflictStrategy.IGNORE)
        void insertCoordinates(Coordinate... coordinates);

        //Ny Coordinate
        @Insert (onConflict = OnConflictStrategy.IGNORE)
        void insertSingleCoordinate(Coordinate coordinate);

        //Ny coordinate som lägger till kopior av existerande coordinates.
        @Insert (onConflict = OnConflictStrategy.REPLACE)
        void insertDuplicateCoordinate(Coordinate... coordinates);

        //Uppdatera Coordinate
        @Update(onConflict = OnConflictStrategy.REPLACE)
        void updateCoordinate(Coordinate coordinate);

        //Ta bort Coordinates
        @Delete()
        void deleteCoordinate(Coordinate coordinate);

        //Hämta sist insatta raden i tabellen
        @Query("SELECT CoordinateID FROM COORDINATE WHERE CoordinateID =(SELECT MAX(CoordinateID) FROM COORDINATE)")
        int getLastRecordCoordinates();

        //Visa alla Coordinate.
        @Query("SELECT * FROM COORDINATE")
        List<Coordinate> getAllCoordinates();

        //Sök Coordinate baserat på CoordinateId.
        @Query("SELECT * FROM COORDINATE WHERE CoordinateId LIKE :spotCoordinate")
        Coordinate getCoordinateByID(int spotCoordinate);

        //Visa Coordinate med specifik longitude och latitude.
        @Query("SELECT * FROM COORDINATE WHERE latitude LIKE :spotLatitude AND longitude LIKE :spotLongitude")
        List<Coordinate> getCoordinateByLatAndLong (double spotLatitude, double spotLongitude);

        //Visa Coordinate från ett visst land
        @Query("SELECT * FROM COORDINATE WHERE country LIKE :spotCountry")
        List<Coordinate> getCoordinateByCountry(String spotCountry);

        //Visa Coordinates med viss address.
        @Query("SELECT * FROM COORDINATE WHERE local_address LIKE :spotAddress")
        List<Coordinate> getCoordinateByAddress(String spotAddress);

        //Ta bort koordinater med ett visst koordinatid från databasen
        @Query("DELETE FROM COORDINATE WHERE COORDINATE_ID LIKE :coordinateId")
        void deleteCoordinates(int coordinateId);
    }

}
