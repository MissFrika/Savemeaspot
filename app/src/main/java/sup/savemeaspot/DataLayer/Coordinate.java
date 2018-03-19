package sup.savemeaspot.DataLayer;
import android.content.Context;
import android.content.ContentValues;
import android.database.sqlite.SQLiteCursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


/**
 * Created by Frida on 2018-03-07.
 */

/** Klass för att hantera koordinater GPS**/
public class Coordinate {
    private int CoordinateID;
    private double latitude;
    private double longitude;
    private String localAddress;
    private String countryName;

    /**
     * Constructor för Koordinater
     */
    public Coordinate(double lat, double lon, String address, String country){
        latitude = lat;
        longitude = lon;
        localAddress = address;
        countryName = country;

    }

    public double getLatitude(){
        return this.latitude;
    }
    public double getLongitude(){
        return this.longitude;
    }
    public String getLocalAddress(){
        return this.localAddress;
    }
    public String getCountryName(){
        return this.countryName;
    }


}
