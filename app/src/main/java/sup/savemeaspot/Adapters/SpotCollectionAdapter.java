package sup.savemeaspot.Adapters;

import android.arch.persistence.room.Room;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.List;


import sup.savemeaspot.Activities.MapsStart;
import sup.savemeaspot.DataLayer.DatabaseHelper;
import sup.savemeaspot.DataLayer.Models.Coordinate;
import sup.savemeaspot.DataLayer.Models.Spot;
import sup.savemeaspot.DataLayer.SpotDatabase;
import sup.savemeaspot.R;

/**
 * Adapter för att hantera Spot-objekt i RecyclerView
 * Created by andre on 2018-04-27.
 */

public class SpotCollectionAdapter extends RecyclerView.Adapter<SpotCollectionAdapter.ViewHolder> {
    private List<Spot> spotDataset;
    private Context context;
    private RecyclerView dropDown;

    public SpotCollectionAdapter(Context context, List<Spot> items, RecyclerView recyclerView){
        this.context = context;
        this.spotDataset = items;
        this.dropDown = recyclerView;
    }

    /**
     * Metod körs vid skapande av ny ViewHolder
     *
     * @param parent
     * @param viewType
     * @return
     */
    @Override
    public SpotCollectionAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // skapa ny vy
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_view_layout_spot_collection, parent, false);
        SpotCollectionAdapter.ViewHolder viewHolder = new SpotCollectionAdapter.ViewHolder(v, context, spotDataset);
        return viewHolder;
    }


    @Override
    public void onBindViewHolder(final SpotCollectionAdapter.ViewHolder holder, final int position) {
        holder.collectionTextView.setText(spotDataset.get(position).getSpotTitle());
        holder.spotDescriptionTextView.setText(spotDataset.get(position).getSpotDescription());
        final Spot spot = spotDataset.get(position);

        holder.spotTitleLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeDropdownLayoutVisibility(holder);
            }
        });
        holder.showOnMapButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, MapsStart.class);

                //Hämta koordinater från databasen
                Coordinate coordinate = DatabaseHelper.getSpotCoordinates(context, spot);
                double latitude = coordinate.getLatitude();
                double longitude = coordinate.getLongitude();

                intent.putExtra("EXTRA_MESSAGE_COORDINATES_LAT", latitude);
                intent.putExtra("EXTRA_MESSAGE_COORDINATES_LONG", longitude);
                //Starta aktivitet
                context.startActivity(intent);

                //Uri gmmIntentUri = Uri.parse("geo:"+ latitude + "," + longitude);
                //Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                //mapIntent.setPackage("com.google.android.apps.maps");
                //context.startActivity(mapIntent);
            }
        });
    }

    /**
     * Räknar antal items i recycleView
      */
    @Override
    public int getItemCount() {
        return spotDataset.size();
    }

    /**
     * Visar eller döljer relativelayout
     * @param holder
     */
    public void changeDropdownLayoutVisibility(SpotCollectionAdapter.ViewHolder holder) {

        if (holder.spotDetails.getVisibility()==View.GONE) {
            holder.spotDetails.setVisibility(View.VISIBLE);
        }
        else if(holder.spotDetails.getVisibility()==View.VISIBLE){
            holder.spotDetails.setVisibility(View.GONE);
        }
    }

    /**
     * Klass för RecylerViewHolder for Spots
     */
    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView collectionTextView;
        TextView spotDescriptionTextView;
        Context context;
        List<Spot> spots;
        RelativeLayout spotDetails;
        RelativeLayout spotTitleLayout;
        ImageButton showOnMapButton;

        public ViewHolder(View v, Context context, List<Spot> spots){
            super(v);
            this.spots = spots;
            this.context = context;
            spotDetails = v.findViewById(R.id.collection_drop_down_layout);
            collectionTextView = v.findViewById(R.id.collection_text_view);
            spotDescriptionTextView = v.findViewById(R.id.spot_description_text_view);
            spotTitleLayout = v.findViewById(R.id.collection_relative_layout);
            showOnMapButton = v.findViewById(R.id.show_map_button);
        }
    }
}
