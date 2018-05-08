package sup.savemeaspot.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import sup.savemeaspot.DataLayer.Models.Spot;
import sup.savemeaspot.R;

/**
 * Adapter för att hantera ändringen av information om Spots i RecyclerViews
 * Created by Frida on 2018-05-04.
 */

public class EditSpotAdapter extends RecyclerView.Adapter<EditSpotAdapter.ViewHolder> {

    private List<Spot> spotDataset;
    private Context context;

    public EditSpotAdapter (Context context, List<Spot> spot){
        this.spotDataset = spot;
        this.context = context;
    }

    @Override
    public EditSpotAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.edit_spots_card_view_layout, parent, false);
        EditSpotAdapter.ViewHolder viewHolder = new EditSpotAdapter.ViewHolder(v, context, spotDataset);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.spotTitle.setText(spotDataset.get(position).getSpotTitle());
        //OnClick listener för att ta bort Spot
        holder.deleteSpotBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

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
}
