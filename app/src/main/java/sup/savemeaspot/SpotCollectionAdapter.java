package sup.savemeaspot;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;


import sup.savemeaspot.DataLayer.Category;
import sup.savemeaspot.DataLayer.Spot;

/**
 * Adapter för att hantera Spot-objekt i RecyclerView
 * Created by andre on 2018-04-27.
 */

public class SpotCollectionAdapter extends RecyclerView.Adapter<SpotCollectionAdapter.ViewHolder> {
    private List<Spot> spotDataset;
    private Context context;

    public SpotCollectionAdapter(Context context, List<Spot> items){
        this.context = context;
        this.spotDataset = items;

    }

    /**
     * Metod körs vid skapande av ny ViewHolder
     *
     * @param parent
     * @param viewType
     * @return
     */
    @Override
    public SpotCollectionAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                               int viewType) {
        // skapa ny vy
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_view_layout_spot_collection, parent, false);
        SpotCollectionAdapter.ViewHolder viewHolder = new SpotCollectionAdapter.ViewHolder(v, context, spotDataset);
        return viewHolder;
    }


    @Override
    public void onBindViewHolder(SpotCollectionAdapter.ViewHolder holder, final int position) {
        holder.collectionTextView.setText(spotDataset.get(position).getSpotTitle());
    }

    /**
     * Räknar antal items i recycleView
      */
    @Override
    public int getItemCount() {
        return spotDataset.size();
    }


    /**
     * Klass för RecylerViewHolder for Spots
     */
    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView collectionTextView;
        Button dropDownButton;
        TextView spotDescriptionTextView;
        Button showMapButton;
        Context context;
        List<Spot> spots;

        public ViewHolder(View v, Context context, List<Spot> spots){
            super(v);
            this.spots = spots;
            this.context = context;

            collectionTextView = v.findViewById(R.id.collection_text_view);
            dropDownButton = v.findViewById(R.id.collection_drop_down_button);
            spotDescriptionTextView = v.findViewById(R.id.spot_description_text_view);
            showMapButton = v.findViewById(R.id.show_map_button);

        }
    }
}
