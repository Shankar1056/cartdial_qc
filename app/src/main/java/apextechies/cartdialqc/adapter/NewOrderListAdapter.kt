package apextechies.cartdialqc.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import apextechies.cartdialqc.R
import apextechies.cartdialqc.R.id.ordrsourceTV
import apextechies.cartdialqc.R.id.priceTV
import apextechies.cartdialqc.model.NewOrderModel
import apextechies.cartdialqc.model.ProductDetailsmodel
import comcater.employeemanagement.adapter.ProductDetailsAdapter

class NewOrderListAdapter (private val myCartModels: ArrayList<NewOrderModel>, private val click: OnItemClick) : RecyclerView.Adapter<NewOrderListAdapter.MyViewHolder>() {

    inner class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var orerId: TextView

        init {
            orerId = view.findViewById<View>(R.id.orerId) as TextView
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context)
                .inflate(R.layout.row_new_orders, parent, false)

        return MyViewHolder(itemView)
    }

    interface OnItemClick {
        fun onClick(pos: Int)

    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val sa = myCartModels!![position]

        holder.orerId.text = "Order Id: "+sa.id

        holder.itemView.setOnClickListener {
            click.onClick(position)
        }
    }

    override fun getItemCount(): Int {
        return myCartModels!!.size
    }

}
