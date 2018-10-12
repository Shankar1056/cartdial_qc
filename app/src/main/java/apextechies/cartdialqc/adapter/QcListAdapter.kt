package apextechies.cartdialqc.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import apextechies.cartdialqc.R
import apextechies.cartdialqc.model.DeviceQuestionModel
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.Toast
import apextechies.cartdialqc.R.id.price
import apextechies.cartdialqc.R.id.qcRG
import java.lang.NullPointerException


class QcListAdapter( private val context: Context, private val myCartModels: ArrayList<DeviceQuestionModel>) : RecyclerView.Adapter<QcListAdapter.MyViewHolder>() {
    inner class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var qcTV: TextView
        var qcRG: RadioGroup
        var yes: RadioButton
        var no: RadioButton
        var nc: RadioButton


        init {
            qcTV = view.findViewById<View>(R.id.qcTV) as TextView
            qcRG = view.findViewById<View>(R.id.qcRG) as RadioGroup
            yes = view.findViewById<View>(R.id.yes) as RadioButton
            no = view.findViewById<View>(R.id.no) as RadioButton
            nc = view.findViewById<View>(R.id.nc) as RadioButton
        }

            fun setOptions(sa: DeviceQuestionModel, pos: Int){
                if(sa.isAnswered) {
                    qcRG.check(sa.checkedId);
                } else {
                    qcRG.check(-1);
                }


                qcRG.setOnCheckedChangeListener(object : RadioGroup.OnCheckedChangeListener {
                    override fun onCheckedChanged(group: RadioGroup?, checkedId: Int) {
                        val pos = group!!.getTag() as Int
                        val que = myCartModels.get(pos)
                        que.isAnswered = true
                        que.checkedId = checkedId
                        try {
                            val radioButton = qcRG.findViewById<View>(qcRG.checkedRadioButtonId) as RadioButton
                            que.selectedText = radioButton.text as String
                        }catch (e: NullPointerException){

                        }

                    }

                })
            }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context)
                .inflate(R.layout.row_qclist, parent, false)

        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val sa = myCartModels!![position]
        holder.qcRG.setTag(position);
        holder.qcTV.text = sa.function_name
        holder.setOptions(sa, position);




    }

    override fun getItemCount(): Int {
        return myCartModels!!.size
    }

}
