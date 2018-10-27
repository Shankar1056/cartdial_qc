package apextechies.cartdialqc.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import apextechies.cartdialqc.R;
import apextechies.cartdialqc.model.DeliveryoyListModel;

public class DeliveryBoyAdapter extends BaseAdapter {
    Context context;
   ArrayList<DeliveryoyListModel> list;
    LayoutInflater inflter;

    public DeliveryBoyAdapter(Context applicationContext, ArrayList<DeliveryoyListModel> list) {
        this.context = applicationContext;
        this.list = list;
        inflter = (LayoutInflater.from(applicationContext));
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        view = inflter.inflate(R.layout.custom_spinner_items, null);
        TextView names = (TextView) view.findViewById(R.id.orerId);
        names.setText(list.get(i).getName());
        return view;
    }
}