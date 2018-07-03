package test.kietpt.smartmarket.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
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
import test.kietpt.smartmarket.activity.MainActivity;
import test.kietpt.smartmarket.activity.MyCartActi;
import test.kietpt.smartmarket.model.Cart;
import test.kietpt.smartmarket.model.ProductDTO;
import test.kietpt.smartmarket.ulti.Database;

public class CartAdapter extends BaseAdapter {
    Context context;
    List<Cart> cartList;
    Database database;

    public CartAdapter(Context context, List<Cart> cartList) {
        this.context = context;
        this.cartList = cartList;
    }

    @Override
    public int getCount() {
        return cartList.size();
    }

    @Override
    public Object getItem(int position) {
        return cartList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    private class ViewHolder {
        ImageView imgCart;
        TextView nameCartItem, priceCartItem, quantityCartitem;
        Button btnAdd, btnSub, btnRemove;
    }

    boolean checked = false;

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.product_in_cart_item, null);
            viewHolder.imgCart = (ImageView) convertView.findViewById(R.id.imgCartItem);
            viewHolder.nameCartItem = (TextView) convertView.findViewById(R.id.txtNameCartItem);
            viewHolder.priceCartItem = (TextView) convertView.findViewById(R.id.txtPriceCartItem);
            viewHolder.quantityCartitem = (TextView) convertView.findViewById(R.id.txtQuantityCartItem);
            viewHolder.btnAdd = (Button) convertView.findViewById(R.id.btnAddCartItem);
            viewHolder.btnSub = (Button) convertView.findViewById(R.id.btnSubCartItem);
            viewHolder.btnRemove = (Button) convertView.findViewById(R.id.btnRemoveCartItem);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        Cart cart = cartList.get(position);
        viewHolder.nameCartItem.setText(cart.getProductName());
        final DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
        viewHolder.priceCartItem.setText(decimalFormat.format(cart.getProductPrice()) + " $ ");
        Picasso.get().load(cart.getUrlPic()).placeholder(R.drawable.error).error(R.drawable.errors).into(viewHolder.imgCart);
        viewHolder.quantityCartitem.setText(String.valueOf(cart.getProductQuantity()));

        int newSl = Integer.parseInt(viewHolder.quantityCartitem.getText().toString());
        if (newSl <= 1) {
            viewHolder.btnSub.setVisibility(View.INVISIBLE);
            viewHolder.btnAdd.setVisibility(View.VISIBLE);
        } else {
            viewHolder.btnSub.setVisibility(View.VISIBLE);
            viewHolder.btnAdd.setVisibility(View.VISIBLE);
        }

        viewHolder.btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                database = new Database(context);
                int newSl = Integer.parseInt(viewHolder.quantityCartitem.getText().toString()) + 1;
                int presentSl = MainActivity.listCart.get(position).getProductQuantity();
                float presentPrice = MainActivity.listCart.get(position).getProductPrice();
                MainActivity.listCart.get(position).setProductQuantity(newSl);
                float newPrice = (presentPrice * newSl) / presentSl;
                MainActivity.listCart.get(position).setProductPrice(newPrice);
                DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
                viewHolder.priceCartItem.setText(decimalFormat.format(newPrice) + " $ ");
                MyCartActi.getDataInCart();
                if (newSl > 1) {
                    viewHolder.btnSub.setVisibility(View.VISIBLE);
                }
                viewHolder.quantityCartitem.setText(String.valueOf(newSl));
                database.updateQuantityInCart(MainActivity.listCart.get(position).getProductId(), newSl, newPrice);

            }
        });

        viewHolder.btnSub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                database = new Database(context);
                int newSl = Integer.parseInt(viewHolder.quantityCartitem.getText().toString()) - 1;
                int presentSl = MainActivity.listCart.get(position).getProductQuantity();
                float presentPrice = MainActivity.listCart.get(position).getProductPrice();
                MainActivity.listCart.get(position).setProductQuantity(newSl);
                float newPrice = (presentPrice * newSl) / presentSl;
                MainActivity.listCart.get(position).setProductPrice(newPrice);
                DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
                viewHolder.priceCartItem.setText(decimalFormat.format(newPrice) + " $ ");
                MyCartActi.getDataInCart();
                if (newSl <= 1) {
                    viewHolder.btnSub.setVisibility(View.INVISIBLE);
                } else {
                    viewHolder.btnSub.setVisibility(View.VISIBLE);
                }
                viewHolder.quantityCartitem.setText(String.valueOf(newSl));
                database.updateQuantityInCart(MainActivity.listCart.get(position).getProductId(), newSl, newPrice);
            }
        });


        viewHolder.btnRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Remove item out cart!!!");
                builder.setMessage("Do you want to remove product?");
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        database = new Database(context);
                        Cart cart = MainActivity.listCart.get(position);
                        database.deleteProductInCart(MainActivity.listCart.get(position).getProductId());
                        MainActivity.listCart.remove(cart);
                        MyCartActi.getDataInCart();
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
