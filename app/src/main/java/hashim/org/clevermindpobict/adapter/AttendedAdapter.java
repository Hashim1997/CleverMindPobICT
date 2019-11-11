package hashim.org.clevermindpobict.adapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;
import hashim.org.clevermindpobict.R;
import hashim.org.clevermindpobict.model.Course;

import static android.content.Context.MODE_PRIVATE;

public class AttendedAdapter extends RecyclerView.Adapter<AttendedAdapter.ViewHolderAttend> {

    private final Context context;
    private final List<Course> coursesList;
    private final RequestOptions option;
    private final Activity activity;


    public AttendedAdapter(Context context, List<Course> coursesList,Activity activity) {
        this.context = context;
        this.coursesList = coursesList;
        this.activity=activity;
        option=new RequestOptions().placeholder(R.drawable.loading_image).error(R.drawable.error_load);
    }

    @NonNull
    @Override
    public AttendedAdapter.ViewHolderAttend onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView= LayoutInflater.from(parent.getContext()).inflate(R.layout.item_card_attended,null);
        return new ViewHolderAttend(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderAttend holder, final int position) {
        final Course courses=coursesList.get(position);
        holder.textCourse.setText(courses.getCourseName());
        holder.courseDate.setText(courses.getCourseDate()+"\n"+courses.getCourseTime());
        Glide.with(context).load(courses.getCourseImage()).apply(option).into(holder.courseImage);
        holder.courseCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder=new AlertDialog.Builder(activity);
                builder.setMessage("Are you sure to quit course");
                builder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        deleteCourse(courses.getId());
                        removeItem(position);
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
        });
    }


    private void deleteCourse(String id){
        SharedPreferences profilePref = context.getSharedPreferences("LogIn", MODE_PRIVATE);
        String userName = profilePref.getString("username", "empty");
        RequestQueue queue= Volley.newRequestQueue(context);
        final JSONObject object=new JSONObject();
        try {
            object.put("courseID",id);
            object.put("username", userName);
        } catch (JSONException e){
            Toast.makeText(context,e.toString(),Toast.LENGTH_LONG).show();
        }

        String attend_url = "http://192.168.1.60/concept-master/delete_attended.php";
        JsonObjectRequest request=new JsonObjectRequest(Request.Method.POST, attend_url, object, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    String result = response.getString("result");

                        Toast.makeText(context, result, Toast.LENGTH_SHORT).show();

                } catch (JSONException e) {
                    Toast.makeText(context, e.toString(), Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(context,error.toString(),Toast.LENGTH_LONG).show();
            }
        });

        queue.add(request);
    }

    private void removeItem(int position){
        coursesList.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position,coursesList.size());
    }

    @Override
    public int getItemCount() {
        return coursesList.size();
    }

    public static class ViewHolderAttend extends RecyclerView.ViewHolder{
        final TextView textCourse;
        final TextView courseDate;
        final ImageView courseImage;
        final ImageView courseCancel;

        ViewHolderAttend(View itemLayoutView){
            super(itemLayoutView);
            textCourse=itemLayoutView.findViewById(R.id.courseName);
            courseDate=itemLayoutView.findViewById(R.id.courseDate);
            courseImage=itemLayoutView.findViewById(R.id.courseImage);
            courseCancel=itemLayoutView.findViewById(R.id.cancel_course);
        }
    }
}
