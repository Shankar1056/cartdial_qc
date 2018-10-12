package apextechies.cartdialqc.retrofitnetwork;

/**
 * Created by akshay on 12/8/17.
 */

public interface DownlodableCallback<T> {


    void onSuccess(T result);

    void onFailure(String error);
    void onOtherResult(int errorNumber);
}
