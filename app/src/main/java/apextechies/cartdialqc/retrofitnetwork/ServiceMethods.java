package apextechies.cartdialqc.retrofitnetwork;


import apextechies.cartdialqc.model.AddUpdateQCListModel;
import apextechies.cartdialqc.model.CommonResponce;

public interface ServiceMethods {

    void addUpateQclist(String api_token, AddUpdateQCListModel addUpdateQCListModel, DownlodableCallback<CommonResponce> callback);


}
