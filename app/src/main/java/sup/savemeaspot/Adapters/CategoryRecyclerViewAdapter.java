package sup.savemeaspot.Adapters;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import sup.savemeaspot.Activities.MapsStart;
import sup.savemeaspot.Activities.SaveSpotCategoryActivity;
import sup.savemeaspot.Activities.SaveTitleActivity;
import sup.savemeaspot.DataLayer.Models.Category;
import sup.savemeaspot.DataLayer.Models.Coordinate;
import sup.savemeaspot.DataLayer.DatabaseHelper;
import sup.savemeaspot.DataLayer.Models.Spot;
import sup.savemeaspot.R;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;

/**
 * Created by Frika on 2018-03-27.
 */

public class CategoryRecyclerViewAdapter extends RecyclerView.Adapter<CategoryRecyclerViewAdapter.ViewHolder> {
    private List<Category> categoryDataset;
    private Context context;
    private Coordinate extraCoordinates;
    private PopupWindow editPopupWindow;
    private final Activity activity;
    private Category newCategory = new Category();
    private final int[] drawables = new int[]{R.drawable.apple,R.drawable.cherry, R.drawable.fish, R.drawable.wheat, R.drawable.water,
            R.drawable.heart,R.drawable.star, R.drawable.fire, R.drawable.building, R.drawable.bus, R.drawable.boat, R.drawable.plane, R.drawable.train};

    /**
     * Konstruktor + koordinater
     * @param context
     * @param items
     * @param extraCoordinates
     */
    public CategoryRecyclerViewAdapter(Context context, List<Category> items, Coordinate extraCoordinates, Activity activity) {

        this.categoryDataset = items;
        this.context = context;
        this.activity = activity;
        this.extraCoordinates = extraCoordinates;
    }

    /**
     * Konstruktor
     * @param context
     * @param items
     */
    public CategoryRecyclerViewAdapter(Context context, List<Category> items, Activity activity) {

        this.categoryDataset = items;
        this.context = context;
        this.activity = activity;
    }


    /**
     *  Skapa nya vyer (anropas av layout manager)
     */

    @Override
    public CategoryRecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //Är context från aktiviteten SaveSpotCategoryActivity
        if (context instanceof SaveSpotCategoryActivity) {
            //Skapa ny vy
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
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        if(holder.isCategoryCollection) {
            holder.catNameTextView.setText(categoryDataset.get(position).getCategoryName());
            holder.catImageView.setImageResource(categoryDataset.get(position).getCategoryImg());
            final AlertDialog.Builder confirmationWindowBuilder = createDeleteCategoryDialog(position);

            holder.relativeLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //Visar knappen om den är dold
                    if(holder.showOnMapButton.getVisibility() == View.GONE){
                        holder.showOnMapButton.setVisibility(View.VISIBLE);

                    }
                    //Döljer knappen om den är synlig
                    else if(holder.showOnMapButton.getVisibility() == View.VISIBLE){
                        holder.showOnMapButton.setVisibility(View.GONE);
                    }
                }
            });

            //Öppnar aktiviteten MapsStart och skickar med ett Intent som kan hanteras
            holder.showOnMapButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //Skapa intent
                    Intent intent = new Intent(context, MapsStart.class);
                    ArrayList<Spot> spotsInCategory = (ArrayList<Spot>)DatabaseHelper.getSpotsByCategory(context, categoryDataset.get(position));
                    intent.putExtra("EXTRA_MESSAGE_SPOTS_IN_CATEGORY", spotsInCategory);

                    activity.startActivity(intent);
                }
            });


            holder.catEditImageView.setOnClickListener(new View.OnClickListener() {
            @Override
                public void onClick(View view) {
                LayoutInflater inflater = (LayoutInflater) context.getSystemService(LAYOUT_INFLATER_SERVICE);
                final View customView = inflater.inflate(R.layout.category_popup_layout, null);
                editPopupWindow = new PopupWindow(
                        customView,
                        LayoutParams.MATCH_PARENT,
                        LayoutParams.MATCH_PARENT,true
                );

                // Set an elevation value for popup window
                // Call requires API level 21
                if(Build.VERSION.SDK_INT>=21){
                    editPopupWindow.setElevation(5.0f);
                }
                ImageButton closeButton = customView.findViewById(R.id.popup_category_close_button);
                closeButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        editPopupWindow.dismiss();
                    }
                });

                ImageButton saveButton = customView.findViewById(R.id.popup_category_save_button);
                saveButton.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View view){
                        //Hämta titel från EditText-fält
                        EditText categoryTitleToSave = customView.findViewById(R.id.popup_category_name_editText);
                        String categoryTitle = categoryTitleToSave.getText().toString();
                        if(!categoryTitle.isEmpty()){
                            try {
                                //Spara ändringar i kategorin till databasen
                                DatabaseHelper.editCategory(context, categoryDataset.get(position), categoryTitle, newCategory.getCategoryImg());
                                Toast.makeText(context, "Changes have been saved.", Toast.LENGTH_SHORT).show();
                                editPopupWindow.dismiss();
                                activity.recreate();
                            }
                            catch (Exception ex){
                                Log.d("ex", ex.getMessage());
                            }
                        }
                        else{
                            Toast.makeText(context, "Category name can not be empty.",Toast.LENGTH_SHORT).show();
                        }
                    }
                });

                editPopupWindow.showAtLocation(holder.relativeLayout, Gravity.CENTER,0,0);
                EditText catName = customView.findViewById(R.id.popup_category_name_editText);
                catName.setText(categoryDataset.get(position).getCategoryName());
                setSpinner();
            }});

            holder.catDeleteImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                   AlertDialog alertDialog = confirmationWindowBuilder.create();
                   alertDialog.show();

                }
            });

            if(categoryDataset.get(position).getIsDeletable()==0){
                holder.catDeleteImageView.setVisibility(View.GONE);
                holder.catEditImageView.setVisibility(View.GONE);
            }
        }
        else if (!holder.isCategoryCollection) {

            holder.catNameTextView.setText(categoryDataset.get(position).getCategoryName());
            holder.catImageView.setImageResource(categoryDataset.get(position).getCategoryImg());
            //OnClickListener för view
            holder.relativeLayout.setOnClickListener(new View.OnClickListener() {
                /**
                 * Metod körs om ett objekt i ViewHolder klickas på
                 *
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
        notifyItemRangeChanged(position, getItemCount());
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
        Button showOnMapButton;
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
                showOnMapButton = v.findViewById(R.id.show_spots_category_button);
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

    /**
     * Sätt spinner till editPopupWindow
     */
    private void setSpinner(){
        //Spinner med anpassad adapter
        CustomSpinnerAdapter customAdapter = new CustomSpinnerAdapter(context, drawables);
        customAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        Spinner spinner = (Spinner) editPopupWindow.getContentView().findViewById(R.id.popup_category_icon_spinner);
        spinner.setAdapter(customAdapter);

        //On Select listener
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                int resourceId = drawables[i];
                newCategory.setCategoryImg(resourceId);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }
}

