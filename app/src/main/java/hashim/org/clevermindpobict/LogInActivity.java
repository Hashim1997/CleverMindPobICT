package hashim.org.clevermindpobict;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
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

public class LogInActivity extends AppCompatActivity {

    private EditText userName,passWord;
    private ImageView logInBtn;
    private String userNameText,passWordText;
    private ProgressBar logInLoading;
    private static final String login_url="http://192.168.1.60/concept-master/user_login.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);

        logInBtn=findViewById(R.id.logInBtn);
        logInLoading=findViewById(R.id.loginLoading);

        logInBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                logInClick();
            }
        });
    }

    public void signUpClick(View view){
        Intent signUpIntent=new Intent(LogInActivity.this,SignUp.class);
        startActivity(signUpIntent);
    }


    private void logInClick(){


        userName=findViewById(R.id.userNameLog);
        passWord=findViewById(R.id.passWordLog);

        userNameText=userName.getText().toString();
        passWordText=passWord.getText().toString();

        SharedPreferences logInPref = getSharedPreferences("LogIn", Context.MODE_PRIVATE);
        final SharedPreferences.Editor editor= logInPref.edit();

        if (validation()){
            logInLoading.setVisibility(View.VISIBLE);
            logInBtn.setVisibility(View.GONE);
            RequestQueue queue= Volley.newRequestQueue(getApplicationContext());
            JSONObject params=new JSONObject();
            try {
                params.put("userName",userNameText);
                params.put("password",passWordText);
            } catch (JSONException e) {
                e.printStackTrace();
                Toast.makeText(getApplicationContext(),e.toString(),Toast.LENGTH_SHORT).show();
            }

            JsonObjectRequest request=new JsonObjectRequest(Request.Method.POST, login_url, params, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    try {
                        String state=response.getString("state");
                        if (state.equals("1")) {
                            JSONArray jsonArray = response.getJSONArray("data");
                            String name = "";
                            String username = "";
                            String passwords="";
                            for (int index = 0; index < jsonArray.length(); index++) {
                                JSONObject object = jsonArray.getJSONObject(index);
                                name = object.getString("name");
                                username = object.getString("user_name");
                                passwords=object.getString("password");
                            }
                            editor.putString("username",username);
                            editor.putString("password",passwords);
                            editor.apply();
                            Toast.makeText(getApplicationContext(), "Welcome " + username+"\n"+name, Toast.LENGTH_LONG).show();
                            logInLoading.setVisibility(View.GONE);
                            logInBtn.setVisibility(View.VISIBLE);
                            Intent intent=new Intent(LogInActivity.this,HomeActivity.class);
                            startActivity(intent);
                            LogInActivity.this.finish();
                        }
                        else{
                            Toast.makeText(getApplicationContext(),"Wrong Data",Toast.LENGTH_LONG).show();
                            logInLoading.setVisibility(View.GONE);
                            logInBtn.setVisibility(View.VISIBLE);
                        }
                    }
                    catch (JSONException e){
                        e.printStackTrace();
                        logInLoading.setVisibility(View.GONE);
                        logInBtn.setVisibility(View.VISIBLE);
                        Toast.makeText(getApplicationContext(),e.toString(),Toast.LENGTH_SHORT).show();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    logInLoading.setVisibility(View.GONE);
                    logInBtn.setVisibility(View.VISIBLE);
                    Toast.makeText(getApplicationContext(),error.toString(),Toast.LENGTH_SHORT).show();
                }
            });
            queue.add(request);
        }
        else
            Toast.makeText(getApplicationContext(),"Please Insert Correct info",Toast.LENGTH_SHORT).show();

    }
    private boolean validation(){
        boolean state=true;
        if (userNameText.isEmpty()){
            userName.setError("Please input username");
            state=false;
        }
        if (passWordText.isEmpty()){
            passWord.setError("Please input password");
        }
        return state;
    }

}
