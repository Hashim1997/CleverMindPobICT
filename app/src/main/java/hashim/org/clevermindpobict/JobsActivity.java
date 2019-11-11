package hashim.org.clevermindpobict;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.FileUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import hashim.org.clevermindpobict.adapter.JobsAdapter;
import hashim.org.clevermindpobict.model.Jobs;

public class JobsActivity extends AppCompatActivity {

    private RecyclerView viewJobs;
    private static final String image_url="http://192.168.1.60/concept-master/uploads/";
    private final List<Jobs> jobsList=new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jobs);

        ImageView backBtn = findViewById(R.id.backBtnJob);

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                JobsActivity.this.finish();
            }
        });


        RequestQueue queue= Volley.newRequestQueue(getApplicationContext());

        String job_url = "http://192.168.1.60/concept-master/fetch_job.php";
        JsonArrayRequest request=new JsonArrayRequest(job_url, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                JSONObject object;
                for (int i=0;i<response.length();i++){
                    try {
                        object=response.getJSONObject(i);
                        Jobs jobs=new Jobs();
                        jobs.setId(object.getString("id"));
                        jobs.setPosition(object.getString("name"));
                        jobs.setExperience(object.getString("experienc"));
                        jobs.setPositionImage(image_url+object.getString("image"));

                        jobsList.add(jobs);
                    } catch (JSONException e) {
                        Toast.makeText(getApplicationContext(),e.toString(),Toast.LENGTH_SHORT).show();
                        e.printStackTrace();
                    }
                }
                setUpRecycler(jobsList);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(),"Error "+error.toString(),Toast.LENGTH_LONG).show();
            }
        });


        queue.add(request);
        viewJobs=findViewById(R.id.jobsList);
    }
    private void setUpRecycler(List<Jobs> list){
        JobsAdapter jobsAdapter = new JobsAdapter(getApplicationContext(), list,JobsActivity.this);
        viewJobs.setAdapter(jobsAdapter);
        viewJobs.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            JobsAdapter.onActivityResult(requestCode, resultCode, data);
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(getApplicationContext(),e.toString(),Toast.LENGTH_LONG).show();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
