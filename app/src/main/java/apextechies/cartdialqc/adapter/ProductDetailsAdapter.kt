package comcater.employeemanagement.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import apextechies.cartdialqc.R
import apextechies.cartdialqc.model.ProductDetailsmodel

class ProductDetailsAdapter( private val myCartModels: ArrayList<ProductDetailsmodel>) : RecyclerView.Adapter<ProductDetailsAdapter.MyViewHolder>() {

    inner class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var productTV: TextView
        var ordrsourceTV: TextView
        var priceTV: TextView
        var paymentmethodTV: TextView

        init {
            productTV = view.findViewById<View>(R.id.productTV) as TextView
            ordrsourceTV = view.findViewById<View>(R.id.ordrsourceTV) as TextView
            priceTV = view.findViewById<View>(R.id.priceTV) as TextView
            paymentmethodTV = view.findViewById<View>(R.id.paymentmethodTV) as TextView
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context)
                .inflate(R.layout.row_product_details, parent, false)

        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val sa = myCartModels!![position]

        holder.productTV.text = sa.order_item_name
      for (i in 0 until sa.product_price!!.size){
          if (sa.product_price!![i].meta_key.equals("_line_total"))
              holder.priceTV.text = sa.product_price!![i].meta_value
       }
    }

    override fun getItemCount(): Int {
        return myCartModels!!.size
    }

}
