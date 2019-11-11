package hashim.org.clevermindpobict;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class PrivacyPolicy extends AppCompatActivity {

    private TextView privacyPolicy;
    private String privacyDesc="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_privacy_policy);

        privacyPolicy=findViewById(R.id.privacyPolicy);
        ImageView backArrow = findViewById(R.id.ppBack);

        backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PrivacyPolicy.this.finish();
            }
        });

        privacyPolicy.setMovementMethod(new ScrollingMovementMethod());
        RequestQueue queue= Volley.newRequestQueue(getApplicationContext());

        String privacy_url = "http://192.168.1.60/concept-master/fetch_privacy.php";
        JsonArrayRequest request=new JsonArrayRequest(privacy_url, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                JSONObject jsonObject;
                for (int i=0;i<response.length();i++){
                    try {
                        jsonObject=response.getJSONObject(i);
                        privacyDesc+=jsonObject.getString("disc");
                        privacyDesc+="\n";
                        Log.i("desc",privacyDesc);
                    }
                    catch (JSONException e){
                        Toast.makeText(getApplicationContext(),e.toString(),Toast.LENGTH_LONG).show();
                    }
                }
                privacyPolicy.setText(privacyDesc);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        queue.add(request);
    }
}
