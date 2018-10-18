package apextechies.cartdialqc.activity

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import apextechies.cartdialqc.R
import apextechies.cartdialqc.adapter.QcListAdapter
import apextechies.cartdialqc.api.Download_web
import apextechies.cartdialqc.api.OnTaskCompleted
import apextechies.cartdialqc.api.WebServices
import apextechies.cartdialqc.common.ClsGeneral
import apextechies.cartdialqc.common.ConstantValue
import apextechies.cartdialqc.model.AddUpdateQCListModel
import apextechies.cartdialqc.model.CommonResponce
import apextechies.cartdialqc.model.DeviceQuestionModel
import apextechies.cartdialqc.model.QcListModel
import apextechies.cartdialqc.retrofitnetwork.DownlodableCallback
import apextechies.cartdialqc.retrofitnetwork.RetrofitDataProvider
import kotlinx.android.synthetic.main.activity_devicefeature.*
import kotlinx.android.synthetic.main.toolbar.*
import org.json.JSONException
import org.json.JSONObject

class DeviceFeatureOptionActivity : AppCompatActivity() {
    val list = ArrayList<DeviceQuestionModel>()
    var sendList = ArrayList<DeviceQuestionModel>()
    var retrofitDataProvider: RetrofitDataProvider? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_devicefeature)

        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.title = "device diagnostic"
        featurelistRV.layoutManager = LinearLayoutManager(this)
        retrofitDataProvider = RetrofitDataProvider(this)

        getdata()

        toolbar.setNavigationOnClickListener {
            finish()
        }

        nextBTN.setOnClickListener {
            var qcList = getData()
            Log.e("value", qcList.toString())

            retrofitDataProvider!!.addUpateQclist(ClsGeneral.getPreferences(this, ConstantValue.APITOKEN), qcList, object : DownlodableCallback<CommonResponce> {
                override fun onSuccess(result: CommonResponce?) {

                    startActivity(Intent(this@DeviceFeatureOptionActivity, ImageActivity::class.java))

                }

                override fun onFailure(error: String?) {
                    startActivity(Intent(this@DeviceFeatureOptionActivity, ImageActivity::class.java))
                }

                override fun onOtherResult(errorNumber: Int) {}
            })

        }
    }

    fun getData(): AddUpdateQCListModel {
        var model = AddUpdateQCListModel()
        var qclist = ArrayList<QcListModel>()
        model.operation = "insert"
        model.order_id = intent.getStringExtra("orderid")
        model.user_id = ClsGeneral.getPreferences(this, ConstantValue.USERID)
        for (i in 0 until list.size) {
            qclist.add(QcListModel(list.get(i).function_name, list[i].selectedText))
        }
        model.data = qclist
        return model
    }

    private fun getdata() {
        var web = Download_web(this, object : OnTaskCompleted {
            override fun onTaskCompleted(response: String) {
                if (response.length > 0) {


                    try {
                        var obj = JSONObject(response)
                        var array = obj.getJSONArray("data")
                        for (i in 0 until array.length()) {
                            var jo = array.getJSONObject(i)
                            list.add(DeviceQuestionModel(jo.optString("id"), jo.optString("function_name"), jo.optString("status")))
                        }

                        featurelistRV.adapter = QcListAdapter(this@DeviceFeatureOptionActivity, list)
                    } catch (e: JSONException) {

                    }
                }
            }
        })

        web.setReqType(true)
        web.setApiToken(ClsGeneral.getPreferences(this, WebServices.XAPIKEY))
        web.execute(WebServices.GETQUESTION)
    }
}