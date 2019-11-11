package hashim.org.clevermindpobict;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.ImageDecoder;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Patterns;
import android.view.View;
import android.widget.DatePicker;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.regex.Pattern;

import de.hdodenhof.circleimageview.CircleImageView;

public class SignUp extends AppCompatActivity {

    private EditText fullName,userName,email,passWord,phoneNumber,dateOfBirth;
    private String fullNameText,userNameText,emailText,passWordText,phoneNumberText,dateOfBirthText;
    private ImageView signUpBtn;
    private ProgressBar loading;
    private CircleImageView profileImage;
    private static final String api_url="http://192.168.1.60/concept-master/sigunup.php";
    private static final String upload_url="http://192.168.1.60/concept-master/upload_image.php";
    private final Calendar birthCalender=Calendar.getInstance();
    private Bitmap bitmap;
    private int counter=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        signUpBtn=findViewById(R.id.signUpBtn2);
        dateOfBirth=findViewById(R.id.signUpBirthDate);
        loading=findViewById(R.id.loading);
        profileImage=findViewById(R.id.profile);
        fullName=findViewById(R.id.fullName);
        userName=findViewById(R.id.userNameSign);
        email=findViewById(R.id.signUpemail);
        passWord=findViewById(R.id.signUpassWord);
        phoneNumber=findViewById(R.id.signUpPhoneNo);
        dateOfBirth=findViewById(R.id.signUpBirthDate);


        profileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseImage();
            }
        });


        final DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month,int day) {
                birthCalender.set(Calendar.YEAR,year);
                birthCalender.set(Calendar.MONTH,month);
                birthCalender.set(Calendar.DAY_OF_MONTH,day);
                updateLabel();
            }
        };

        dateOfBirth.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {

                DatePickerDialog dialog= new DatePickerDialog(SignUp.this, dateSetListener, birthCalender
                        .get(Calendar.YEAR), birthCalender.get(Calendar.MONTH),
                        birthCalender.get(Calendar.DAY_OF_MONTH));
                dialog.getDatePicker().setMaxDate(System.currentTimeMillis());
                dialog.show();

            }
        });

        signUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fullNameText=fullName.getText().toString().trim();
                userNameText=userName.getText().toString().trim();
                emailText=email.getText().toString().trim();
                passWordText=passWord.getText().toString().trim();
                phoneNumberText=phoneNumber.getText().toString().trim();
                dateOfBirthText=dateOfBirth.getText().toString().trim();

                if (validation()) {
                    signUpClick();
                    if (counter==1)
                    uploadImage(getStringImage(bitmap),userNameText);
                }
            }
        });
    }

    private void updateLabel(){
        String format="dd/MM/yyyy";
        SimpleDateFormat DateFormat=new SimpleDateFormat(format, Locale.US);
        dateOfBirth.setText(DateFormat.format(birthCalender.getTime()));
    }

    private void signUpClick(){

        loading.setVisibility(View.VISIBLE);
        signUpBtn.setVisibility(View.GONE);

        SharedPreferences signUpPref = getSharedPreferences("LogIn", Context.MODE_PRIVATE);
        final SharedPreferences.Editor editor= signUpPref.edit();

        RequestQueue queue= Volley.newRequestQueue(getApplicationContext());

        final JSONObject params=new JSONObject();
        try {
            params.put("fullName",fullNameText);
            params.put("userName",userNameText);
            params.put("email",emailText);
            params.put("phoneNumber",phoneNumberText);
            params.put("password",passWordText);
            params.put("dateOfBirth",dateOfBirthText);
            params.put("image",userNameText+".jpeg");
        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(getApplicationContext(),"this is "+e.toString(),Toast.LENGTH_SHORT).show();
        }
        JsonObjectRequest stringRequest=new JsonObjectRequest(Request.Method.POST, api_url, params, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    String result=response.getString("result");
                    Toast.makeText(getApplicationContext(),result,Toast.LENGTH_SHORT).show();
                    if (result.equals("data inserted")){
                        loading.setVisibility(View.GONE);
                        signUpBtn.setVisibility(View.VISIBLE);
                        editor.putString("username",params.getString("userName"));
                        editor.putString("password",params.getString("password"));
                        editor.apply();
                        Intent intent=new Intent(SignUp.this,HomeActivity.class);
                        startActivity(intent);
                        SignUp.this.finish();
                    }
                    else{
                        loading.setVisibility(View.GONE);
                        signUpBtn.setVisibility(View.VISIBLE);
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
                loading.setVisibility(View.VISIBLE);
                signUpBtn.setVisibility(View.GONE);
            }
        });

        queue.add(stringRequest);
    }

    private boolean validation(){
        boolean state=true;
        boolean emailState= Patterns.EMAIL_ADDRESS.matcher(emailText).matches();
        boolean passWordState=isValidPassWord(passWordText);

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
        if (passWordText.isEmpty() || !passWordState){
            passWord.setError("Please input correct password");
            Toast.makeText(getApplicationContext(),"Password must contain letter and character & at least 8 digits",Toast.LENGTH_LONG).show();
            state=false;
        }
        if (phoneNumberText.length()!=10){
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

    private void chooseImage(){
        Intent intent=new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent,"Select Profile Image"),1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==1 && resultCode==RESULT_OK && data!=null && data.getData()!=null){
            Uri filePath=data.getData();
            try {
                if (Build.VERSION.SDK_INT>=29){
                    ImageDecoder.Source source=ImageDecoder.createSource(this.getContentResolver(),filePath);
                    bitmap=ImageDecoder.decodeBitmap(source);
                }
                else {
                    bitmap= MediaStore.Images.Media.getBitmap(getContentResolver(),filePath);
                }

                profileImage.setImageBitmap(bitmap);
                counter=1;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void uploadImage(final String photo,final String id){
        RequestQueue queue=Volley.newRequestQueue(getApplicationContext());
        JSONObject object=new JSONObject();
        try {
            object.put("id", id);
            object.put("photo",photo);
        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(getApplicationContext(),e.toString(),Toast.LENGTH_SHORT).show();
        }
        JsonObjectRequest request=new JsonObjectRequest(Request.Method.POST, upload_url,object, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    String success=response.getString("success");
                    if (success.equals("1")){
                        Toast.makeText(getApplicationContext(),response.getString("message"),Toast.LENGTH_SHORT).show();
                    }
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

    private String getStringImage(Bitmap bitmap){
        ByteArrayOutputStream byteArrayOutputStream=new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG,100,byteArrayOutputStream);
        byte[] imageByteArray= byteArrayOutputStream.toByteArray();
        return Base64.encodeToString(imageByteArray,Base64.DEFAULT);
    }
}
