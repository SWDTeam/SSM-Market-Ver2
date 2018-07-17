package test.kietpt.smartmarket.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;
import test.kietpt.smartmarket.R;
import test.kietpt.smartmarket.activity.ProductDetailActi;
import test.kietpt.smartmarket.model.ProductDTO;
import test.kietpt.smartmarket.ulti.CheckConnection;

public class CheapProductAdapter extends RecyclerView.Adapter<CheapProductAdapter.ViewHolder>{

    private static final String TAG = "Recyclerview";
    private ArrayList<ProductDTO> listProduct;
    private Context context;

    public CheapProductAdapter(Context context,ArrayList<ProductDTO> listProduct) {
        this.listProduct = listProduct;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cheap_product_item,null);
        ViewHolder cheapProductHolder = new ViewHolder(view);
        return cheapProductHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

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

    public class ViewHolder extends RecyclerView.ViewHolder{
        CircleImageView imgProduct;
        TextView productName,productPrice;

        public ViewHolder(View itemView) {
            super(itemView);
            imgProduct = itemView.findViewById(R.id.imgCheapProduct);
            productName = itemView.findViewById(R.id.txtNameCheapProduct);
            productPrice = itemView.findViewById(R.id.txtPriceCheapProduct);
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
