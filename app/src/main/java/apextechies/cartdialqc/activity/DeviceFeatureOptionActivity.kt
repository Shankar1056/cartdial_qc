package apextechies.cartdialqc.activity

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import apextechies.cartdialqc.R
import apextechies.cartdialqc.api.Download_web
import apextechies.cartdialqc.api.OnTaskCompleted
import apextechies.cartdialqc.common.ClsGeneral
import apextechies.cartdialqc.common.ConstantValue
import kotlinx.android.synthetic.main.activity_devicefeature.*
import kotlinx.android.synthetic.main.toolbar.*

class DeviceFeatureOptionActivity: AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_devicefeature)

        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.title = "device diagnostic"
        featurelistRV.layoutManager = LinearLayoutManager(this)

        getdata()

        toolbar.setOnClickListener {
            finish()
        }
    }

    private fun getdata() {
        var web = Download_web(this, object : OnTaskCompleted {
            override fun onTaskCompleted(response: String) {
                if (response.length>0){

                }
            }
        })

        web.setReqType(true)
        web.setApiToken(ClsGeneral.getPreferences(this, ConstantValue.APIKEY))
    }
}