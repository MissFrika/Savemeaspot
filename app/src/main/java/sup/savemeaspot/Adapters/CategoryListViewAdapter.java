package sup.savemeaspot.Adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import sup.savemeaspot.Activities.SaveSpotCategoryActivity;
import sup.savemeaspot.Activities.SaveTitleActivity;
import sup.savemeaspot.DataLayer.Models.Category;
import sup.savemeaspot.DataLayer.Models.Coordinate;
import sup.savemeaspot.DataLayer.DatabaseHelper;
import sup.savemeaspot.R;

/**
 * Created by Frika on 2018-03-27.
 */

public class CategoryListViewAdapter extends RecyclerView.Adapter<CategoryListViewAdapter.ViewHolder> {
    private List<Category> categoryDataset;
    private Context context;
    private Coordinate extraCoordinates;
    private PopupWindow editPopupWindow;

    /**
     * Konstruktor + koordinater
     * @param context
     * @param items
     * @param extraCoordinates
     */
    public CategoryListViewAdapter(Context context, List<Category> items, Coordinate extraCoordinates) {

        this.categoryDataset = items;
        this.context = context;
        this.extraCoordinates = extraCoordinates;
    }

    /**
     * Konstruktor
     * @param context
     * @param items
     */
    public CategoryListViewAdapter(Context context, List<Category> items) {

        this.categoryDataset = items;
        this.context = context;
    }


    // Create new views (invoked by the layout manager)
    @Override
    public CategoryListViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //Är context från aktiviteten SaveSpotCategoryActivity
        if (context instanceof SaveSpotCategoryActivity) {
            // create a new view
            View v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.card_view_layout_category, parent, false);
            ViewHolder viewHolder = new ViewHolder(v, context, categoryDataset);
            return viewHolder;
        }
        //Är context från aktiviteten CategoryCollectionActivity
        else {
            View v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.edit_category_card_view_layout, parent, false);
            ViewHolder viewHolder = new ViewHolder(v, context, categoryDataset);
            return viewHolder;
        }
    }

    /**
     * Hanterar innehållet i recylerviewn
     * @param holder
     * @param position
     */
    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        if(holder.isCategoryCollection) {
            holder.catNameTextView.setText(categoryDataset.get(position).getCategoryName());
            holder.catImageView.setImageResource(categoryDataset.get(position).getCategoryImg());

            if(categoryDataset.get(position).getIsDeletable()==0){
                holder.catDeleteImageView.setVisibility(View.GONE);
                holder.catEditImageView.setVisibility(View.GONE);
            }
            final AlertDialog.Builder confirmationWindowBuilder = createDeleteCategoryDialog(position);
            holder.catEditImageView.setOnClickListener(new View.OnClickListener() {
            @Override
                public void onClick(View v) {

                }
            });
            holder.catDeleteImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                   AlertDialog alertDialog = confirmationWindowBuilder.create();
                   alertDialog.show();
                }
            });
        }
        else if (!holder.isCategoryCollection) {

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
                    Intent intent = new Intent(context, SaveTitleActivity.class);
                    intent.putExtra("EXTRA_MESSAGE_CATEGORY_ID", id);
                    intent.putExtra("EXTRA_MESSAGE_CATEGORY_NAME", name);
                    intent.putExtra("EXTRA_MESSAGE_CATEGORY_IMG", img);
                    intent.putExtra("EXTRA_MESSAGE_CATEGORY_IS_DELETABLE", delete);

                    Coordinate coordinate = new Coordinate();
                    coordinate.putExtraMessageCoordinate(intent, extraCoordinates);

                    context.startActivity(intent);
                }
            });
        }
    }

    /**
     * Skapa en dialogruta för att acceptera eller neka radering av en kategori
     * @param position
     * @return
     */
    private AlertDialog.Builder createDeleteCategoryDialog(final int position) {
        final AlertDialog.Builder confirmationDialog = new AlertDialog.Builder(context);
        confirmationDialog.setTitle(context.getText(R.string.delete_category));
        confirmationDialog.setMessage(context.getText(R.string.delete_confirmation) + " " + categoryDataset.get(position).getCategoryName() + "?");

        //Acceptera
        confirmationDialog.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(final DialogInterface dialog, int which) {
                        AlertDialog.Builder alertDialog = createDeleteCategorySpotsDialog(position);
                        alertDialog.show();
                    }
                });

        //Neka
        confirmationDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                //Avbryt och stäng dialogruta
                dialog.dismiss();
            }
        });

        return confirmationDialog;
    }


    /**
     * Skapa en alert-ruta för att be en användare acceptera att Spots tas bort om kategorier tas bort
     * @param position
     * @return
     */
    private AlertDialog.Builder createDeleteCategorySpotsDialog(final int position){

        final AlertDialog.Builder confirmDeleteSpots = new AlertDialog.Builder(context);
        //Acceptera att spots tas bort
        confirmDeleteSpots.setMessage(context.getText(R.string.delete_category_spots_included) + " " + categoryDataset.get(position).getCategoryName() + " " + context.getText(R.string.category_spots_will_delete));
        confirmDeleteSpots.setPositiveButton(context.getText(R.string.am_sure), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                //Acceptera och radera kategori från databasen
                DatabaseHelper.deleteCategory(context, categoryDataset.get(position));
                Toast.makeText(context, categoryDataset.get(position).getCategoryName() + " " + context.getText(R.string.has_deleted), Toast.LENGTH_SHORT).show();
                deleteCategory(position);
                dialogInterface.dismiss();
            }
        });
        confirmDeleteSpots.setNegativeButton(context.getText(R.string.not_sure), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        return confirmDeleteSpots;
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

    // Insert a new item to the RecyclerView on a predefined position
    public void insertCategory(Category category) {
        categoryDataset.add(category);

        notifyItemInserted(categoryDataset.size());
    }

    /**
     * Tar bort en kategori från en position i dataset
     * @param position
     */
    public void deleteCategory(int position){
        categoryDataset.remove(position);
        notifyItemRemoved(position);
    }

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView catNameTextView;
        ImageView catImageView;
        ImageView catDeleteImageView;
        ImageView catEditImageView;
        RelativeLayout relativeLayout;
        List<Category> categories;
        Context context;
        //False om adaptern ej används i CategoryCollectionActivity
        boolean isCategoryCollection;

        public ViewHolder(View v, Context context, List<Category> category) {
            super(v);
            this.categories = category;
            this.context = context;
            if (v.findViewById(R.id.relativeLayout_category_collection) != null){
                catNameTextView = (TextView) v.findViewById(R.id.category_name_cardview);
                catImageView = (ImageView) v.findViewById(R.id.category_image_cardview);
                catDeleteImageView = v.findViewById(R.id.delete_category_icon);
                catEditImageView = v.findViewById(R.id.edit_category_icon);
                relativeLayout = (RelativeLayout) v.findViewById(R.id.relativeLayout_category_collection);
                isCategoryCollection = true;
            }
            else if(v.findViewById(R.id.relativeLayout) != null){
                catNameTextView = (TextView) v.findViewById(R.id.category_name_cardview);
                catImageView = (ImageView) v.findViewById(R.id.category_image_cardview);
                relativeLayout = (RelativeLayout) v.findViewById(R.id.relativeLayout);
                isCategoryCollection = false;
            }

        }

    }
}

