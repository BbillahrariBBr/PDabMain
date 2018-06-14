package test.com.potherdabai.potherdabimain;


import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView.Adapter;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;
import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.heatmaps.WeightedLatLng;
import java.text.DecimalFormat;
import java.util.ArrayList;

public class CountryAdapter extends Adapter<CountryAdapter.CountryViewHolder> {
    Context f22c;
    ArrayList<SingleRow> countryList = new ArrayList();
    String source_lat;
    String source_lng;

    static class CountryViewHolder extends ViewHolder {
        TextView address_txt;
        TextView distance_txt;
        TextView name_txt;
        View f21v;

        public CountryViewHolder(View itemView) {
            super(itemView);
            this.f21v = itemView;
            this.name_txt = (TextView) itemView.findViewById(R.id.item_title);
            this.distance_txt = (TextView) itemView.findViewById(R.id.item_distance);
            this.address_txt = (TextView) itemView.findViewById(R.id.item_address);
        }
    }

    public CountryAdapter(Context c, ArrayList<SingleRow> countryList, ArrayList<String> arrayList, ArrayList<String> arrayList2, String source_lat, String source_lng) {
        this.f22c = c;
        this.countryList = countryList;
        this.source_lat = source_lat;
        this.source_lng = source_lng;

    }

    public CountryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new CountryViewHolder(LayoutInflater.from(parent.getContext()).
                inflate(R.layout.card_layout, parent, false));
    }

    public void onBindViewHolder(CountryViewHolder holder, int position) {
        final SingleRow con = (SingleRow) this.countryList.get(position);
        holder.name_txt.setText(con.name);
        holder.address_txt.setText(con.vicinity);
        String lat_desti = con.latitude;
        String lng_desti = con.longitude;
        final double source_lat_double = Double.parseDouble(this.source_lat);
        final double source_lng_double = Double.parseDouble(this.source_lng);
        double desti_lat_double = Double.parseDouble(lat_desti);
        double desti_lng_double = Double.parseDouble(lng_desti);
        holder.distance_txt.setText(new DecimalFormat("#.##")
                .format(CalculationByDistance(new LatLng(source_lat_double,
                        source_lng_double), new LatLng(desti_lat_double, desti_lng_double))) + " KM");
        holder.f21v.setOnClickListener(new OnClickListener() {



            public void onClick(View v) {

                Intent i = new Intent(CountryAdapter.this.f22c, MapDetailActivity.class);
                LatLng latLngDesti = new LatLng(Double.parseDouble(con.latitude), Double.parseDouble(con.longitude));
                LatLng latLngSource = new LatLng(source_lat_double, source_lng_double);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                i.putExtra("latitude_destination", latLngDesti);
                i.putExtra("longitude_source", latLngSource);
                CountryAdapter.this.f22c.startActivity(i);
            }
        });
    }

    double CalculationByDistance(LatLng StartP, LatLng EndP) {
        double lat1 = StartP.latitude;
        double lat2 = EndP.latitude;
        double lon1 = StartP.longitude;
        double lon2 = EndP.longitude;
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);
        double valueResult = ((double) 6371) * (2.0d * Math.asin(Math.sqrt((Math.sin(dLat / 2.0d) * Math.sin(dLat / 2.0d)) + (((Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))) * Math.sin(dLon / 2.0d)) * Math.sin(dLon / 2.0d)))));
        double km = valueResult / WeightedLatLng.DEFAULT_INTENSITY;
        DecimalFormat decimalFormat = new DecimalFormat("####");
        int kmInDec = Integer.valueOf(decimalFormat.format(km)).intValue();
        double meter = valueResult % 1000.0d;
        int meterInDec = Integer.valueOf(decimalFormat.format(meter)).intValue();
        return meter;
    }

    public int getItemCount() {
        return this.countryList.size();
    }
}
