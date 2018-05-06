package sup.savemeaspot.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
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
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

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
