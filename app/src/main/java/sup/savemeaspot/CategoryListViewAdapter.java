package sup.savemeaspot;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import sup.savemeaspot.DataLayer.Category;

/**
 * Created by Frika on 2018-03-27.
 */

public class CategoryListViewAdapter extends RecyclerView.Adapter<CategoryListViewAdapter.ViewHolder> {
    private List<Category> categoryDataset;
    private Context context;

    public CategoryListViewAdapter(List<Category> items, Context context) {

        this.categoryDataset = items;
        this.context = context;
    }


    // Create new views (invoked by the layout manager)
    @Override
    public CategoryListViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                   int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_view_layout_category, parent, false);
        ViewHolder viewHolder = new ViewHolder(v, context, categoryDataset);
        return viewHolder;
    }


    // Byter ut delar av en view (layout manager behövs)
    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
             holder.catNameTextView.setText(categoryDataset.get(position).getCategoryName());
             holder.catImageView.setImageResource(categoryDataset.get(position).getCategoryImg());
            //OnClickListener för view
            holder.relativeLayout.setOnClickListener(new View.OnClickListener() {
                /**
                 * Metod körs om ett objekt i ViewHolder klickas på
                 * @param v
                 */
                @Override
                public void onClick(View v) {
                    //position i view
                    Category aCategory = categoryDataset.get(position);
                    int id = aCategory.getCategoryId();
                    String name = aCategory.getCategoryName();
                    int img = aCategory.getCategoryImg();
                    int delete = aCategory.getIsDeletable();
                    //Nytt intent
                    Intent intent = new Intent(context,SaveTitleActivity.class);
                    intent.putExtra( "EXTRA_MESSAGE_CATEGORY_ID", id);
                    intent.putExtra( "EXTRA_MESSAGE_CATEGORY_NAME", name);
                    intent.putExtra("EXTRA_MESSAGE_CATEGORY_IMG", img);
                    intent.putExtra( "EXTRA_MESSAGE_CATEGORY_IS_DELETABLE", delete);

                    context.startActivity(intent);
                }
            });

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

        TextView catNameTextView;
        ImageView catImageView;
        RelativeLayout relativeLayout;
        List<Category> categories;
        Context context;

        public ViewHolder(View v, Context context, List<Category> category) {
            super(v);
            this.categories = category;
            this.context = context;

            catNameTextView = (TextView) v.findViewById(R.id.category_name_cardview);
            catImageView = (ImageView) v.findViewById(R.id.category_image_cardview);
            relativeLayout = (RelativeLayout) v.findViewById(R.id.relativeLayout);
        }

    }
}

