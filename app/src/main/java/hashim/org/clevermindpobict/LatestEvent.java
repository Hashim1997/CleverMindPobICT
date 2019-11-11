package hashim.org.clevermindpobict;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.gigamole.infinitecycleviewpager.HorizontalInfiniteCycleViewPager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import hashim.org.clevermindpobict.adapter.EventPagerAdapter;
import hashim.org.clevermindpobict.adapter.SoonAdapter;
import hashim.org.clevermindpobict.model.Events;
import hashim.org.clevermindpobict.model.Soon;

public class LatestEvent extends AppCompatActivity {

    private final List<Events> eventsList=new ArrayList<>();
    private final List<Soon> soonList=new ArrayList<>();
    private RecyclerView soonRecycler;
    private HorizontalInfiniteCycleViewPager eventsCycleViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_latest_event);

        eventsCycleViewPager=findViewById(R.id.eventViewPager);
        soonRecycler=findViewById(R.id.recyclerViewSoon);
        ImageView backArrow = findViewById(R.id.eventBack);

        backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LatestEvent.this.finish();
            }
        });

        fetchEvent();
        fetchSoon();
    }

    private void fetchEvent(){
        RequestQueue queue= Volley.newRequestQueue(getApplicationContext());

        String event_url = "http://192.168.1.60/concept-master/fetch_event.php";
        JsonArrayRequest arrayRequest=new JsonArrayRequest(event_url, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                JSONObject object;
                for (int i=0;i<response.length();i++){
                    try {
                        object=response.getJSONObject(i);
                        Events events=new Events();
                        events.setId(object.getString("id"));
                        events.setEventName(object.getString("head"));
                        events.setEventDay(object.getString("day"));
                        events.setEventDate(object.getString("on_date"));
                        events.setEventTime(object.getString("on_time"));
                        eventsList.add(events);
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(getApplicationContext(),e.toString(),Toast.LENGTH_LONG).show();
                    }
                }
                setUpEvent(eventsList);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(),error.toString(),Toast.LENGTH_LONG).show();
            }
        });
        queue.add(arrayRequest);
    }
    private void setUpEvent(List<Events> list){
        EventPagerAdapter pagerAdapter = new EventPagerAdapter(list, getApplicationContext());
        eventsCycleViewPager.setAdapter(pagerAdapter);
    }


    private void fetchSoon(){
        RequestQueue queue= Volley.newRequestQueue(getApplicationContext());

        String soon_url = "http://192.168.1.60/concept-master/fetch_soon.php";
        JsonArrayRequest arrayRequest=new JsonArrayRequest(soon_url, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                JSONObject object;
                for (int i=0;i<response.length();i++){
                    try {
                        object=response.getJSONObject(i);
                        Soon soon=new Soon();
                        soon.setIdSoon(object.getString("id"));
                        soon.setTitle(object.getString("head"));
                        soonList.add(soon);
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(getApplicationContext(),e.toString(),Toast.LENGTH_LONG).show();
                    }
                }
                setUpSoon(soonList);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(),error.toString(),Toast.LENGTH_LONG).show();
            }
        });
        queue.add(arrayRequest);
    }
    private void setUpSoon(List<Soon> list){
        SoonAdapter adapter = new SoonAdapter(list);
        soonRecycler.setAdapter(adapter);
        soonRecycler.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
    }
}
