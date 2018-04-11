package sup.savemeaspot;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import sup.savemeaspot.DataLayer.Category;

public class SaveTitleActivity extends AppCompatActivity {


    private List<String> exampleTitles = new ArrayList<String>();
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

        checkIncomingIntents();
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

    /**
     * Kontrollerar om det finns intents medskickade från föregående aktivitet
     */
    private void checkIncomingIntents(){
        if(getIntent().hasExtra("EXTRA_MESSAGE_CATEGORY_ID") && getIntent().hasExtra("EXTRA_MESSAGE_CATEGORY_NAME") &&
                getIntent().hasExtra("EXTRA_MESSAGE_CATEGORY_IMG") && getIntent().hasExtra("EXTRA_MESSAGE_CATEGORY_IS_DELETABLE")){
            Bundle extra = getIntent().getExtras();
            int id = extra.getInt("EXTRA_MESSAGE_CATEGORY_ID");
            String name = extra.getString("EXTRA_MESSAGE_CATEGORY_NAME");
            int image = extra.getInt("EXTRA_MESSAGE_CATEGORY_IMG");
            int isDeletable = extra.getInt("EXTRA_MESSAGE_CATEGORY_IS_DELETABLE");
            this.chosenCategory = new Category(id,name,image,isDeletable);
        }
    }
}

