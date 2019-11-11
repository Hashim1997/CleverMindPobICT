package hashim.org.clevermindpobict.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.util.Base64;
import android.util.Log;
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

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Objects;

import hashim.org.clevermindpobict.FilePath;
import hashim.org.clevermindpobict.R;
import hashim.org.clevermindpobict.model.Jobs;

import static android.content.Context.MODE_PRIVATE;


public class JobsAdapter extends RecyclerView.Adapter<JobsAdapter.ViewHolder> {

    private static Context mContext;
    private final Context context;
    private final List<Jobs> jobsList;
    private final RequestOptions option;
    private final Context app;
    private static String fi;
    private static String id;

    private static final String pdf_upload_url="http://192.168.1.60/concept-master/pdf_upload.php";

    public JobsAdapter(Context context, List<Jobs> jobsList,Context app) {
        this.context = context;
        this.jobsList = jobsList;
        this.app=app;
        this.mContext=context;
        option=new RequestOptions().placeholder(R.drawable.loading_image).error(R.drawable.error_load);
    }

    @NonNull
    @Override
    public JobsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemViewLayout=LayoutInflater.from(parent.getContext()).inflate(R.layout.item_card_jobs,null);
        return new ViewHolder(itemViewLayout);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final Jobs jobs=jobsList.get(position);
        holder.experiencePosition.setText(jobs.getExperience());
        holder.textPosition.setText(jobs.getPosition());
        Glide.with(context).load(jobs.getPositionImage()).apply(option).into(holder.positionImage);
        holder.uploadCV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                choosePdf();
                id=jobs.getId();
            }
        });
    }

    public static void onActivityResult(int requestCode, int resultCode, Intent data) throws IOException, JSONException {
        if (requestCode==1 && resultCode==-1 && data!=null && data.getData()!=null){
            Uri filePath=data.getData();

            String filepath= FilePath.getPath(mContext,filePath);
            if (filePath!=null){
                fi=convertFileToByteArray(new File(filepath));
                fileUpload(fi,id);
            }
            else
                Toast.makeText(mContext,"Please Select File",Toast.LENGTH_LONG).show();
        }
    }

    private static String convertFileToByteArray(File file) throws IOException {
        byte[] byteArray=null;
        InputStream inputStream=new FileInputStream(file);
        ByteArrayOutputStream bos=new ByteArrayOutputStream();
        byte[] b=new byte[1024*11];
        int bytesRead=0;
        while ((bytesRead=inputStream.read(b))!=-1){
            bos.write(b,0,bytesRead);
        }

        byteArray=bos.toByteArray();
        Log.e("Byte array",">"+byteArray);
        return Base64.encodeToString(byteArray,Base64.NO_WRAP);
    }


    private void choosePdf(){
        Intent intent=new Intent();
        intent.setType("application/pdf");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        ((Activity) app).startActivityForResult(Intent.createChooser(intent,"Select Profile Image"),1);;
    }

    private static void fileUpload(final String file, final String id)  {
        SharedPreferences profilePref = mContext.getSharedPreferences("LogIn", MODE_PRIVATE);
        String userName = profilePref.getString("username", "empty");

        RequestQueue queue= Volley.newRequestQueue(mContext);

        JSONObject object=new JSONObject();
        try {
            object.put("pdf",file);
            object.put("id",id);
            object.put("userID",userName);
        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(mContext,"jsonObj "+e.toString(),Toast.LENGTH_LONG).show();
        }

        JsonObjectRequest request=new JsonObjectRequest(Request.Method.POST, pdf_upload_url, object, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    String success=response.getString("success");
                    if (success.equals("1")){
                        Toast.makeText(mContext,"Thank You Uploaded Successful",Toast.LENGTH_SHORT).show();
                    }
                    else
                        Toast.makeText(mContext,"Error while uploading",Toast.LENGTH_SHORT).show();
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(mContext,"request "+e.toString(),Toast.LENGTH_LONG).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(mContext,"Error Listener "+error.toString(),Toast.LENGTH_LONG).show();
                Log.i("errorListX",error.toString());
            }
        });

        queue.add(request);
    }


    @Override
    public int getItemCount() {
        return jobsList.size();
    }



    public static class ViewHolder extends RecyclerView.ViewHolder{
        final TextView textPosition;
        final TextView experiencePosition;
        final TextView uploadCV;
        final ImageView positionImage;

        ViewHolder(View itemLayoutView){
            super(itemLayoutView);
            textPosition=itemLayoutView.findViewById(R.id.jobPosition);
            experiencePosition=itemLayoutView.findViewById(R.id.experience);
            positionImage=itemLayoutView.findViewById(R.id.positionImage);
            uploadCV=itemLayoutView.findViewById(R.id.uploadCV);
        }
    }
}
