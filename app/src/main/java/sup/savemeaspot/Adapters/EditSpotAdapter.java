package sup.savemeaspot.Adapters;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import sup.savemeaspot.Activities.SpotCollectionActivity;
import sup.savemeaspot.DataLayer.DatabaseHelper;
import sup.savemeaspot.DataLayer.Models.Spot;
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
               LayoutInflater inflater = (LayoutInflater) context.getSystemService(LAYOUT_INFLATER_SERVICE);
               View customView = inflater.inflate(R.layout.edit_spot_popup_layout, null);
               PopupWindow newWindow = new PopupWindow(
                       customView,
                       ViewGroup.LayoutParams.MATCH_PARENT,
                       ViewGroup.LayoutParams.MATCH_PARENT
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
               ImageButton closeButton = customView.findViewById(R.id.edit_spot_close_button);
               //Fyll textfält med information från Spot
               EditText spotTitle = customView.findViewById(R.id.edit_spot_title_et);
               spotTitle.setText(spotDataset.get(position).getSpotTitle());
               EditText spotDescription = customView.findViewById(R.id.edit_spot_desc_et);
               spotDescription.setText(spotDataset.get(position).getSpotDescription());

               //Instansiera knapp för att spara förändringar som gjorts
               ImageButton btn = customView.findViewById(R.id.save_edited_spot_button);
               //OnClickListener - sparaknapp
               btn.setOnClickListener(new View.OnClickListener() {
                   @Override
                   public void onClick(View view) {
                       Toast.makeText(context, "Changes has been saved.", Toast.LENGTH_SHORT).show();
                       editPopupWindow.dismiss();;
                   }
               });

               //OnClickListener stäng-knapp
               closeButton.setOnClickListener(new View.OnClickListener() {
                   @Override
                   public void onClick(View view) {
                       editPopupWindow.dismiss();
                       popupShows = false;
                   }
               });
               if(Build.VERSION.SDK_INT>=21){
                   editPopupWindow.setElevation(5.0f);
               }
               editPopupWindow.showAtLocation(holder.relativeLayout, Gravity.CENTER,0,0);
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
     * Deleta a spot from the dataset
     * @param position
     */
    private void deleteSpot(int position){
        spotDataset.remove(position);
        notifyItemRemoved(position);
    }
}
