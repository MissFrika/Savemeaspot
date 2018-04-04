package sup.savemeaspot;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.List;

import sup.savemeaspot.DataLayer.Category;

public class SaveTitleActivity extends AppCompatActivity {


    private List<String> exampleTitles;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private String[] exampleStrings;
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
                case "Mushroom":
                    exampleStrings = getResources().getStringArray(R.array.titles_example_mushroom);
                    break;
                case "Fish":
                    exampleStrings = getResources().getStringArray(R.array.titles_example_fish);
                    break;
                case "Berry":
                    exampleStrings = getResources().getStringArray(R.array.titles_example_berry);
                    break;
                case "Fruit":
                    exampleStrings = getResources().getStringArray(R.array.titles_example_fruit);
                    break;
                case "":
                    exampleStrings = new String[]{"Could not load titles"};
                    break;
            }
            for (String item : exampleStrings){
                exampleTitles.add(item);
            }
        }

        // Specifierar en adapter för RecyclerView
        adapter = new TitleRecyclerViewAdapter(exampleTitles);
        recyclerView = findViewById(R.id.title_recycler_view);
        layoutManager = new LinearLayoutManager(this);
        //LayoutManager
        recyclerView.setLayoutManager(layoutManager);
        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        recyclerView.setHasFixedSize(true);

        recyclerView.setAdapter(adapter);

    }
}

