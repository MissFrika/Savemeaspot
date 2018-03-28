package sup.savemeaspot;

import android.support.v7.widget.RecyclerView;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import sup.savemeaspot.DataLayer.Category;

/**
 * Created by Frika on 2018-03-27.
 */

public class CategoryListViewAdapter extends RecyclerView.Adapter<CategoryListViewAdapter.ViewHolder> {
    private List<Category> categoryDataset;
    public TextView mTextView;

    public CategoryListViewAdapter(List<Category> items) {

        this.categoryDataset = items;
    }

    public void setCategoryDataset(List<Category> items){
        categoryDataset = items;
        notifyDataSetChanged();
    }


    // Create new views (invoked by the layout manager)
    @Override
    public CategoryListViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                   int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_categoryitem, parent, false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }


    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        if(holder instanceof ViewHolder){
             holder.catNameTextView.setText(categoryDataset.get(position).getCategoryName());
             holder.catImgTextView.setText(categoryDataset.get(position).getCategoryImg());
        }

    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return categoryDataset.size();
    }

    //Return item view type
    @Override
    public int getItemViewType(int position) {
        return 0;
    }


    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView catNameTextView;
        public TextView catImgTextView;

        public ViewHolder(View v) {
            super(v);
            catNameTextView = (TextView) v.findViewById(R.id.category_item_name);
            catImgTextView = (TextView) v.findViewById(R.id.category_item_image);
        }

        static ViewHolder inflate(ViewGroup parent){
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_categoryitem, parent, false);
            return new ViewHolder(view);
        }

    }
}

