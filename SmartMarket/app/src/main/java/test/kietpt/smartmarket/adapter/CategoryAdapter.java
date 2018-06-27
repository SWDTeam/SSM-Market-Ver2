package test.kietpt.smartmarket.adapter;

import android.content.Context;
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
        Picasso.get().load(categoryDTO.getImgPic()).placeholder(R.drawable.address).
                error(R.drawable.backincon).into(viewHolder.urlPic);

        return convertView;
    }
}
