package test.kietpt.smartmarket.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import test.kietpt.smartmarket.R;

import test.kietpt.smartmarket.activity.CategoryListActi;


import test.kietpt.smartmarket.model.SearchHistoryDTO;
import test.kietpt.smartmarket.ulti.Database;


public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.SearchHolder>{
    Context context;
    List<SearchHistoryDTO> listSearchHistory;
    Database database;
    public SearchAdapter(Context context, List<SearchHistoryDTO> listSearchHistory, Database database) {
        this.context = context;
        this.listSearchHistory = listSearchHistory;
        this.database = database;
    }

    @NonNull
    @Override
    public SearchHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.search_items,null);
        SearchHolder searchName = new SearchHolder(view);
        return searchName;
    }



    @Override
    public void onBindViewHolder(@NonNull SearchHolder holder, int position) {
        SearchHistoryDTO searchHistoryDTO = listSearchHistory.get(position);
        holder.name.setMaxLines(1);
        holder.name.setEllipsize(TextUtils.TruncateAt.END);
        holder.name.setText(searchHistoryDTO.getSearchName());


    }

    @Override
    public int getItemCount() {
        return listSearchHistory.size();
    }

    public class SearchHolder extends RecyclerView.ViewHolder{

        public TextView name;

        public SearchHolder(final View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.txtSearch);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, CategoryListActi.class);
                    intent.putExtra("txtSearchView",listSearchHistory.get(getPosition()).getSearchName());
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);
                }
            });
            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    final AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setMessage("Do you want to delete ?");
                    builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            database.deleteItemsSearchName(listSearchHistory.get(getPosition()).getSearchName());
                            Toast.makeText(context, "Delete Successfully!!! ", Toast.LENGTH_SHORT).show();
                            itemView.setAlpha(0);
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
                    return true;
                }
            });
        }
    }

}
