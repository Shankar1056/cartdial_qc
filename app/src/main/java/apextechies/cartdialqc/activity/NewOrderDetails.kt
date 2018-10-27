package apextechies.cartdialqc.activity

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import android.widget.AdapterView
import apextechies.cartdialqc.R
import apextechies.cartdialqc.adapter.DeliveryBoyAdapter
import apextechies.cartdialqc.api.Download_web
import apextechies.cartdialqc.api.OnTaskCompleted
import apextechies.cartdialqc.api.Utilz
import apextechies.cartdialqc.api.WebServices
import apextechies.cartdialqc.common.ClsGeneral
import apextechies.cartdialqc.common.ConstantValue
import apextechies.cartdialqc.model.DeliveryoyListModel
import apextechies.cartdialqc.model.ProdPrice
import apextechies.cartdialqc.model.ProductDetailsmodel
import comcater.employeemanagement.adapter.ProductDetailsAdapter
import kotlinx.android.synthetic.main.content_neworder.*
import kotlinx.android.synthetic.main.toolbar.*
import org.apache.http.NameValuePair
import org.apache.http.message.BasicNameValuePair
import org.json.JSONException
import org.json.JSONObject

class NewOrderDetails: AppCompatActivity(), AdapterView.OnItemSelectedListener {
    var deliverlist = ArrayList<DeliveryoyListModel>()
    var boy_id = ""
    override fun onNothingSelected(parent: AdapterView<*>?) { }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        boy_id = deliverlist[position].id!!
    }

    var ordrcreatedby = ""
    var paymentMethod = ""
    var prodList = ArrayList<ProductDetailsmodel>()
    var priceList = ArrayList<ProdPrice>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_newdetails)

        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.title = "Order Details"
        searchCV.visibility = View.GONE

        initWidgit()

        call.setOnClickListener {
            if (!checkPermissions()) {
                requestPermissions()
            } else {
                makecall()
            }
        }

        assign.setOnClickListener {

            var datalist = ArrayList<NameValuePair>()
            datalist.add(BasicNameValuePair("order_id", intent.getStringExtra("id")))
            datalist.add(BasicNameValuePair("admin_id", ClsGeneral.getPreferences(this, ConstantValue.USERID)))
            datalist.add(BasicNameValuePair("boy_id", boy_id))
            var web = Download_web(this, object : OnTaskCompleted {
                override fun onTaskCompleted(response: String) {

                    updateStatusInDezentsDB()

                }
            })
            web.setReqType(false)
            web.setApiToken(ClsGeneral.getPreferences(this, ConstantValue.APITOKEN))
            web.setData(datalist)
            web.execute(WebServices.ASSIGNORDER)

        }

    }

    private fun updateStatusInDezentsDB() {
        //Utilz.showDailog(this, "plese wait...")
        var list = ArrayList<NameValuePair>()
        list.add(BasicNameValuePair("order_id", intent.getStringExtra("id")))
        list.add(BasicNameValuePair("status", "wc-pickupassigned"))
        var web = Download_web(this, object : OnTaskCompleted {
            override fun onTaskCompleted(response: String) {

                if (response.length>0){

                   finish()
                }

            }
        })

        web.setData(list)
        web.setReqType(false)
        web.setApiToken(ClsGeneral.getPreferences(this, ConstantValue.APITOKEN))
        web.execute(WebServices.UPDATEORDERSTATUS)
    }

    fun checkPermissions(): Boolean {
        val permissionState = ActivityCompat.checkSelfPermission(this,
                Manifest.permission.CALL_PHONE)
        return permissionState == PackageManager.PERMISSION_GRANTED
    }
    fun requestPermissions() {
        val shouldProvideRationale = ActivityCompat.shouldShowRequestPermissionRationale(this,
                Manifest.permission.CALL_PHONE)

        if (shouldProvideRationale) {
            ActivityCompat.requestPermissions(this,
                    arrayOf(Manifest.permission.CALL_PHONE),
                    REQUEST_PERMISSIONS_REQUEST_CODE)

        } else {
            startLocationPermissionRequest()
        }
    }

    fun startLocationPermissionRequest() {
        ActivityCompat.requestPermissions(this@NewOrderDetails,
                arrayOf(Manifest.permission.CALL_PHONE),
                REQUEST_PERMISSIONS_REQUEST_CODE)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>,
                                            grantResults: IntArray) {
        if (requestCode == REQUEST_PERMISSIONS_REQUEST_CODE) {
            if (grantResults.size <= 0) {
            } else if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                makecall()
            } else {
            }
        }
    }


    private fun makecall() {
        try {
            val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:"+mobileTV.text.toString()))
            startActivity(intent)
        } catch (e: Exception) {
            //TODO smth
        }

    }


    private fun initWidgit() {
        spinner.setOnItemSelectedListener(this);
        prodRV.layoutManager = LinearLayoutManager(this)
        prodRV.isNestedScrollingEnabled = false

                val list = ArrayList<NameValuePair>()
                list.add(BasicNameValuePair("order_id", intent.getStringExtra("id")))
                var web = Download_web(this, object : OnTaskCompleted {
                    override fun onTaskCompleted(response: String) {
                        getProdDetails()
                        getDeliveryBorList()
                        var firstname = ""
                        var lastname = ""
                        var adrdess1 = ""
                        var address2 = ""
                        var city = ""
                        var state = ""
                        var pincode = ""
                        try {

                            var obj = JSONObject(response)
                            var array = obj.getJSONArray("data")
                            if (array.length() > 0) {
                                ordrdetailsCV.visibility = View.VISIBLE
                                for (i in 0 until array.length()) {
                                    var jo = array.getJSONObject(i)
                                    if (jo.optString("meta_key").equals("_created_via")) {
                                        ordrcreatedby = jo.optString("meta_value")
                                    }
                                    if (jo.optString("meta_key").equals("_billing_first_name")) {
                                        firstname = jo.optString("meta_value")
                                        nameTV.text = firstname
                                    }
                                    if (jo.optString("meta_key").equals("_billing_last_name")) {
                                        lastname = jo.optString("meta_value")
                                        nameTV.text = firstname + " " + lastname
                                    }
                                    if (jo.optString("meta_key").equals("_billing_company")) {
                                        var ordrcreatedby = jo.optString("meta_value")
                                    }
                                    if (jo.optString("meta_key").equals("_billing_email")) {
                                        emailTV.text = jo.optString("meta_value")
                                    }
                                    if (jo.optString("meta_key").equals("_billing_phone")) {
                                        mobileTV.text = jo.optString("meta_value")
                                    }
                                    if (jo.optString("meta_key").equals("_billing_address_1")) {
                                        adrdess1 = jo.optString("meta_value")
                                    }
                                    if (jo.optString("meta_key").equals("_billing_address_2")) {
                                        address2 = jo.optString("meta_value")
                                    }
                                    if (jo.optString("meta_key").equals("_billing_city")) {
                                        city = jo.optString("meta_value")
                                    }
                                    if (jo.optString("meta_key").equals("_billing_state")) {
                                        state = jo.optString("meta_value")
                                    }
                                    if (jo.optString("meta_key").equals("_billing_postcode")) {
                                        pincode = jo.optString("meta_value")
                                        addressTV.text = adrdess1 + " " + address2 + " " + city + " " + state + " " + pincode
                                    }
                                    if (jo.optString("meta_key").equals("_payment_method")) {
                                        paymentMethod = jo.optString("meta_value")
                                    }
                                }
                            }
                        } catch (e: JSONException) {
                            e.printStackTrace()
                        }

                    }


                })

                web.setReqType(false);
                web.setData(list)
                web.execute(WebServices.USERDETAILS_BYORDERID)

    }

    private fun getDeliveryBorList() {
        var web = Download_web(this, object : OnTaskCompleted {
            override fun onTaskCompleted(response: String) {
                if (response.length>0){

                    var obj = JSONObject(response)
                    var array = obj.getJSONArray("data")
                    if (array.length() > 0) {
                        for (i in 0 until array.length()) {
                            var jo = array.getJSONObject(i)

                            deliverlist.add(DeliveryoyListModel(jo.optString("id"), jo.optString("name"), jo.optString("mobile_number")))
                        }
                        spinner.adapter = DeliveryBoyAdapter(this@NewOrderDetails,deliverlist)
                    }

                }

            }
        })
        web.setReqType(true)
        web.setApiToken(ClsGeneral.getPreferences(this, ConstantValue.APITOKEN))
        web.execute(WebServices.DELIVERYBOYLIST)
    }

    fun getProdDetails() {
        var list = ArrayList<NameValuePair>()
        list.add(BasicNameValuePair("order_id", intent.getStringExtra("id")))
        var web = Download_web(this, object : OnTaskCompleted {
            override fun onTaskCompleted(response: String) {
                try {
                    prodList.clear()
                    priceList.clear()
                    var obj = JSONObject(response)
                    var array = obj.getJSONArray("data")
                    if (array.length() > 0) {
                        for (i in 0 until array.length()) {
                            var jo = array.getJSONObject(i)
                            var priceArray = jo.getJSONArray("product_price")
                            var dummylist = ArrayList<ProdPrice>()
                            for (j in 0 until priceArray.length()) {
                                var priceobj = priceArray.getJSONObject(j)


                                priceList.add(ProdPrice(priceobj.optString("meta_key"), priceobj.optString("meta_value")))
                                dummylist.addAll(priceList)
                                priceList.clear()
                            }
                            prodList.add(ProductDetailsmodel(jo.optString("order_item_type"), jo.optString("order_item_name"), dummylist))

                        }

                        prodRV.adapter = ProductDetailsAdapter(prodList)
                    }
                } catch (e: JSONException) {
                    e.printStackTrace()
                }
            }
        })

        web.setReqType(false)
        web.setData(list)
        web.execute(WebServices.PRODUCTDETAILS_BYORDERID)
    }

    companion object {

       private val REQUEST_PERMISSIONS_REQUEST_CODE = 34
    }
}