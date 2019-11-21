package dk.boonga.homesecurityp3;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class AdapterRooms extends RecyclerView.Adapter<AdapterRooms.myViewHolder> {

    Context mContext;
    List<Room> mData;
    private static final String TAG = "adapter_event_cards";

    public AdapterRooms(Context mContext, List<Room> mData) {
        this.mContext = mContext;
        this.mData = mData;
    }

    @Override
    public myViewHolder onCreateViewHolder(ViewGroup parent, int i) {

        LayoutInflater inflater = LayoutInflater.from(mContext);
        View v = inflater.inflate(R.layout.card_room, parent, false);

        return new myViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final myViewHolder holder, final int position) {
        holder.room_id.setText(mData.get(position).getmTitle());

        // ------------------------------ on click for adapter ------------------------------
        holder.house_id.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppCompatActivity activity = (AppCompatActivity) v.getContext();
                Fragment myFragment = new SensorsFragment();
                //pack roomid to use later in other fragments
                Bundle bundle = new Bundle();
                bundle.putString("roomID", mData.get(position).getmTitle());
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

        TextView room_id;
        ImageView house_id;

        public myViewHolder(View itemView) {
            super(itemView);
            room_id = itemView.findViewById(R.id.card_room_text_id);
            house_id = itemView.findViewById(R.id.card_house_id);

        }
    }
}
