package hashim.org.clevermindpobict;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;

public class Splash extends AppCompatActivity {

    private final static int SPLASH_LENGTH=2000;
    private String userName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        SharedPreferences logInPref = getSharedPreferences("LogIn", Context.MODE_PRIVATE);

        userName= logInPref.getString("username","empty");

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (userName.equals("empty")) {
                    Intent logIn = new Intent(Splash.this, LogInActivity.class);
                    Splash.this.startActivity(logIn);
                    Splash.this.finish();
                }
                else{
                    Intent home=new Intent(Splash.this,HomeActivity.class);
                    Splash.this.startActivity(home);
                    Splash.this.finish();
                }
            }
        },SPLASH_LENGTH);
    }
}
