package hashim.org.clevermindpobict;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import hashim.org.clevermindpobict.adapter.NewsAdapter;
import hashim.org.clevermindpobict.model.News;

public class LatestNewsActivity extends AppCompatActivity {

    private final List<News> newsList=new ArrayList<>();
    private static final String newsURL="http://192.168.1.60/concept-master/fetch_latestNews.php";
    private static final String image_url="http://192.168.1.60/concept-master/uploads/";
    private RecyclerView newsRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_latest_news);

        newsRecyclerView=findViewById(R.id.recyclerViewNews);

        RequestQueue queue= Volley.newRequestQueue(getApplicationContext());

        JsonArrayRequest arrayRequest=new JsonArrayRequest(newsURL, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                JSONObject object;
                for (int i=0;i<response.length();i++){
                    try {
                        object=response.getJSONObject(i);
                        News news=new News();
                        news.setNewsTitle(object.getString("title"));
                        news.setNewsSubTitle(object.getString("subtitle"));
                        news.setNewsDescription(object.getString("description"));
                        news.setNewsImage(image_url+object.getString("image"));
                        newsList.add(news);
                    } catch (JSONException e) {
                        Toast.makeText(getApplicationContext(),e.toString(),Toast.LENGTH_LONG).show();
                        e.printStackTrace();
                    }
                }
                setUpRecyclerView(newsList);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(),error.toString(),Toast.LENGTH_LONG).show();
            }
        });

        queue.add(arrayRequest);


    }
    private void setUpRecyclerView(List<News> list){
        NewsAdapter adapter=new NewsAdapter(list,getApplicationContext());
        newsRecyclerView.setAdapter(adapter);
        newsRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

    }
}
