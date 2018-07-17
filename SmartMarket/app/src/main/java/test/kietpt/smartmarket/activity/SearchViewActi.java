package test.kietpt.smartmarket.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import test.kietpt.smartmarket.R;
import test.kietpt.smartmarket.adapter.SearchAdapter;
import test.kietpt.smartmarket.model.SearchHistoryDTO;
import test.kietpt.smartmarket.ulti.Database;

public class SearchViewActi extends AppCompatActivity {

    Toolbar toolbar;
    SearchView txtSearch;
    Button btnSearch;
    RecyclerView recyclerView;
    List<SearchHistoryDTO> listSearchHisory;
    SearchAdapter adapter;
    Database database;
    TextView imgRemove;
    LinearLayout layoutListSearchName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_view);
        reflect();
        getListSearchedName();
        actionToolbar();
        searchCategory();
        removeSearchedName();
    }

    private void reflect() {
        database = new Database(this);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerSearchView);
        imgRemove = (TextView) findViewById(R.id.imgRemoveSearchName);
        toolbar = (Toolbar) findViewById(R.id.toolbarSearchView);
        txtSearch = (SearchView) findViewById(R.id.txtSearchView);
        btnSearch = (Button) findViewById(R.id.btnSearch);
        layoutListSearchName = (LinearLayout)findViewById(R.id.layoutListSearchName);



    }
    private void getListSearchedName(){
        listSearchHisory = database.getListBySearchName();
        if(listSearchHisory.size() ==0){
            layoutListSearchName.setVisibility(View.INVISIBLE);
        }
        recyclerView.setAlpha(1);
        adapter = new SearchAdapter(this, listSearchHisory, database);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 3));
        recyclerView.setAdapter(adapter);
    }
    private void searchCategory() {
        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String search = txtSearch.getQuery().toString();
                if (search.equals("") || search.length() == 0 || search == null) {
                    Toast.makeText(SearchViewActi.this, "Please input something to search category", Toast.LENGTH_SHORT).show();
                } else {
                    if (!database.checkSearchNameInHistory(search)) {
                        database.insertToSearchHistory(search);
                    }
                    Intent intent = new Intent(SearchViewActi.this, CategoryListActi.class);
                    intent.putExtra("txtSearchView", search);
                    startActivity(intent);
                }
            }
        });
    }

    private void actionToolbar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }


    private void removeSearchedName() {
        imgRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                database.deleteAllSearchName(listSearchHisory);
                listSearchHisory = null;
                recyclerView.setAlpha(0);

                    layoutListSearchName.setVisibility(View.INVISIBLE);

                adapter.notifyDataSetChanged();
                Toast.makeText(SearchViewActi.this, "Delete Successfully", Toast.LENGTH_SHORT).show();

            }
        });
    }


}
