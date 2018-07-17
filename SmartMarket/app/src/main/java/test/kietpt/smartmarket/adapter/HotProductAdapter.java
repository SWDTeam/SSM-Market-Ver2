package test.kietpt.smartmarket.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.util.ArrayList;

import test.kietpt.smartmarket.R;
import test.kietpt.smartmarket.activity.ProductDetailActi;
import test.kietpt.smartmarket.model.ProductDTO;
import test.kietpt.smartmarket.ulti.CheckConnection;

public class HotProductAdapter extends RecyclerView.Adapter<HotProductAdapter.HotProductHolder>{
    Context context;
    ArrayList<ProductDTO> listProduct;

    public HotProductAdapter(Context context, ArrayList<ProductDTO> listProduct) {
        this.context = context;
        this.listProduct = listProduct;
    }

    @NonNull
    @Override
    public HotProductHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.hot_product_item,null);
        HotProductHolder hotProductHolder = new HotProductHolder(view);
        return hotProductHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull HotProductHolder holder, int position) {
        ProductDTO productDTO = listProduct.get(position);
        holder.productName.setText(productDTO.getProductName());
        DecimalFormat format = new DecimalFormat("###,###,###");
        holder.productPrice.setText(" $ "+format.format(productDTO.getPrice()));
        Picasso.get().load(productDTO.getUrlPic()).placeholder(R.drawable.error).error(R.drawable.errors).into(holder.imgProduct);

    }

    @Override
    public int getItemCount() {
        return listProduct.size();
    }

    public class HotProductHolder extends RecyclerView.ViewHolder{
        public ImageView imgProduct;
        public TextView productName;
        public TextView productPrice;

        public HotProductHolder(View itemView) {
            super(itemView);
            imgProduct = itemView.findViewById(R.id.imgViewHotPro);
            productName = itemView.findViewById(R.id.txtNameHotPro);
            productPrice = itemView.findViewById(R.id.txtPriceHotPro);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, ProductDetailActi.class);
                    intent.putExtra("ProductInfo",listProduct.get(getPosition()));
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    CheckConnection.showConnection(context,listProduct.get(getPosition()).getProductName());
                    context.startActivity(intent);
                }
            });
        }
    }

}
