package dk.boonga.homesecurityp3;

import android.content.Context;
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
    List<sensor> mData;
    private static final String TAG = "adapter_sensor_cards";

    public AdapterSensors(Context mContext, List<sensor> mData) {
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
        //holder.sensor_id.setText(mData.get(position).getmTitle());

        // ------------------------------ on click for adapter ------------------------------
        holder.lock_id.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("aaa", "Sensor clicked:" + mData.get(position).getmTitle());

                AppCompatActivity activity = (AppCompatActivity) v.getContext();
                Fragment myFragment = new fragment_sensor();
                activity.getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container_main, myFragment).addToBackStack(null).commit();

            }
        });
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public class myViewHolder extends RecyclerView.ViewHolder {

        ImageView sensor_id;
        ImageView lock_id;

        public myViewHolder(View itemView) {
            super(itemView);
            sensor_id = itemView.findViewById(R.id.card_sensor_id);
            lock_id = itemView.findViewById(R.id.card_lock_id);

        }
    }
}
