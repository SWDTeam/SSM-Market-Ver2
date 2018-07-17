package test.kietpt.smartmarket.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.util.List;

import test.kietpt.smartmarket.R;
import test.kietpt.smartmarket.activity.AccountActivity;
import test.kietpt.smartmarket.activity.MainActivity;
import test.kietpt.smartmarket.activity.MyCartActi;
import test.kietpt.smartmarket.activity.OrderedDetailActi;
import test.kietpt.smartmarket.model.Cart;
import test.kietpt.smartmarket.model.OrderDTO;
import test.kietpt.smartmarket.model.OrderDetailDTO;

public class OrderDetailAdapter extends BaseAdapter {
    Context context;
    List<OrderDetailDTO> listOrderedDetail;

    public OrderDetailAdapter(Context context, List<OrderDetailDTO> listOrderedDetail) {
        this.context = context;
        this.listOrderedDetail = listOrderedDetail;
    }

    @Override
    public int getCount() {
        return listOrderedDetail.size();
    }

    @Override
    public Object getItem(int position) {
        return listOrderedDetail.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public class ViewHolder {
        TextView price, name, quantity;
        ImageView imgPic;

    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.order_detail_item, null);
            viewHolder.name = (TextView) convertView.findViewById(R.id.txtNameOrderDetail);
            viewHolder.price = (TextView) convertView.findViewById(R.id.txtPriceOrderDetail);
            viewHolder.imgPic = (ImageView) convertView.findViewById(R.id.imgOrderDetail);
            viewHolder.quantity = (TextView) convertView.findViewById(R.id.txtQuantityOrderDetail);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        OrderDetailDTO orderDetailDTO = listOrderedDetail.get(position);
        viewHolder.name.setText(orderDetailDTO.getProductName());
        final DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
        viewHolder.price.setText(decimalFormat.format(orderDetailDTO.getPrice()) + " $ ");
        Picasso.get().load(orderDetailDTO.getImgKey()).placeholder(R.drawable.error).error(R.drawable.errors).into(viewHolder.imgPic);
        viewHolder.quantity.setText(String.valueOf(orderDetailDTO.getQuantity()));

        return convertView;
    }


}
