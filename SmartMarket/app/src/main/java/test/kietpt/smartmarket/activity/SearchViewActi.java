package test.kietpt.smartmarket.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import test.kietpt.smartmarket.R;

public class SearchViewActi extends AppCompatActivity {

    Toolbar toolbar;
    EditText txtSearch;
    Button btnSearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_view);
        reflect();
        actionToolbar();
        searchCategory();
    }


    private void searchCategory() {
        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String search = txtSearch.getText().toString();
                if (search.equals("") || search.length() == 0 || search == null) {
                    Toast.makeText(SearchViewActi.this, "Please input something to search category", Toast.LENGTH_SHORT).show();
                } else {
                    Intent intent = new Intent(getApplicationContext(), CategoryListActi.class);
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

    private void reflect() {
        toolbar = (Toolbar) findViewById(R.id.toolbarSearchView);
        txtSearch = (EditText) findViewById(R.id.txtSearchView);
        btnSearch = (Button) findViewById(R.id.btnSearch);
    }


}
