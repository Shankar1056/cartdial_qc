package apextechies.cartdialqc.activity

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import apextechies.cartdialqc.R
import apextechies.cartdialqc.adapter.NewOrderListAdapter
import apextechies.cartdialqc.api.Download_web
import apextechies.cartdialqc.api.OnTaskCompleted
import apextechies.cartdialqc.api.WebServices
import apextechies.cartdialqc.common.ClsGeneral
import apextechies.cartdialqc.common.ConstantValue
import apextechies.cartdialqc.model.NewOrderModel
import apextechies.cartdialqc.retrofitnetwork.RetrofitDataProvider
import kotlinx.android.synthetic.main.activity_newrequest.*
import kotlinx.android.synthetic.main.toolbar.*
import org.json.JSONObject

class NewRequestList: AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_newrequest)

        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.title = "All New Order"

        newRequestRV.layoutManager = LinearLayoutManager(this)


    }

    override fun onResume() {
        super.onResume()
        getData()
    }

    private fun getData() {
        var web = Download_web(this, object : OnTaskCompleted {
            override fun onTaskCompleted(response: String) {

                if (response.length>0){
                    var list = ArrayList<NewOrderModel>()
                    var jobj = JSONObject(response)
                    var array = jobj.getJSONArray("data")
                    for (i in 0 until array.length()){
                        var jo = array.optJSONObject(i)

                        list.add(NewOrderModel(jo.optString("ID")))
                    }
                    newRequestRV.adapter = NewOrderListAdapter(list, object : NewOrderListAdapter.OnItemClick {
                        override fun onClick(pos: Int) {

                            startActivity(Intent(this@NewRequestList, NewOrderDetails::class.java)
                                    .putExtra("id", list[pos].id))
                        }

                    })
                }
            }

        })
        web.setReqType(true)
        web.setApiToken(ClsGeneral.getPreferences(this, ConstantValue.APITOKEN))
        web.execute(WebServices.NEWORDERLIST)
    }
}