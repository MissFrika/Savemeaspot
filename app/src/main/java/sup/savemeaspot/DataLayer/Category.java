package sup.savemeaspot.DataLayer;
import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.PrimaryKey;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;
import android.content.Context;
import android.content.ContentValues;
import android.database.sqlite.SQLiteCursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import java.util.List;
import java.util.Locale;


/**
 * Created by Frida on 2018-03-19.
 */


@Entity(tableName = "Category")
public class Category {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo( name = "categoryid")
    private int CategoryId;
    @ColumnInfo( name = "category_name")
    private String categoryName;
    @ColumnInfo( name = "category_img")
    private String categoryImg;
    @ColumnInfo( name = "is_deletable")
    private int isDeletable;


    /**
     * Getters & Setters
     * @return
     */
    public int getCategoryId(){
        return this.CategoryId;
    }
    public void setCategoryId (int categoryId){
        this.CategoryId = categoryId;
    }
    public String getCategoryName() {
        return this.categoryName;
    }

    public void setCategoryName(String name) {
        this.categoryName = name;
    }

    public void setCategoryImg(String categoryImg) {
        this.categoryImg = categoryImg;
    }

    public String getCategoryImg() {
        return this.categoryImg;
    }

    public int getIsDeletable() {
        return this.isDeletable;
    }
    public void setIsDeletable(int isDeletable){
        this.isDeletable = isDeletable;
    }

    /**
     * Constructor för ny kategori
     *
     */
    public Category(){}

    public Category(String name, String image, int deletable){
        this.categoryName = name;
        this.categoryImg = image;
        this.isDeletable = deletable;
    }

    @Dao
    public interface CategoryDao{
        //Ny kategori
        @Insert(onConflict = OnConflictStrategy.REPLACE)
        void insertCategories(Category... categories);

        //Get all categories
        @Query("SELECT * FROM CATEGORY")
            List<Category> getAllCategories();

        //Get category name from id
        @Query("SELECT * FROM Category where categoryid like :spotCatId")
            Category getSpotCategory(int spotCatId);
        //Update
        @Update(onConflict = OnConflictStrategy.REPLACE)
        void updateCategory(Category category);
    }

    public static Category[] populateData() {
        return new Category[] {
                new Category("Fish", "Path", 0),
                new Category("Fruit", "Path", 0),
                new Category("Berry", "Path", 0),
                new Category("Mushroom", "Path", 0)
        };
    }
}

