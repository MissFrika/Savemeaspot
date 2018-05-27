package sup.savemeaspot.Adapters;

import android.app.Activity;
import android.app.AlertDialog;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.provider.ContactsContract;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import sup.savemeaspot.Activities.SpotCollectionActivity;
import sup.savemeaspot.DataLayer.DatabaseHelper;
import sup.savemeaspot.DataLayer.Models.Category;
import sup.savemeaspot.DataLayer.Models.Spot;
import sup.savemeaspot.DataLayer.SpotDatabase;
import sup.savemeaspot.R;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;

/**
 * Adapter för att hantera ändringen av information om Spots i RecyclerViews
 * Created by Frida on 2018-05-04.
 */

public class EditSpotAdapter extends RecyclerView.Adapter<EditSpotAdapter.ViewHolder> {

    private List<Spot> spotDataset;
    private Context context;
    private static PopupWindow editPopupWindow;
    private final Activity activity;
    private static boolean popupShows = false;

    public EditSpotAdapter (Context context, List<Spot> spot, Activity activityContext){
        this.spotDataset = spot;
        this.context = context;
        this.activity = activityContext;

    }

    @Override
    public EditSpotAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.edit_spots_card_view_layout, parent, false);
        EditSpotAdapter.ViewHolder viewHolder = new EditSpotAdapter.ViewHolder(v, context, spotDataset);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        holder.spotTitle.setText(spotDataset.get(position).getSpotTitle());
        //OnClick listener för att ta bort Spot
        holder.deleteSpotBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Ny AlertDialog instansieras
                AlertDialog.Builder alertDialog = createDeleteSpotDialog(position, spotDataset);
                alertDialog.show();
            }
        });
        //OnClick listener för att redigera Spot
       holder.editSpotBtn.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               if(!activity.isFinishing()) {
                   activity.runOnUiThread(new Runnable() {
                       @Override
                       public void run() {
                           //Öppna popup-fönster
                           editSpotPopup(holder, position);
                       }
                   });
               }
           }});
    }

    @Override
    public int getItemCount() {

        return spotDataset.size();
    }

    /**
     * Skapa en AlertDialog som frågar en användare att konfirmera borttagning av Spot
     * @param position
     * @return
     */
    private AlertDialog.Builder createDeleteSpotDialog(final int position, final List<Spot> spotDataset){
        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(activity);
        alertDialog.setTitle("Delete Spot");
        alertDialog.setMessage(activity.getApplicationContext().getText(R.string.delete_confirmation) + " " + spotDataset.get(position).getSpotTitle() +"?" );
        //Acceptera
        alertDialog.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String spotName = spotDataset.get(position).getSpotTitle();
                DatabaseHelper.deleteSpot(activity.getApplicationContext(), spotDataset.get(position));
                dialog.dismiss();
                deleteSpot(position);
                Toast.makeText(activity, spotName + " has been deleted.", Toast.LENGTH_SHORT).show();

            }
        });
        //Neka
        alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        return alertDialog;
    }


    public static class ViewHolder extends RecyclerView.ViewHolder{

        List<Spot> spots;
        Context context;
        TextView spotTitle;
        ImageView editSpotBtn;
        ImageView deleteSpotBtn;
        RelativeLayout relativeLayout;

        public ViewHolder(View view, Context context, List<Spot> spot) {
            super(view);
            this.spots = spot;
            this.context = context;

            spotTitle = view.findViewById(R.id.edit_spot_text_view);
            editSpotBtn = view.findViewById(R.id.edit_spot_button);
            deleteSpotBtn = view.findViewById(R.id.delete_spot_button);
            relativeLayout = view.findViewById(R.id.edit_spots_relative_layout);
        }
    }

    /**
     * Radera Spot från dataset
     * @param position
     */
    private void deleteSpot(int position){
        spotDataset.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, getItemCount());
    }

    /**
     * Hämtar ut angiven data från textfält och skapar ett nytt Spot-objekt
     * @param context
     * @param view
     * @param spot old Spot
     */
    private void saveChanges(Context context, View view, Spot spot, Category category){
        try {
            //Spot titel
            EditText spotTitleET = view.findViewById(R.id.edit_spot_title_et);
                if(!spotTitleET.getText().toString().isEmpty()) {
                    String title = spotTitleET.getText().toString();
                    //Spot beskrivning
                    EditText spotDescET = view.findViewById(R.id.edit_spot_desc_et);
                    String description = spotDescET.getText().toString();
                    //Kategori
                    DatabaseHelper.editSpot(context, spot, title, description, category.getCategoryId());
                }
                else{
                    Toast.makeText(context, "Spot title can not be empty.", Toast.LENGTH_SHORT).show();
                }
        }
        catch (Exception ex){
            Toast.makeText(context, ex.getMessage(), Toast.LENGTH_SHORT);
        }
    }

    /**
     * Skapar en PopupWindow för att redigera en Spot
     * @param position
     */
    private void editSpotPopup(ViewHolder holder, final int position){
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(LAYOUT_INFLATER_SERVICE);
        final View customView = inflater.inflate(R.layout.edit_spot_popup_layout, null);
        PopupWindow newWindow = new PopupWindow(
                customView,
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT, true
        );

        //Finns redan ett popup eller ej
        if(!popupShows) {
            editPopupWindow = newWindow;
            popupShows = true;
        }
        else{
            editPopupWindow.dismiss();
            editPopupWindow = newWindow;
        }
        //Fyll textfält med information från Spot
        EditText spotTitle = customView.findViewById(R.id.edit_spot_title_et);
        spotTitle.setText(spotDataset.get(position).getSpotTitle());
        EditText spotDescription = customView.findViewById(R.id.edit_spot_desc_et);
        spotDescription.setText(spotDataset.get(position).getSpotDescription());
        //Hämta kategorier
        final Spinner spinner = customView.findViewById(R.id.spinner_edit_spot_category);

        ArrayList<Category> categories = DatabaseHelper.getAllCategories(context);

        //Hämta ut alla titlar från kategorierna
        List<String> listOfCategoryTitles = new ArrayList<>();
        for(Category category : categories){
            CategoryStringWithId categoryToAdd = new CategoryStringWithId(category.getCategoryId(),category.getCategoryName());
            listOfCategoryTitles.add(category.getCategoryName());
        }
        //Populera spinner med kategorinamn
        ArrayAdapter<String> categoryArrayAdapter = new ArrayAdapter<>(context, android.R.layout.simple_list_item_1, listOfCategoryTitles);
        categoryArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(categoryArrayAdapter);
        //Hitta specifik kategori som skall visas
        Category spotCategory = DatabaseHelper.getSpotCategory(context, spotDataset.get(position));
        String categoryString = spotCategory.getCategoryName();
        int index = categoryArrayAdapter.getPosition(categoryString);
        spinner.setSelection(index);

        //Instansiera knapp för att spara förändringar som gjorts
        ImageButton btn = customView.findViewById(R.id.save_edited_spot_button);
        //OnClickListener - sparaknapp
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    String selectedCategory = spinner.getSelectedItem().toString();
                    Category categoryToSave = DatabaseHelper.getCategoryByName(context, selectedCategory);
                    //Spara ändringar
                    saveChanges(context, customView, spotDataset.get(position), categoryToSave);
                    Toast.makeText(context, "Changes have been saved.", Toast.LENGTH_SHORT).show();
                    editPopupWindow.dismiss();
                    activity.recreate();
                }
                catch (Exception ex){
                    Toast.makeText(context, ex.getMessage(),Toast.LENGTH_SHORT).show();
                }
            }
        });
        ImageButton closeButton = customView.findViewById(R.id.edit_spot_close_button);
        //OnClickListener stäng-knapp
        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final AlertDialog.Builder alert = new AlertDialog.Builder(activity);
                alert.setTitle("Cancel");
                alert.setMessage("Are you sure you want to cancel? Unsaved changes will be lost.");
                alert.setPositiveButton(activity.getApplicationContext().getText(R.string.am_sure), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                        editPopupWindow.dismiss();
                        popupShows = false;
                    }
                });
                alert.setNegativeButton(activity.getApplicationContext().getText(R.string.not_sure), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                         dialogInterface.dismiss();
                    }
                });
                alert.show();
            }
        });
        if(Build.VERSION.SDK_INT>=21){
            editPopupWindow.setElevation(5.0f);
        }
        editPopupWindow.showAtLocation(holder.relativeLayout, Gravity.CENTER,0,0);
    }

    /**
     * Används för att hantera kategori-spinnern
     */
    private static class CategoryStringWithId {
        public String string;
        public int id;

        public CategoryStringWithId(int id, String string) {
            this.string = string;
            this.id = id;
        }

        public int getId() {
            return id;
        }

        @Override
        public String toString() {
            return string;
        }
    }
}
