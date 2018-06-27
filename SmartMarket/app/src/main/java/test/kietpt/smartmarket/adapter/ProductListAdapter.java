package test.kietpt.smartmarket.adapter;

import android.content.Context;
import android.text.Html;
import android.text.TextUtils;
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
import test.kietpt.smartmarket.model.ProductDTO;

public class ProductListAdapter extends BaseAdapter {
    Context context;
    ArrayList<ProductDTO> listProduct;

    public ProductListAdapter(Context context, ArrayList<ProductDTO> listProduct) {
        this.context = context;
        this.listProduct = listProduct;
    }

    @Override
    public int getCount() {
        return listProduct.size();
    }

    @Override
    public Object getItem(int position) {
        return listProduct.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public class ViewHolder {
        ImageView imgProductList;
        TextView nameProductList;
        TextView priceProductList;
        TextView desProductList;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.product_list_item, null);
            viewHolder.nameProductList = (TextView) convertView.findViewById(R.id.txtNameProductListItem);
            viewHolder.priceProductList = (TextView) convertView.findViewById(R.id.txtPriceProductListItem);
            viewHolder.imgProductList = (ImageView) convertView.findViewById(R.id.imgViewProductListItem);
            viewHolder.desProductList = (TextView) convertView.findViewById(R.id.txtDesProductListItem);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        ProductDTO dto = listProduct.get(position);
        viewHolder.nameProductList.setText(dto.getProductName());
        DecimalFormat format = new DecimalFormat("###,###,###");
        viewHolder.priceProductList.setText("Price: " + format.format(dto.getPrice()) + " $ ");
        viewHolder.desProductList.setMaxLines(2);
        viewHolder.desProductList.setEllipsize(TextUtils.TruncateAt.END);
        viewHolder.desProductList.setText(Html.fromHtml(dto.getDescription(),Html.FROM_HTML_MODE_COMPACT));

        Picasso.get().load(dto.getUrlPic()).placeholder(R.drawable.error).error(R.drawable.errors).into(viewHolder.imgProductList);
        return convertView;
    }
}
