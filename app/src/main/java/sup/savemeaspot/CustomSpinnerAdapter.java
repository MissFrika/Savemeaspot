package sup.savemeaspot;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

/**
 * En anpassad adapter för Spinner. Används för att fylla
 * Created by Frida on 2018-04-18.
 */

public class CustomSpinnerAdapter extends ArrayAdapter {

    private int[] spinnerImages;
    private Context context;

    public CustomSpinnerAdapter(@NonNull Context context, int[] spinnerImages) {
        super(context, R.layout.custom_spinner_layout);
        this.spinnerImages = spinnerImages;
        this.context = context;


    }

    @Override
    public int getCount() {
        return spinnerImages.length;
    }

    //Hämtar Viewn för spinnern
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        ViewHolder mViewHolder = new ViewHolder();
        if (convertView == null) {
            LayoutInflater mInflater = (LayoutInflater) context.
                    getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = mInflater.inflate(R.layout.custom_spinner_layout, parent, false);
            mViewHolder.mIcon = (ImageView) convertView.findViewById(R.id.category_icon);
            convertView.setTag(mViewHolder);
        } else {
            mViewHolder = (ViewHolder) convertView.getTag();
        }
        mViewHolder.mIcon.setImageResource(spinnerImages[position]);

        return convertView;
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return super.getDropDownView(position, convertView, parent);
    }

    //Inre klass ViewHolder
    private static class ViewHolder{
        ImageView mIcon;
    }
}
