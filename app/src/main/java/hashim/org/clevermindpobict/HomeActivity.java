package hashim.org.clevermindpobict;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.gigamole.infinitecycleviewpager.HorizontalInfiniteCycleViewPager;
import com.google.android.material.navigation.NavigationView;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;
import hashim.org.clevermindpobict.adapter.NewsPagerAdapter;
import hashim.org.clevermindpobict.adapter.TrainingCenterAdapter;
import hashim.org.clevermindpobict.model.Course;
import hashim.org.clevermindpobict.model.News;

public class HomeActivity extends AppCompatActivity {

    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle drawerToggle;
    private final List<News> newsList=new ArrayList<>();
    private HorizontalInfiniteCycleViewPager newsCycleViewPager;
    private RecyclerView recyclerViewCourse;
    private static final String newsURL="http://192.168.1.60/concept-master/fetch_latestNews.php";
    private static final String courseURL="http://192.168.1.60/concept-master/fetch_course.php";
    private static final String image_url="http://192.168.1.60/concept-master/uploads/";
    private final List<Course> courses=new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        newsCycleViewPager=findViewById(R.id.newsViewPager);
        recyclerViewCourse=findViewById(R.id.recyclerViewTraining);

        SharedPreferences logInPref = getSharedPreferences("LogIn", Context.MODE_PRIVATE);
        final SharedPreferences.Editor editor= logInPref.edit();

        RequestQueue queue= Volley.newRequestQueue(getApplicationContext());

        final JsonArrayRequest request=new JsonArrayRequest(newsURL, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                JSONObject object;
                for (int index=0;index<response.length();index++){
                    try {
                        object=response.getJSONObject(index);
                        News news=new News();
                        news.setNewsTitle(object.getString("title"));
                        news.setNewsSubTitle(object.getString("subtitle"));
                        news.setNewsDescription(object.getString("description"));
                        news.setNewsImage(image_url+object.getString("image"));
                        newsList.add(news);
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(getApplicationContext(),e.toString(),Toast.LENGTH_LONG).show();
                    }
                }
                setUpRecycler(newsList);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(),error.toString(),Toast.LENGTH_LONG).show();
            }
        });

        queue.add(request);


        //RecyclerView for Training Center


        RequestQueue queueCourse=Volley.newRequestQueue(getApplicationContext());
        JsonArrayRequest requestCourse=new JsonArrayRequest(courseURL, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                JSONObject object;
                for (int i=0;i<response.length();i++){
                    try {
                        object=response.getJSONObject(i);
                        Course course=new Course();
                        course.setId(object.getString("id"));
                        course.setCourseName(object.getString("name"));
                        course.setCourseDate(object.getString("day"));
                        course.setCourseTime(object.getString("tr_time"));
                        course.setCourseImage(image_url+object.getString("image"));
                        courses.add(course);

                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(getApplicationContext(),e.toString(),Toast.LENGTH_LONG).show();
                    }
                }
                setUpTraining(courses);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(),error.toString(),Toast.LENGTH_LONG).show();
            }
        });

        queueCourse.add(requestCourse);


        ImageView drawerMenuImage = findViewById(R.id.drawerMenu);

        drawerLayout=findViewById(R.id.home_Screen);
        drawerToggle=new ActionBarDrawerToggle(this,drawerLayout,R.string.open,R.string.close);

        drawerLayout.addDrawerListener(drawerToggle);

        drawerToggle.syncState();

        NavigationView viewNav = findViewById(R.id.navView);
        viewNav.setItemIconTintList(null);

        View headerView= viewNav.getHeaderView(0);

        LinearLayout linearHeader = headerView.findViewById(R.id.linearHeader);
        linearHeader.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawerLayout.closeDrawer(GravityCompat.START);
            }
        });

        drawerMenuImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });
        viewNav.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                int id=menuItem.getItemId();

                switch (id){
                    case R.id.profileScreen:
                        Intent profileIntent=new Intent(HomeActivity.this,Profile.class);
                        startActivity(profileIntent);
                        break;
                    case R.id.attendedScreen:
                        Intent AttendedIntent=new Intent(HomeActivity.this,AttendedActivity.class);
                        startActivity(AttendedIntent);
                        break;
                    case R.id.latestEventScreen:
                        Intent eventsIntent=new Intent(HomeActivity.this,LatestEvent.class);
                        startActivity(eventsIntent);
                        break;
                    case R.id.jobsScreen:
                        Intent JobsIntent=new Intent(HomeActivity.this,JobsActivity.class);
                        startActivity(JobsIntent);
                        break;
                    case R.id.settingsScreen:
                        Intent settingsIntent=new Intent(HomeActivity.this,Settings.class);
                        startActivity(settingsIntent);
                        break;
                    case R.id.paymentScreen:
                        Intent paymentIntent=new Intent(HomeActivity.this,Payment.class);
                        startActivity(paymentIntent);
                        break;
                    case R.id.privacyPolicyScreen:
                        Intent privacyIntent=new Intent(HomeActivity.this,PrivacyPolicy.class);
                        startActivity(privacyIntent);
                        break;

                    case R.id.logOut:
                        Toast.makeText(getApplicationContext(),"Loging Out",Toast.LENGTH_LONG).show();
                        editor.clear();
                        editor.apply();
                        Intent intent=new Intent(HomeActivity.this,LogInActivity.class);
                        startActivity(intent);
                        HomeActivity.this.finish();
                        break;
                        default:
                            return true;
                }

                return true;
            }
        });
    }
    private void setUpRecycler(List<News> list){
        NewsPagerAdapter pagerAdapter = new NewsPagerAdapter(list, getApplicationContext());
        newsCycleViewPager.setAdapter(pagerAdapter);
    }

    private void setUpTraining(List<Course> list){
        recyclerViewCourse.setAdapter(new TrainingCenterAdapter(list,getApplicationContext()));
        recyclerViewCourse.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (drawerToggle.onOptionsItemSelected(item))
            return true;
        return super.onOptionsItemSelected(item);
    }

}
