package test.kietpt.smartmarket.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import test.kietpt.smartmarket.R;
import test.kietpt.smartmarket.model.CategoryDTO;

public class CategoryAdapter extends BaseAdapter {
    Context context;
    int layout;
    List<CategoryDTO> categoryDTOList;

    public CategoryAdapter(Context context, List<CategoryDTO> categoryDTOList) {
        this.context = context;
        this.categoryDTOList = categoryDTOList;
    }

    @Override
    public int getCount() {
        return categoryDTOList.size();
    }

    @Override
    public Object getItem(int position) {
        return categoryDTOList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public class ViewHolder {
        ImageView urlPic;
        TextView categoryName;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.category_list_item, null);
            viewHolder.categoryName = (TextView) convertView.findViewById(R.id.cateName);
            viewHolder.urlPic = (ImageView) convertView.findViewById(R.id.cateImg);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();

        }
        CategoryDTO categoryDTO = categoryDTOList.get(position);
        viewHolder.categoryName.setText(categoryDTO.getCateName().toString());
//        Log.e("kiet dap chai",categoryDTO.getCateId() + " - "+categoryDTO.getImgPic());
//        if(categoryDTO.getImgPic().equals("https://ssm-market.herokuapp.com/uploads/image/url/1/coca.jpg")){
//            categoryDTO.setImgPic("http://southeasternbeers.co.uk/291-thickbox_default/330ml-coke-icon.jpg");
//            Log.e("test123123456 = ",categoryDTO.getImgPic());
//
//        }else if(categoryDTO.getImgPic().equals("https://ssm-market.herokuapp.com/uploads/image/url/2/7up.jpg")){
//            categoryDTO.setImgPic("http://muaban247.top/image/cache/catalog/7up-500x539.jpg");
//            Log.e("test123123456 = ",categoryDTO.getImgPic());
//        }
        Picasso.get().load(categoryDTO.getImgPic()).placeholder(R.drawable.error).
                error(R.drawable.errors).into(viewHolder.urlPic);
        return convertView;
    }
}
