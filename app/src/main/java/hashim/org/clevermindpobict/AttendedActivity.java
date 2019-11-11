package hashim.org.clevermindpobict;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.text.HtmlCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import hashim.org.clevermindpobict.adapter.AttendedAdapter;
import hashim.org.clevermindpobict.model.Course;

public class AttendedActivity extends AppCompatActivity {

    private String savedUserName;
    private final List<Course> courseList=new ArrayList<>();
    private RecyclerView courseRecycler;
    private static final String image_url="http://192.168.1.60/concept-master/uploads/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attended);

        TextView titleAttended = findViewById(R.id.attendedTitle);
        ImageView backImage = findViewById(R.id.backBtnAttend);


        SharedPreferences profilePref = getSharedPreferences("LogIn", MODE_PRIVATE);
        savedUserName= profilePref.getString("username","empty");

        fetchCourse();

        backImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AttendedActivity.this.finish();
            }
        });

        String course = "<font color=\'#0000ff\'>COURSE</font>";
        String trainingSession = "<font color=\'#0000ff\'>TRAINING <p>SESSION</p></font>";

        String wholeTitle = "ATTENDED " + course + " AND " + trainingSession;
        titleAttended.setText(HtmlCompat.fromHtml(wholeTitle,HtmlCompat.FROM_HTML_MODE_LEGACY));


        courseRecycler = findViewById(R.id.recyclerViewAttended);


    }
    private void fetchCourse(){
        RequestQueue queue= Volley.newRequestQueue(getApplicationContext());

        JSONObject object=new JSONObject();
        try {
            object.put("userName",savedUserName);
        } catch (JSONException e){
            Toast.makeText(getApplicationContext(),e.toString(),Toast.LENGTH_LONG).show();
        }

        String attend_url = "http://192.168.1.60/concept-master/attended_course.php";
        JsonObjectRequest request=new JsonObjectRequest(Request.Method.POST, attend_url, object, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    String result = response.getString("result");
                    Toast.makeText(getApplicationContext(),result,Toast.LENGTH_SHORT).show();

                    if (result.equals("successful")){
                        JSONArray array=response.getJSONArray("data");

                        for (int i=0;i<array.length();i++){
                            JSONObject objectCourse=array.getJSONObject(i);
                            Course courseObj=new Course();
                            courseObj.setId(objectCourse.getString("id"));
                            courseObj.setCourseName(objectCourse.getString("name"));
                            courseObj.setCourseDate(objectCourse.getString("day"));
                            courseObj.setCourseTime(objectCourse.getString("tr_time"));
                            courseObj.setCourseImage(image_url+objectCourse.getString("image"));
                            courseObj.setCourseDesc(objectCourse.getString("description"));
                            courseList.add(courseObj);
                        }
                        setUpRecycler(courseList);
                    }
                    else
                        Toast.makeText(getApplicationContext(),response.getString("result"),Toast.LENGTH_LONG).show();
                }
                catch (JSONException e){
                    Toast.makeText(getApplicationContext(),e.toString(),Toast.LENGTH_LONG).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(),error.toString(),Toast.LENGTH_LONG).show();
            }
        });
        queue.add(request);
    }


    private void setUpRecycler(List<Course> courses){
        AttendedAdapter adapter=new AttendedAdapter(getApplicationContext(),courses,AttendedActivity.this);
        courseRecycler.setAdapter(adapter);
        courseRecycler.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
    }
}
