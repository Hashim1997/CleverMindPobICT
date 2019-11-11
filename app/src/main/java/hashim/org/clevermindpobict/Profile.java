package hashim.org.clevermindpobict;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
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

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.regex.Pattern;


public class Profile extends AppCompatActivity {

    private EditText fullName,userName,email,password,phoneNumber,dateOfBirth;
    private String fullNameText,userNameText,emailText,passwordText,phoneNumberText,dateOfBirthText,savedUserName,savedPassword;
    private TextView statues;
    private ProgressBar saveLoad;
    private ImageView saveButton;
    private ImageView profileImage;
    private SharedPreferences profilePref;
    private RequestOptions option;
    private final Calendar birthCalender=Calendar.getInstance();
    private static final String login_url="http://192.168.1.60/concept-master/user_login.php";
    private static final String profile_url="http://192.168.1.60/concept-master/edit_profile.php";
    private static final String image_url="http://192.168.1.60/concept-master/uploads/";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        fullName=findViewById(R.id.fullNameProfile);
        userName=findViewById(R.id.userNameProfile);
        email=findViewById(R.id.emailProfile);
        profileImage=findViewById(R.id.profileImage);
        password=findViewById(R.id.passWordProfile);
        phoneNumber=findViewById(R.id.phoneNoProfile);
        dateOfBirth=findViewById(R.id.birthDateProfile);
        saveButton=findViewById(R.id.saveBtnProfile);
        statues=findViewById(R.id.editStatues);
        saveLoad=findViewById(R.id.saveLoading);
        ImageView backArrow = findViewById(R.id.profileBack);

        backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Profile.this.finish();
            }
        });

        option=new RequestOptions().placeholder(R.drawable.loading_image).error(R.mipmap.ic_profile);
        profilePref=getSharedPreferences("LogIn",MODE_PRIVATE);
        savedUserName=profilePref.getString("username","empty");
        savedPassword=profilePref.getString("password","empty");

        retrieveProfileData();

        final DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                birthCalender.set(Calendar.YEAR,year);
                birthCalender.set(Calendar.MONTH,month);
                birthCalender.set(Calendar.DAY_OF_MONTH,day);
                updateLabel();
            }
        };

        dateOfBirth.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                DatePickerDialog dialog= new DatePickerDialog(Profile.this, dateSetListener, birthCalender
                        .get(Calendar.YEAR), birthCalender.get(Calendar.MONTH),
                        birthCalender.get(Calendar.DAY_OF_MONTH));
                dialog.getDatePicker().setMaxDate(System.currentTimeMillis());
                dialog.show();
            }
        });


        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (statues.getText().equals("EDIT")){
                    statues.setText("SAVE");
                    userName.setEnabled(true);
                    fullName.setEnabled(true);
                    dateOfBirth.setEnabled(true);
                    phoneNumber.setEnabled(true);
                    email.setEnabled(true);
                }
                else if (statues.getText().equals("SAVE")){
                    fullNameText=fullName.getText().toString().trim();
                    userNameText=userName.getText().toString().trim();
                    emailText=email.getText().toString().trim();
                    passwordText=password.getText().toString().trim();
                    phoneNumberText=phoneNumber.getText().toString().trim();
                    dateOfBirthText=dateOfBirth.getText().toString().trim();

                    if (validation()){
                        editProfileInfo();
                        statues.setText("EDIT");
                        userName.setEnabled(false);
                        fullName.setEnabled(false);
                        dateOfBirth.setEnabled(false);
                        phoneNumber.setEnabled(false);
                        email.setEnabled(false);
                    }
                    else {
                        Toast.makeText(getApplicationContext(),"Please Input Correct Data",Toast.LENGTH_LONG).show();
                    }
                }
            }
        });
    }
    private void updateLabel(){
        String format="dd/MM/yyyy";
        SimpleDateFormat DateFormat=new SimpleDateFormat(format, Locale.US);
        dateOfBirth.setText(DateFormat.format(birthCalender.getTime()));
    }

    private void editProfileInfo(){
        saveLoad.setVisibility(View.VISIBLE);
        saveButton.setVisibility(View.GONE);

        final SharedPreferences.Editor editor=profilePref.edit();

        RequestQueue editQueue= Volley.newRequestQueue(getApplicationContext());

        final JSONObject params=new JSONObject();
        try {
            params.put("fullName",fullNameText);
            params.put("userName",savedUserName);
            params.put("email",emailText);
            params.put("phoneNumber",phoneNumberText);
            params.put("password",savedPassword);
            params.put("dateOfBirth",dateOfBirthText);
            params.put("userNameNew",userNameText);
        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(getApplicationContext(),"this is "+e.toString(),Toast.LENGTH_SHORT).show();
        }
        JsonObjectRequest stringRequestEdit=new JsonObjectRequest(Request.Method.POST, profile_url, params, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    String result=response.getString("result");
                    Toast.makeText(getApplicationContext(),result,Toast.LENGTH_SHORT).show();
                    if (result.equals("data changed successfully")){
                        saveLoad.setVisibility(View.GONE);
                        saveButton.setVisibility(View.VISIBLE);
                        editor.putString("username",params.getString("userName"));
                        editor.apply();
                    }
                    else{
                        saveLoad.setVisibility(View.GONE);
                        saveButton.setVisibility(View.VISIBLE);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(),e.toString(),Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(),"Error while upload data"+error.toString(),Toast.LENGTH_SHORT).show();
                saveLoad.setVisibility(View.VISIBLE);
                saveButton.setVisibility(View.GONE);
            }
        });

        editQueue.add(stringRequestEdit);
    }

    private void retrieveProfileData(){

        RequestQueue queue= Volley.newRequestQueue(getApplicationContext());
        JSONObject params=new JSONObject();
        try {
            params.put("userName",savedUserName);
            params.put("password",savedPassword);
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
                        String emails="";
                        String phone="";
                        String birth="";
                        String image="";
                        for (int index = 0; index < jsonArray.length(); index++) {
                            JSONObject object = jsonArray.getJSONObject(index);
                            name = object.getString("name");
                            username = object.getString("user_name");
                            passwords=object.getString("password");
                            emails=object.getString("email");
                            phone=object.getString("phone");
                            birth=object.getString("birth");
                            image=object.getString("image");
                        }
                        userName.setText(username);
                        fullName.setText(name);
                        password.setText(passwords);
                        email.setText(emails);
                        phoneNumber.setText(phone);
                        dateOfBirth.setText(birth);
                        Glide.with(getApplicationContext()).load(image_url+image).apply(option).into(profileImage);

                    }
                    else{
                        Toast.makeText(getApplicationContext(),"Wrong Data",Toast.LENGTH_LONG).show();
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

    private boolean validation(){
        boolean state=true;
        boolean emailState= Patterns.EMAIL_ADDRESS.matcher(emailText).matches();
        boolean passWordState=isValidPassWord(passwordText);

        if (fullNameText.isEmpty()){
            fullName.setError("Please input name");
            state=false;
        }
        if (userNameText.isEmpty()){
            userName.setError("Please input username");
            state=false;
        }
        if (emailText.isEmpty() || !emailState){
            email.setError("Please input correct email");
            state=false;
        }
        if (passwordText.isEmpty() || !passWordState){
            password.setError("Please input correct password");
            Toast.makeText(getApplicationContext(),"Password must contain letter and character & at least 8 digits",Toast.LENGTH_LONG).show();
            state=false;
        }
        if (phoneNumberText.isEmpty()){
            phoneNumber.setError("Please input phone num");
            state=false;
        }
        if (dateOfBirthText.isEmpty()){
            dateOfBirth.setError("Please input date");
            state=false;
        }
        return state;
    }

    private boolean isValidPassWord(String passwords){
        Pattern PASSWORD_PATTERN=Pattern.compile("^(?=.*[0-9])(?=.*[A-Z])(?=.*[@#%^&+=!*])(?=\\S+$).{8,16}$");
        return PASSWORD_PATTERN.matcher(passwords).matches();
    }
}
