package sup.savemeaspot.DataLayer;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;

/**
 * Created by Frida on 2018-03-19.
 */

@Entity(tableName = "spot_join_category",  primaryKeys = {"category_id", "spot_in_cat"},
        foreignKeys = {
                @ForeignKey(entity = Category.class,
                        parentColumns = "category_id",
                        childColumns = "category_id"),
                @ForeignKey(entity = Spot.class,
                        parentColumns = "spot_id",
                        childColumns = "spot_in_cat")
        })
public class Spot_join_Category {

}
