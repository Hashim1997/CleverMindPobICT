package hashim.org.clevermindpobict;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Objects;

import hashim.org.clevermindpobict.model.Course;

public class Courses extends AppCompatActivity {

    private TextView courseTitle,courseDate,courseDesc;
    private ImageView courseImage;
    private Button attendCourseBtn;
    private String id;
    private String statues;
    private RadioButton publicRadio,privateRadio;
    private RequestOptions option;
    private static final String courseURL="http://192.168.1.60/concept-master/view_course.php";
    private static final String attendCourseURL="http://192.168.1.60/concept-master/buy_Course.php";
    private static final String image_url="http://192.168.1.60/concept-master/uploads/";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_courses);

        courseTitle=findViewById(R.id.Title);
        courseDate=findViewById(R.id.dateOfCourse);
        courseDesc=findViewById(R.id.courseDescInfo);
        courseImage=findViewById(R.id.courseImageLogo);
        attendCourseBtn=findViewById(R.id.attendBtn);

        SharedPreferences profilePref = getSharedPreferences("LogIn", MODE_PRIVATE);
        final String userName = profilePref.getString("username", "empty");

        option=new RequestOptions().placeholder(R.drawable.loading_image).error(R.drawable.error_load);

        Bundle bundle=getIntent().getExtras();

        id= Objects.requireNonNull(bundle).getString("id");

        attendCourseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (statues!=null){
                    AlertDialog.Builder builder=new AlertDialog.Builder(Courses.this);
                    builder.setMessage("Are You Sure you want to enroll to this course ?");
                    builder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            attendCourse(id,userName);
                        }
                    });
                    builder.setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });
                    AlertDialog dialog=builder.create();
                    dialog.show();
                }
                else
                    Toast.makeText(getApplicationContext(),"Please Select Course Type public or private",Toast.LENGTH_SHORT).show();
            }
        });

        fetchCourseData();
    }

    private void attendCourse(String id,String userName){
        RequestQueue queue=Volley.newRequestQueue(getApplicationContext());

        JSONObject object=new JSONObject();
        try {
            object.put("userName",userName);
            object.put("courseID",id);
            object.put("statues",statues);
        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(getApplicationContext(),e.toString(),Toast.LENGTH_LONG).show();
        }

        final JsonObjectRequest request=new JsonObjectRequest(Request.Method.POST, attendCourseURL, object, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    String result=response.getString("result");
                    Toast.makeText(getApplicationContext(),result,Toast.LENGTH_SHORT).show();
                } catch (JSONException e) {
                    e.printStackTrace();
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

    public void onRadioButtonClicked(View view){
        Boolean checked=((RadioButton) view).isChecked();
        switch (view.getId()){
            case R.id.publicRadio:
                if (checked)
                    statues="Public";
                break;
            case R.id.privateRadio:
                if (checked)
                    statues="Private";
                break;
        }
    }

    private final Course course=new Course();

    private void fetchCourseData(){


        RequestQueue queue= Volley.newRequestQueue(getApplicationContext());
        JSONObject params=new JSONObject();
        try {
            params.put("id",id);
        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(getApplicationContext(),e.toString(),Toast.LENGTH_SHORT).show();
        }


        JsonObjectRequest request=new JsonObjectRequest(Request.Method.POST, courseURL, params, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    String state=response.getString("state");
                    String result=response.getString("result");

                    if (state.equals("1")) {
                        JSONArray jsonArray = response.getJSONArray("data");

                        for (int index = 0; index < jsonArray.length(); index++) {
                            JSONObject object = jsonArray.getJSONObject(index);
                            course.setCourseName(object.getString("name"));
                            course.setCourseImage(image_url+object.getString("image"));
                            course.setCourseTime(object.getString("tr_time"));
                            course.setCourseDate(object.getString("day"));
                            course.setCourseDesc(object.getString("description"));
                        }
                        courseTitle.setText(course.getCourseName());
                        courseDate.setText(course.getCourseDate()+"\n"+course.getCourseTime());
                        courseDesc.setText(course.getCourseDesc());
                        Glide.with(getApplicationContext()).load(course.getCourseImage()).apply(option).into(courseImage);
                    }
                    else{
                        Toast.makeText(getApplicationContext(),result,Toast.LENGTH_LONG).show();
                    }
                }
                catch (JSONException e){
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(),e.toString(),Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(),error.toString(),Toast.LENGTH_SHORT).show();
            }
        });

        queue.add(request);
    }
}
