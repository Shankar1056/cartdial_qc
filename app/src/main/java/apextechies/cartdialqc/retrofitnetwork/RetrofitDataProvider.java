package apextechies.cartdialqc.retrofitnetwork;


import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import java.util.concurrent.TimeUnit;

import apextechies.cartdialqc.api.WebServices;
import apextechies.cartdialqc.common.ConstantValue;
import apextechies.cartdialqc.model.AddUpdateQCListModel;
import apextechies.cartdialqc.model.CommonResponce;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitDataProvider extends AppCompatActivity implements ServiceMethods {
    private Context context;

    private ApiRetrofitService createRetrofitService() {

        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient okHttpClient = new OkHttpClient().newBuilder()
                .connectTimeout(60, TimeUnit.SECONDS)
                .readTimeout(60, TimeUnit.SECONDS)
                .writeTimeout(60, TimeUnit.SECONDS)
                .addInterceptor(interceptor)
                .build();


        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(WebServices.BASEURL)
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        return retrofit.create(ApiRetrofitService.class);

    }

    public  RetrofitDataProvider(Context context)
    {
        this.context = context;
    }


    @Override
    public void addUpateQclist(String api_token, AddUpdateQCListModel addUpdateQCListModel, final DownlodableCallback<CommonResponce> callback) {
        createRetrofitService().addUdateQcData(api_token, addUpdateQCListModel).enqueue(
                new Callback<CommonResponce>() {
                    @Override
                    public void onResponse(@NonNull Call<CommonResponce> call, @NonNull final Response<CommonResponce> response) {
                        if (response.code()==200 || response.code()==201) {

                            CommonResponce mobileRegisterPojo = response.body();
                            callback.onSuccess(mobileRegisterPojo);

                        } else {
                            callback.onOtherResult(response.code());
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<CommonResponce> call, @NonNull Throwable t) {
                        Log.d("Result", "t" + t.getMessage());
                        callback.onFailure(t.getMessage());

                    }
                }
        );
    }
}
