package sup.savemeaspot.Activities;

import android.arch.persistence.room.Room;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.List;

import sup.savemeaspot.Adapters.CategoryListViewAdapter;
import sup.savemeaspot.DataLayer.Models.Category;
import sup.savemeaspot.DataLayer.SpotDatabase;
import sup.savemeaspot.R;

public class CategoryCollectionActivity extends AppCompatActivity {

    private List<Category> categories;
    private static RecyclerView recyclerView;
    private Context context;
    private static CategoryListViewAdapter adapter;
    private static RecyclerView.LayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_collection);

        recyclerView = findViewById(R.id.recycler_container_category_collection);

        SpotDatabase database = Room.inMemoryDatabaseBuilder(this.getApplicationContext(), SpotDatabase.class)
                .allowMainThreadQueries() //DO NOT !!!
                .build();
        try {
            List<Category> aCat = database.getDatabase(this).categoryDao().getAllCategories();
            this.categories = aCat;
            database.close();
        } catch (Exception e) {
            e.getMessage();
        }
        // Specifierar en adapter för RecyclerView
        adapter = new CategoryListViewAdapter(this, categories);
        //LayoutManager
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        recyclerView.setHasFixedSize(true);

        recyclerView.setAdapter(adapter);
    }


}
