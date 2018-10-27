package apextechies.cartdialqc.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.firebase.digitsmigrationhelpers.AuthMigrator;
import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.ErrorCodes;
import com.firebase.ui.auth.IdpResponse;
import com.firebase.ui.auth.ResultCodes;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;

import apextechies.cartdialqc.R;
import apextechies.cartdialqc.api.Download_web;
import apextechies.cartdialqc.api.OnTaskCompleted;
import apextechies.cartdialqc.api.Utilz;
import apextechies.cartdialqc.api.WebServices;
import apextechies.cartdialqc.common.ClsGeneral;
import apextechies.cartdialqc.common.ConstantValue;


/**
 * Created by Shankar on 4/1/2018.
 */

public class LoginActivity extends AppCompatActivity{
    public static final int RC_SIGN_IN = 111;



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        initWidgit();

    }

    private void initWidgit() {
        checkSession();
    }

    private void checkSession() {
            AuthMigrator.getInstance().migrate(true).addOnSuccessListener(this,
                    new OnSuccessListener() {

                        @SuppressLint("WrongConstant")
                        @Override
                        public void onSuccess(Object o) {
                            FirebaseUser u = FirebaseAuth.getInstance().getCurrentUser();
                            if (u != null) {
                               // StaticConfig.UID = u.getUid();
                                callApi(u.getPhoneNumber());

                            } else {
                                startActivityForResult(
                                        AuthUI.getInstance()
                                                .createSignInIntentBuilder()
                                                .setProviders(
                                                        //  Arrays.asList(
                                                        Collections.singletonList(
                                                                new AuthUI.IdpConfig.Builder(AuthUI.PHONE_VERIFICATION_PROVIDER).build()
                                                        )
                                                )
                                                .setTheme(R.style.LoginTheme)
                                                .setLogo(R.mipmap.ic_launcher)
                                                .build(),
                                        RC_SIGN_IN);
                            }
                        }
                    }).addOnFailureListener(this,
                    new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            e.printStackTrace();
                        }
                    });
        }

    private void callApi(String phoneNumber) {
        String mobile = phoneNumber.replace("+91","");
        ArrayList<NameValuePair> nameValuePairs = new ArrayList<>();
        nameValuePairs.add(new BasicNameValuePair("mobile", mobile));
        Download_web web = new Download_web(LoginActivity.this, new OnTaskCompleted() {
            @Override
            public void onTaskCompleted(@NotNull String response) {
                if (response.length()>0){
                    try {
                        JSONObject object = new JSONObject(response);
                        if (object.optString("status").equals("true")){
                            JSONArray jsonArray = object.getJSONArray("data");
                            for (int i=0; i<jsonArray.length(); i++){
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                if (jsonObject.optString("status").equals("0")){
                                    Utilz.displayMessageAlert(getResources().getString(R.string.authentication_message), LoginActivity.this);
                                }
                                else {
                                    ClsGeneral.setPreferences(LoginActivity.this, ConstantValue.USERID, jsonObject.optString("id"));
                                    ClsGeneral.setPreferences(LoginActivity.this, ConstantValue.APITOKEN, jsonObject.optString("api_token"));

                                    startActivity(new Intent(LoginActivity.this, MainActivity.class));
                                    finish();
                                }
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

            }
        });
        web.setReqType(false);
        web.setData(nameValuePairs);
        web.execute(WebServices.LOGIN);
    }


   protected void onActivityResult(int requestCode, int resultCode, Intent data) {
       super.onActivityResult(requestCode, resultCode, data);
       if (requestCode == RC_SIGN_IN) {
           IdpResponse response = IdpResponse.fromResultIntent(data);
           if (resultCode == ResultCodes.OK) {
               FirebaseUser u = FirebaseAuth.getInstance().getCurrentUser();
               //StaticConfig.UID = u.getUid();
               callApi(response.getPhoneNumber());

           } else {
               // Sign in failed
               if (response == null) {
                   // User pressed back button
                   Log.e("Login", "Login canceled by User");
               }
               if (response.getErrorCode() == ErrorCodes.NO_NETWORK) {
               }
               if (response.getErrorCode() == ErrorCodes.UNKNOWN_ERROR) {
                   Log.e("Login", "Unknown Error");
               }
           }
           Log.e("Login", "Unknown sign in response");

       }
   }

}
