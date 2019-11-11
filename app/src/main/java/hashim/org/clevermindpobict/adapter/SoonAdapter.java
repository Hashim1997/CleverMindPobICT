package hashim.org.clevermindpobict.adapter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import hashim.org.clevermindpobict.R;
import hashim.org.clevermindpobict.model.Soon;

public class SoonAdapter extends RecyclerView.Adapter<SoonAdapter.Holder> {

    private final List<Soon> soonList;

    public SoonAdapter(List<Soon> soonList) {
        this.soonList = soonList;
    }

    @NonNull
    @Override
    public SoonAdapter.Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemViewLayout= LayoutInflater.from(parent.getContext()).inflate(R.layout.item_soon,null);
        return new Holder(itemViewLayout);
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {
        Soon soon=soonList.get(position);
        holder.titleView.setText(soon.getTitle());
        Log.i("nameSoon",soon.getTitle());
    }


    @Override
    public int getItemCount() {
        return soonList.size();
    }

    public static class Holder extends RecyclerView.ViewHolder{
        final TextView titleView;

        Holder(@NonNull View itemView) {
            super(itemView);
            titleView=itemView.findViewById(R.id.soonEventName);
        }
    }
}
