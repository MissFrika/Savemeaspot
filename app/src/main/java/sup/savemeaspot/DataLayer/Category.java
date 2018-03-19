package sup.savemeaspot.DataLayer;
import android.content.Context;
import android.content.ContentValues;
import android.database.sqlite.SQLiteCursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import java.util.List;


/**
 * Created by Frida on 2018-03-19.
 */

public class Category {

    private int CategoryID;
    private String categoryName;
    private String categoryImg;
    private boolean isDeletable;
    private List<Spot> spots;

    /**
     * Getters & Setters
     * @return
     */
    public String getCategoryName(){
        return this.categoryName;
    }
    public void setCategoryName(String name){
        this.categoryName = name;
    }
    public String getImg(String img){
       return this.categoryImg;
    }
    public String setImg(){
        return this.categoryImg;
    }
    public List<Spot> getSpots(){
        return this.spots;
    }
    public boolean getIsDeletable(){
        return this.isDeletable;
    }
    /**
     * Constructor för ny kategori
     * @param title
     * @param image
     */
    public Category(String title,String image){
        categoryName = title;
        categoryImg =image;
        isDeletable = true;
    }

    /**
     * Hämtar alla categorier som finns i databasen
     */
    //public List<Category> getAllCategories(){
        //List<Category> allCategories;
        //return allCategories;
    //}
}

