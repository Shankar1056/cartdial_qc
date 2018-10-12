package apextechies.cartdialqc.api

import android.content.Context
import android.os.AsyncTask
import android.util.Log
import android.widget.Toast
import apextechies.cartdialqc.model.AddUpdateQCListModel

import org.apache.http.NameValuePair

import java.util.ArrayList


class Download_web(private val context: Context, private val listener: OnTaskCompleted?) : AsyncTask<String, Void, String>() {
    private var response = ""
    private var isGet = true
    private var api_token = ""
    private var addUpdateQcList = AddUpdateQCListModel()
    private var data: ArrayList<NameValuePair>? = null
    fun setReqType(isGet: Boolean) {
        this.isGet = isGet
    }

    fun setData(data: ArrayList<NameValuePair>) {
        this.data = data
    }

    fun setApiToken(token: String) {
        this.api_token = api_token
    }


    override fun doInBackground(vararg params: String): String {

        for (url in params) {

                if (isGet) {
                    response = doGet(url, api_token)
                } else {
                    response = doPost(url, this!!.data!!, api_token)
                }


        }

        return response
    }


    override fun onPreExecute() {

        super.onPreExecute()
    }


    override fun onPostExecute(result: String) {

        if (result != "") {
            listener?.onTaskCompleted(result)
        } else {

            Toast.makeText(context, "something wrong", Toast.LENGTH_LONG).show()
            listener?.onTaskCompleted("")
        }

    }

    private fun doGet(url: String, api_token: String): String {
        try {

            response = Utilz.executeHttpGet(url, api_token)

        } catch (e: Exception) {
            e.printStackTrace()
            Log.e("Exception", e.message)
            response = ""
            return response
        }

        return response
    }

    private fun doPost(url: String, params: ArrayList<NameValuePair>, api_token: String): String {
        try {

            response = Utilz.executeHttpPost(url, params, api_token)

        } catch (e: Exception) {
            e.printStackTrace()
            Log.e("Exception", e.message)
            response = ""
            return response
        }

        return response
    }

    fun setQcData(addUpdateQcList: AddUpdateQCListModel) {
        this.addUpdateQcList = addUpdateQcList

    }


}