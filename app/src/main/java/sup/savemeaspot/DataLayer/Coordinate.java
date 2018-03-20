package sup.savemeaspot.DataLayer;
import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.arch.persistence.room.Query;
import java.util.List;

import android.arch.persistence.room.util.TableInfo;
import android.content.Context;
import android.content.ContentValues;
import android.database.sqlite.SQLiteCursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


/**
 * Created by Frida on 2018-03-07.
 */

/** Klass för att hantera koordinater GPS**/
@Entity(tableName = "Coordinate")
public class Coordinate {
    @PrimaryKey
    private int CoordinateId;
    @ColumnInfo(name = "latitude")
    private double latitude;
    @ColumnInfo(name = "longitude")
    private double longitude;
    @ColumnInfo(name = "local_address")
    private String localAddress;
    @ColumnInfo(name = "country_name")
    private String countryName;

    /**
     * Constructor för Koordinater
     */

    public Coordinate(){

    }

    /**
     * Getters and setters
     * @return
     */
    public int getCoordinateId () {return this.CoordinateId;}
    public void setCoordinateId(int coordId) { this.CoordinateId = coordId; }
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
        this.latitude = lon;
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
        @Query("SELECT * FROM COORDINATE")
        List<Coordinate> getAllCoordinates();


    }

}
