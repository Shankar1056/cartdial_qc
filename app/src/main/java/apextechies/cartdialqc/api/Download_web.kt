package apextechies.cartdialqc.api

import android.content.Context
import android.os.AsyncTask
import android.util.Log
import android.widget.Toast

import org.apache.http.NameValuePair

import java.util.ArrayList


class Download_web(private val context: Context, private val listener: OnTaskCompleted?) : AsyncTask<String, Void, String>() {
    private var response = ""
    private var isGet = true
    private var data: ArrayList<NameValuePair>? = null
    fun setReqType(isGet: Boolean) {
        this.isGet = isGet
    }

    fun setData(data: ArrayList<NameValuePair>) {
        this.data = data
    }


    override fun doInBackground(vararg params: String): String {

        for (url in params) {
            if (isGet) {
                response = doGet(url)
            }
            else{
                response = doPost(url, this!!.data!!);
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

    private fun doGet(url: String): String {
        try {

            response = Utilz.executeHttpGet(url)

        } catch (e: Exception) {
            e.printStackTrace()
            Log.e("Exception", e.message)
            response = ""
            return response
        }

        return response
    }

    private fun doPost(url: String, params: ArrayList<NameValuePair>): String {
        try {

            response = Utilz.executeHttpPost(url, params)

        } catch (e: Exception) {
            e.printStackTrace()
            Log.e("Exception", e.message)
            response = ""
            return response
        }

        return response
    }


}