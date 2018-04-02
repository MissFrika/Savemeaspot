package sup.savemeaspot;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.util.List;

import sup.savemeaspot.DataLayer.Category;

public class SaveTitleActivity extends AppCompatActivity {

    private List<String> exampleTitlesFruit;
    private List<String> exampleTitlesBerry;
    private List<String> exampleTitlesFish;
    private List<String> exampleTitlesMushroom;
    private List<String> titlesToDisplay;
    private Category chosenCategory;

    public SaveTitleActivity(){}

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_save_title);

        Bundle extra = getIntent().getExtras();
        //Kontrollerar om intent är null
        if (extra != null) {
            try {
                //Hämtar ut alla strings i intent och skapar ett Category-objekt
                this.chosenCategory = new Category(extra.getInt("EXTRA_MESSAGE_CATEGORY_ID"), extra.getString("EXTRA_MESSAGE_CATEGORY_NAME"),
                        extra.getInt("EXTRA_MESSAGE_CATEGORY_IMG"), extra.getInt("EXTRA_MESSAGE_CATEGORY_IS_DELETABLE"));

            } catch (NullPointerException e) {

            }
        }
        //Kontrollerar vilken kategori som valts från SaveSpotCategoryActivity och instansierar "titlesToDisplay"
        if(this.chosenCategory.getCategoryName()!=null){

            switch (chosenCategory.getCategoryName()){
                case "Mushroom": this.titlesToDisplay = exampleTitlesMushroom;
                    break;
                case "Fish": this.titlesToDisplay = exampleTitlesFish;
                    break;
                case "Berry": this.titlesToDisplay = exampleTitlesBerry;
                    break;
                case "Fruit": this.titlesToDisplay = exampleTitlesFruit;
                    break;
                case "": this.titlesToDisplay.add("Could not load titles");
                    break;
            }
        }
    }
}

