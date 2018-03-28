package sup.savemeaspot.DataLayer;
import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.PrimaryKey;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;
import android.net.Uri;

import java.util.ArrayList;
import java.util.List;

import sup.savemeaspot.R;


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
    private int categoryImg;
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

    public void setCategoryImg(int categoryImg) {
        this.categoryImg = categoryImg;
    }

    public int getCategoryImg() {
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
     *     */
    public Category(){}

    public Category(String name, int image, int deletable){
        this.categoryName = name;
        this.categoryImg = image;
        this.isDeletable = deletable;

    }

    /**
     * Hämtar sökvägen för en resursfil
     * @param resourceId
     * @return
     */
    public static String getURLForResource (int resourceId) {
        return Uri.parse("android.resource:/"+R.class.getPackage().getName()+"/" +resourceId).toString();
    }

    @Dao
    public interface CategoryDao{
        //Ny Category
        @Insert(onConflict = OnConflictStrategy.IGNORE)
        void insertCategories(Category... categories);

        //Uppdatera Category
        @Update(onConflict = OnConflictStrategy.REPLACE)
        void updateCategory(Category category);

        // Ta bort Category
        @Delete()
        void deleteCategories(Category category);

        //Hämta alla Categories
        @Query("SELECT * FROM CATEGORY")
            List<Category> getAllCategories();

        //Hämta Categories med visst Id
        @Query("SELECT * FROM CATEGORY WHERE categoryId LIKE :spotCatId")
            Category getSpotCategory(int spotCatId);

        //Hämta Categories med visst namn.
        @Query("SELECT * FROM CATEGORY WHERE category_name LIKE :catName")
        List<Category> getCategoryByName(String catName);
    }

    public static Category[] populateData() {
        return new Category[] {
                new Category("Fish", R.drawable.fish, 0),
                new Category("Fruit", R.drawable.apple, 0),
                new Category("Berry", R.drawable.cherry, 0),
                new Category("Mushroom", R.drawable.wheat, 0)
        };
    }
}

