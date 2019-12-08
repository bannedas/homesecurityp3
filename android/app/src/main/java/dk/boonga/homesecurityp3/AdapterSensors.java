package dk.boonga.homesecurityp3;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.List;

public class AdapterSensors extends RecyclerView.Adapter<AdapterSensors.myViewHolder> {

    Context mContext;
    List<Sensor> mData;
    private static final String TAG = "adapter_sensor_cards";

    public AdapterSensors(Context mContext, List<Sensor> mData) {
        this.mContext = mContext;
        this.mData = mData;
    }

    @Override
    public myViewHolder onCreateViewHolder(ViewGroup parent, int i) {

        LayoutInflater inflater = LayoutInflater.from(mContext);
        View v = inflater.inflate(R.layout.card_sensor, parent, false);

        return new myViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final myViewHolder holder, final int position) {
        holder.sensor_name.setText(mData.get(position).getmTitle());

        // ------------------------------ on click for adapter ------------------------------
        holder.lock_id.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppCompatActivity activity = (AppCompatActivity) v.getContext();
                Fragment myFragment = new UniqueSensorFragment();
                Bundle bundle = new Bundle();
                bundle.putString("sensorID", mData.get(position).getmTitle());
                myFragment.setArguments(bundle);

                activity.getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container_main, myFragment).addToBackStack(null).commit();

            }
        });
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public class myViewHolder extends RecyclerView.ViewHolder {

        TextView sensor_name;
        ImageView sensor_id;
        ImageView lock_id;

        public myViewHolder(View itemView) {
            super(itemView);
            sensor_name = itemView.findViewById(R.id.card_sensor_text_id);
            sensor_id = itemView.findViewById(R.id.card_sensor_id);
            lock_id = itemView.findViewById(R.id.card_lock_id);

        }
    }
}
