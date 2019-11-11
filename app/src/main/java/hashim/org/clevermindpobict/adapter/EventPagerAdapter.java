package hashim.org.clevermindpobict.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import java.util.List;

import hashim.org.clevermindpobict.R;
import hashim.org.clevermindpobict.model.Events;

public class EventPagerAdapter extends PagerAdapter {

    private final List<Events> eventsList;
    private final Context context;

    public EventPagerAdapter(List<Events> eventsList, Context context) {
        this.eventsList = eventsList;
        this.context = context;
    }

    @Override
    public int getCount() {
        return eventsList.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view.equals(object);
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }

    @SuppressLint("SetTextI18n")
    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        final Events events=eventsList.get(position);
        View view= LayoutInflater.from(container.getContext()).inflate(R.layout.item_latest_event,null);
        TextView eventName=view.findViewById(R.id.eventName);
        TextView eventDate=view.findViewById(R.id.eventDate);
        TextView eventTime=view.findViewById(R.id.eventTime);

        eventName.setText(events.getEventName());
        eventDate.setText(events.getEventDay() +"\n"+ events.getEventDate());
        eventTime.setText(events.getEventTime());
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(context,"This is id "+events.getId(),Toast.LENGTH_SHORT).show();
            }
        });

        container.addView(view);
        return view;
    }
}
