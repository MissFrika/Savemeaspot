package sup.savemeaspot.DataLayer.Models;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by Frida on 2018-05-28.
 */

public class MarkerInfo implements java.io.Serializable {

    private String title;
    private String description;
    private String category;
    private LatLng latLng;
    private String locale;
    private String country;
    private int drawable;


    public MarkerInfo() {
        this.title = title;
        this.description = description;
        this.category = category;
        this.latLng = latLng;
        this.drawable = drawable;

    }
    //Getters & Setters
    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public String getCategory() {
        return category;
    }
    public void setCategory(String category) {
        this.category = category;
    }
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public int getDrawable() {
        return drawable;
    }
    public void setDrawable(int drawable) {
        this.drawable = drawable;
    }
    public String getCountry() {

        return country;
    }
    public void setCountry(String country) {
        this.country = country;
    }
    public String getLocale() {

        return locale;
    }
    public void setLocale(String locale) {
        this.locale = locale;
    }
    public LatLng getLatLng() {

        return latLng;
    }
    public void setLatLng(LatLng latLng) {
        this.latLng = latLng;
    }


}
