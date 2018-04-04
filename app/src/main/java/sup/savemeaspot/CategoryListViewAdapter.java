package sup.savemeaspot;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import sup.savemeaspot.DataLayer.Category;

/**
 * Created by Frika on 2018-03-27.
 */

public class CategoryListViewAdapter extends RecyclerView.Adapter<CategoryListViewAdapter.ViewHolder> {
    private List<Category> categoryDataset;

    public CategoryListViewAdapter(List<Category> items) {

        this.categoryDataset = items;
    }


    // Create new views (invoked by the layout manager)
    @Override
    public CategoryListViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                   int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_view_layout_category, parent, false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }


    // Byter ut delar av en view (layout manager behövs)
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
             holder.catNameTextView.setText(categoryDataset.get(position).getCategoryName());
             holder.catImageView.setImageResource(categoryDataset.get(position).getCategoryImg());

    }

    // Returnerar storleken på dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return categoryDataset.size();
    }

    //Returnerar viewtyp
    @Override
    public int getItemViewType(int position) {
        return 0;
    }


    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView catNameTextView;
        public ImageView catImageView;

        public ViewHolder(View v) {
            super(v);
            catNameTextView = (TextView) v.findViewById(R.id.category_name_cardview);
            catImageView = (ImageView) v.findViewById(R.id.category_image_cardview);
        }

        static ViewHolder inflate(ViewGroup parent){
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_view_layout_category, parent, false);
            return new ViewHolder(view);
        }

    }
}

