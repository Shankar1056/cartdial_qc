package apextechies.cartdialqc.retrofitnetwork;


import apextechies.cartdialqc.api.WebServices;
import apextechies.cartdialqc.model.AddUpdateQCListModel;
import apextechies.cartdialqc.model.CommonResponce;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface ApiRetrofitService {

    @Headers("x-api-key:" + WebServices.XAPIKEY)
    @POST(WebServices.INSERTUPDATE_QCSTATUS)
    Call<CommonResponce> addUdateQcData(@Header("api_toke") String api_token, @Body AddUpdateQCListModel addUpdate);


}
