package hashim.org.clevermindpobict.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.List;

import hashim.org.clevermindpobict.Courses;
import hashim.org.clevermindpobict.R;
import hashim.org.clevermindpobict.model.Course;

public class TrainingCenterAdapter extends RecyclerView.Adapter<TrainingCenterAdapter.ViewHolder> {

    private final List<Course> courseList;
    private final Context context;
    private final RequestOptions option;

    public TrainingCenterAdapter(List<Course> courseList, Context context) {
        this.courseList = courseList;
        this.context = context;
        option=new RequestOptions().placeholder(R.drawable.loading_image).error(R.drawable.error_load);
    }

    @NonNull
    @Override
    public TrainingCenterAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemViewLayout= LayoutInflater.from(parent.getContext()).inflate(R.layout.item_training_center,null);
        return new ViewHolder(itemViewLayout);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final Course course=courseList.get(position);
        holder.courseName.setText(course.getCourseName());
        holder.courseDate.setText(course.getCourseDate()+"\n"+course.getCourseTime());
        Glide.with(context).load(course.getCourseImage()).apply(option).into(holder.courseImage);
        holder.viewBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentCourse=new Intent(context, Courses.class);
                intentCourse.putExtra("id",course.getId());
                intentCourse.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intentCourse);
            }
        });
    }

    @Override
    public int getItemCount() {
        return courseList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        final TextView courseName;
        final TextView courseDate;
        final ImageView courseImage;
        final ImageView viewBtn;

        ViewHolder(View itemLayoutView){
            super(itemLayoutView);
            courseName=itemLayoutView.findViewById(R.id.courseNameTrain);
            courseDate=itemLayoutView.findViewById(R.id.courseDateTrain);
            courseImage=itemLayoutView.findViewById(R.id.courseImageTrain);
            viewBtn=itemLayoutView.findViewById(R.id.viewCourse);
        }
    }
}
