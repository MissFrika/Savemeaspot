package sup.savemeaspot.DataLayer;
import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.PrimaryKey;
import android.content.Context;
import android.content.ContentValues;
import android.database.sqlite.SQLiteCursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import java.util.List;


/**
 * Created by Frida on 2018-03-19.
 */


@Entity(tableName = "Category")
public class Category {

    @PrimaryKey
    @ColumnInfo( name = "category_id")
    private int CategoryId;
    @ColumnInfo( name = "category_name")
    private String categoryName;
    @ColumnInfo( name = "category_img")
    private String categoryImg;
    @ColumnInfo( name = "is_deletable")
    private boolean isDeletable;


    /**
     * Getters & Setters
     *
     * @return
     */
    public String getCategoryName() {
        return this.categoryName;
    }

    public void setCategoryName(String name) {
        this.categoryName = name;
    }

    public String getImg(String img) {
        return this.categoryImg;
    }

    public String setImg() {
        return this.categoryImg;
    }

    public boolean getIsDeletable() {
        return this.isDeletable;
    }

    /**
     * Constructor för ny kategori
     *
     * @param title
     * @param image
     */
    public Category(String title, String image) {
        categoryName = title;
        categoryImg = image;
        isDeletable = true;
    }

    @Dao
    public interface CategoryDao{

    }
}

