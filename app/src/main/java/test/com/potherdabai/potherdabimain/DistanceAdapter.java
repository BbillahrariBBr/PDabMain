package test.com.potherdabai.potherdabimain;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class DistanceAdapter extends RecyclerView.Adapter<DistanceAdapter.DistanceViewHolder> {
    private Context f24c;
    String distanceTxt;
    String durationTxt;

    public static class DistanceViewHolder extends RecyclerView.ViewHolder {
        TextView distance_txt;
        TextView duration_textview;
        View f23v;

        public DistanceViewHolder(View itemView) {
            super(itemView);
            this.f23v = itemView;
            this.distance_txt = (TextView) itemView.findViewById(R.id.item_distance);
            this.duration_textview = (TextView) itemView.findViewById(R.id.item_duration);
        }
    }

    public DistanceAdapter(Context c, String distanceTxt, String durationTxt) {
        this.f24c = c;
        this.distanceTxt = distanceTxt;
        this.durationTxt = durationTxt;
    }

    public int getItemCount() {
        return 1;
    }

    public void onBindViewHolder(DistanceViewHolder holder, int position) {
        holder.distance_txt.setText(this.distanceTxt);
        holder.duration_textview.setText(this.durationTxt);
    }

    public DistanceViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new DistanceViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.distance_layout, parent, false));
    }
}

