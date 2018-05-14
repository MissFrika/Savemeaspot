package sup.savemeaspot.Adapters;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import sup.savemeaspot.Activities.SpotCollectionActivity;
import sup.savemeaspot.DataLayer.DatabaseHelper;
import sup.savemeaspot.DataLayer.Models.Spot;
import sup.savemeaspot.R;

/**
 * Adapter för att hantera ändringen av information om Spots i RecyclerViews
 * Created by Frida on 2018-05-04.
 */

public class EditSpotAdapter extends RecyclerView.Adapter<EditSpotAdapter.ViewHolder> {

    private List<Spot> spotDataset;
    private Context context;
    private final Activity activity;

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
    public void onBindViewHolder(ViewHolder holder, final int position) {
        holder.spotTitle.setText(spotDataset.get(position).getSpotTitle());
        //OnClick listener för att ta bort Spot
        holder.deleteSpotBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder alertDialog = createDeleteSpotDialog(position, spotDataset);
                alertDialog.show();
            }
        });
        //OnClick listener för att redigera Spot
       holder.editSpotBtn.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {

           }
       });

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

        public ViewHolder(View view, Context context, List<Spot> spot) {
            super(view);
            this.spots = spot;
            this.context = context;

            spotTitle = view.findViewById(R.id.edit_spot_text_view);
            editSpotBtn = view.findViewById(R.id.edit_spot_button);
            deleteSpotBtn = view.findViewById(R.id.delete_spot_button);
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
