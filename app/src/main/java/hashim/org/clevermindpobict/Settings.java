package hashim.org.clevermindpobict;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.regex.Pattern;

public class Settings extends AppCompatActivity {

    private EditText oldPassword,newPassword,rePassword,feedBack;
    private String oldPass,newPass,rePass,savedUserName,savedPassword,feedText,result,rateValues;
    private SharedPreferences preferencesUser;
    private RatingBar bar;
    private static final String password_url="http://192.168.1.60/concept-master/edit_password.php";
    private static final String feedback_url="http://192.168.1.60/concept-master/send_feedback.php";
    private static final String rating_url="http://192.168.1.60/concept-master/rate.php";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        oldPassword=findViewById(R.id.oldPassWord);
        newPassword=findViewById(R.id.newPassWord);
        rePassword=findViewById(R.id.redPassWord);
        feedBack=findViewById(R.id.feedBack);
        Button changePass = findViewById(R.id.changePassword);
        Button submitFeed = findViewById(R.id.submitFeed);
        ImageView backArrow = findViewById(R.id.settingBack);
        bar=findViewById(R.id.ratingBar);

        bar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {

                rateValues=String.valueOf(rating);

                    AlertDialog.Builder builder=new AlertDialog.Builder(Settings.this);
                    builder.setMessage(R.string.rate_warn);
                    builder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            try {
                                sendRate(rateValues);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    });
                    builder.setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            //DO NOTHING
                        }
                    });
                    AlertDialog dialog=builder.create();
                    dialog.show();
            }
        });


        backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Settings.this.finish();
            }
        });

        preferencesUser=getSharedPreferences("LogIn",MODE_PRIVATE);
        savedUserName=preferencesUser.getString("username","empty");
        savedPassword=preferencesUser.getString("password","empty");

        submitFeed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                feedText=feedBack.getText().toString().trim();
                if (feedText.isEmpty()){
                    Toast.makeText(getApplicationContext(),"Please Insert Value in feedback",Toast.LENGTH_SHORT).show();
                    feedBack.setError("Please Fill The Data");
                }
                else{
                    submitFeedBack(feedText, savedUserName);
                    feedBack.setText(null);
                }
            }
        });

        changePass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                oldPass=oldPassword.getText().toString().trim();
                newPass=newPassword.getText().toString().trim();
                rePass=rePassword.getText().toString().trim();
                if (Validation()){
                    changePassWord();
                    oldPassword.setText(null);
                    newPassword.setText(null);
                    rePassword.setText(null);
                }
                else
                    Toast.makeText(getApplicationContext(),"Password must contain letter and character & at least 8 digits",Toast.LENGTH_LONG).show();
            }
        });
    }

    private void sendRate(String rateValue) throws JSONException {
        RequestQueue queueRate=Volley.newRequestQueue(getApplicationContext());
        JSONObject object=new JSONObject();
        object.put("userName",savedUserName);
        object.put("rate",rateValue);

        JsonObjectRequest request=new JsonObjectRequest(Request.Method.POST, rating_url, object, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    String result=response.getString("result");
                    Toast.makeText(getApplicationContext(),result,Toast.LENGTH_LONG).show();
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

        queueRate.add(request);
    }

    private void submitFeedBack(String desc,String userName){

        RequestQueue queue=Volley.newRequestQueue(getApplicationContext());
        final JSONObject paramsObject=new JSONObject();
        try {
            paramsObject.put("feed",desc);
            paramsObject.put("username",userName);
        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(getApplicationContext(),e.toString(),Toast.LENGTH_LONG).show();
        }

        JsonObjectRequest request=new JsonObjectRequest(Request.Method.POST, feedback_url, paramsObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    result=response.getString("result");
                    Toast.makeText(getApplicationContext(),result,Toast.LENGTH_LONG).show();
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

    private void changePassWord(){
        final SharedPreferences.Editor editor=preferencesUser.edit();

        RequestQueue queue= Volley.newRequestQueue(getApplicationContext());
        final JSONObject params=new JSONObject();
        try {
            params.put("userName",savedUserName);
            params.put("password",oldPass);
            params.put("newPassword",newPass);
        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(getApplicationContext(),e.toString(),Toast.LENGTH_SHORT).show();
        }

        JsonObjectRequest request=new JsonObjectRequest(Request.Method.POST, password_url, params, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    String result=response.getString("result");
                    Toast.makeText(getApplicationContext(),result,Toast.LENGTH_SHORT).show();
                    editor.putString("password",params.getString("newPassword"));
                    editor.apply();
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

    private boolean Validation(){
        boolean state=true;
        boolean passWordState=isValidPassWord(newPass);

        if (newPass.isEmpty() || !passWordState){
            newPassword.setError("Please input correct password");
            state=false;
        }
        if (oldPass.isEmpty() || !oldPass.equals(savedPassword)){
            oldPassword.setError("Please Input old password");
            state=false;
        }
        if (rePass.isEmpty()){
            rePassword.setError("please re enter password");
            state=false;
        }
        if (!newPass.equals(rePass)){
            Toast.makeText(getApplicationContext(),"The re-entered password not correct",Toast.LENGTH_LONG).show();
            state=false;
        }
        return state;
    }

    private boolean isValidPassWord(String passwords){
        Pattern PASSWORD_PATTERN=Pattern.compile("^(?=.*[0-9])(?=.*[A-Z])(?=.*[@#%^&+=!*])(?=\\S+$).{8,16}$");
        return PASSWORD_PATTERN.matcher(passwords).matches();
    }


}
