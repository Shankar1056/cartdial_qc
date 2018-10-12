package apextechies.cartdialqc.model

import com.google.gson.annotations.SerializedName

class CommonResponce {

    @SerializedName("status")
    var status: String? = null
    @SerializedName("message")
    var message: String? = null
    @SerializedName("data")
    var data: String? = null
}