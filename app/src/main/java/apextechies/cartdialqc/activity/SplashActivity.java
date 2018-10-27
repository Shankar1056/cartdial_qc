package apextechies.cartdialqc.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import apextechies.cartdialqc.R;
import apextechies.cartdialqc.api.Download_web;
import apextechies.cartdialqc.api.OnTaskCompleted;
import apextechies.cartdialqc.api.Utilz;
import apextechies.cartdialqc.common.ClsGeneral;
import apextechies.cartdialqc.common.ConstantValue;


/**
 * Created by Shankar on 3/30/2018.
 */

public class SplashActivity extends AppCompatActivity {
    public static final int RC_SIGN_IN = 111;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash);
       // FirebaseApp.initializeApp(this);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);

        Thread timer= new Thread()
        {
            public void run()
            {
                try
                {
                    //Display for 3 seconds
                    sleep(3000);
                }
                catch (InterruptedException e)
                {
                    // TODO: handle exception
                    e.printStackTrace();
                }
                finally
                {
                    calApi();
                }
            }
        };
        timer.start();
    }

    private void calApi() {
        Download_web web = new Download_web(SplashActivity.this, new OnTaskCompleted() {
            @Override
            public void onTaskCompleted(String response) {

                try {
                    JSONObject jsonObject  = new JSONObject(response);
                    JSONArray jsonArray = jsonObject.getJSONArray("data");
                    for (int i=0;i<jsonArray.length(); i++){
                        JSONObject jsonObject1  = jsonArray.getJSONObject(0);
                        if (jsonObject1.getString("status").equals("0")){
                            Utilz.displayMessageAlert("oohh, App not woring? if you are admin, please call 8002877277", SplashActivity.this);
                        }else {
                            if (ClsGeneral.getPreferences(SplashActivity.this,ConstantValue.USERID).equals("")){
                                startActivity(new Intent(SplashActivity.this, LoginActivity.class));
                                finish();
                            }else {
                                startActivity(new Intent(SplashActivity.this, MainActivity.class));
                                finish();
                            }
                        }
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

        web.setReqType(true);
        web.execute("http://apextechies.com/englishbolega/index.php/start_stop_app");
    }


}
