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
        Button btnAddOrderDetail, btnSubOrderDetail, btnRemoveOrderDetail;

    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.product_in_cart_item, null);
            viewHolder.name = (TextView) convertView.findViewById(R.id.txtNameCartItem);
            viewHolder.price = (TextView) convertView.findViewById(R.id.txtPriceCartItem);
            viewHolder.imgPic = (ImageView) convertView.findViewById(R.id.imgCartItem);
            viewHolder.quantity = (TextView) convertView.findViewById(R.id.txtQuantityCartItem);
            viewHolder.btnAddOrderDetail = (Button) convertView.findViewById(R.id.btnAddCartItem);
            viewHolder.btnSubOrderDetail = (Button) convertView.findViewById(R.id.btnSubCartItem);
            viewHolder.btnRemoveOrderDetail = (Button) convertView.findViewById(R.id.btnRemoveCartItem);
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

        int newSl = Integer.parseInt(viewHolder.quantity.getText().toString());
        if (newSl <= 1) {
            viewHolder.btnSubOrderDetail.setVisibility(View.INVISIBLE);
            viewHolder.btnAddOrderDetail.setVisibility(View.VISIBLE);
        } else {
            viewHolder.btnSubOrderDetail.setVisibility(View.VISIBLE);
            viewHolder.btnAddOrderDetail.setVisibility(View.VISIBLE);
        }

        viewHolder.btnAddOrderDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int newSl = Integer.parseInt(viewHolder.quantity.getText().toString()) + 1;
                int presentSl = listOrderedDetail.get(position).getQuantity();
                float presentPrice = listOrderedDetail.get(position).getPrice();
                listOrderedDetail.get(position).setQuantity(newSl);
                float newPrice = (presentPrice * newSl) / presentSl;
                listOrderedDetail.get(position).setPrice(newPrice);
                DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
                viewHolder.price.setText(decimalFormat.format(newPrice) + " $ ");
                OrderedDetailActi.getTotalPriceInOrderDetail();
                if (newSl > 1) {
                    viewHolder.btnSubOrderDetail.setVisibility(View.VISIBLE);
                }
                viewHolder.quantity.setText(String.valueOf(newSl));

            }
        });

        viewHolder.btnSubOrderDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int newSl = Integer.parseInt(viewHolder.quantity.getText().toString()) - 1;
                int presentSl = listOrderedDetail.get(position).getQuantity();
                float presentPrice = listOrderedDetail.get(position).getPrice();
                listOrderedDetail.get(position).setQuantity(newSl);
                float newPrice = (presentPrice * newSl) / presentSl;
                listOrderedDetail.get(position).setPrice(newPrice);
                DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
                viewHolder.price.setText(decimalFormat.format(newPrice) + " $ ");
                OrderedDetailActi.getTotalPriceInOrderDetail();
                if (newSl <= 1) {
                    viewHolder.btnSubOrderDetail.setVisibility(View.INVISIBLE);
                } else {
                    viewHolder.btnSubOrderDetail.setVisibility(View.VISIBLE);
                }
                viewHolder.quantity.setText(String.valueOf(newSl));

            }
        });

        viewHolder.btnRemoveOrderDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Remove item out OrderDetail !!!");
                builder.setMessage("Do you want to remove item?");
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        OrderDetailDTO orderDetail = listOrderedDetail.get(position);
                        listOrderedDetail.remove(orderDetail);
                        OrderedDetailActi.getTotalPriceInOrderDetail();
                        notifyDataSetChanged();

                    }
                });
                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
                            @Override
                            public void onDismiss(DialogInterface dialog) {
                                dialog.dismiss();
                            }
                        });
                    }
                });
                builder.show();
            }
        });
        return convertView;
    }


}
