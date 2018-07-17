package test.kietpt.smartmarket.adapter;

import android.content.Context;
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
import test.kietpt.smartmarket.model.AccountMenuItemDTO;
import test.kietpt.smartmarket.model.Cart;

public class AccountAdapter extends BaseAdapter{
    Context context;
    ArrayList<AccountMenuItemDTO> list;

    public AccountAdapter(Context context, ArrayList<AccountMenuItemDTO> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public class ViewHolder{
        ImageView img;
        TextView name;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if(convertView == null){
            holder = new ViewHolder();
            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.account_items, null);
            holder.img = (ImageView) convertView.findViewById(R.id.imgAccountItems);
            holder.name = (TextView)convertView.findViewById(R.id.txtAccountItems);
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }
        AccountMenuItemDTO dto = list.get(position);
        holder.img.setBackgroundResource(dto.getImgIcon());
        holder.name.setText(dto.getName());


        return convertView;
    }
}
