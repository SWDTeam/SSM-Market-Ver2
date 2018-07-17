package test.kietpt.smartmarket.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import test.kietpt.smartmarket.R;
import test.kietpt.smartmarket.model.OrderDTO;

public class MyOrderedAdapter extends BaseAdapter{
    Context context;
    ArrayList<OrderDTO> listOrdered;

    public MyOrderedAdapter(Context context, ArrayList<OrderDTO> listOrdered) {
        this.context = context;
        this.listOrdered = listOrdered;
    }

    @Override
    public int getCount() {
        return listOrdered.size();
    }

    @Override
    public Object getItem(int position) {
        return listOrdered.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }
    public class ViewHolder{
        TextView totalPrice,orderedDate,orderCode,totalQuantity,status;
        ImageView img;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder =null;
        if(convertView == null){
            viewHolder = new ViewHolder();
            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.ordered_item,null);
            viewHolder.orderCode = (TextView)convertView.findViewById(R.id.txtOrderedCode);
            viewHolder.orderedDate = (TextView)convertView.findViewById(R.id.txtDateOrdered);
            viewHolder.totalPrice = (TextView)convertView.findViewById(R.id.txtPriceOrdered);
            viewHolder.totalQuantity = (TextView)convertView.findViewById(R.id.txtQuantityOrdered);
            viewHolder.img = (ImageView)convertView.findViewById(R.id.imgOrdered);
            viewHolder.status = (TextView)convertView.findViewById(R.id.txtStatusOrdered);
            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder)convertView.getTag();
        }
        OrderDTO orderDTO = listOrdered.get(position);
        viewHolder.orderCode.setText(orderDTO.getOrderCode());
        viewHolder.orderedDate.setText((orderDTO.getOrderedDate()));
        viewHolder.totalQuantity.setText(String.valueOf(orderDTO.getTotalQuantity()));
        if(orderDTO.getStatus().equals("payment")){
            viewHolder.status.setTextColor(Color.parseColor("#f28016"));
            viewHolder.status.setText(orderDTO.getStatus());
            viewHolder.img.setBackgroundResource(R.drawable.invoicelarge);
        }else{
            viewHolder.status.setText(orderDTO.getStatus());
        }

        final DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
        viewHolder.totalPrice.setText(decimalFormat.format(orderDTO.getTotalPrice()) + " $ ");

        return convertView;
    }
}
