package com.bimobject.themayproject.ui.searchresultactivity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;

import com.bimobject.themayproject.adapters.RecycleViewAdapter;
import com.bimobject.themayproject.R;
import com.bimobject.themayproject.helpers.OnRecycleViewItemClickListener;
import com.bimobject.themayproject.helpers.Request;
import com.bimobject.themayproject.ui.productinfoactivity.ProductInfoActivity;

public class SearchResultActivity extends AppCompatActivity {

    private static RecycleViewAdapter adapter;
    private static String search;
    private RecyclerView recyclerView;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_result);

        if(getIntent().hasExtra("search")){
            search = getIntent().getStringExtra("search");
        }

        Request request = new Request();
        request.addSearch(search);


        Button buttonCategory = findViewById(R.id.activity_serch_result_btn_filter);
        buttonCategory.setOnClickListener(view -> {

            request.addCategory("137");
            adapter.makeNewRequest(request);

        });

        Button btnClear = findViewById(R.id.activity_search_result_btn_filter_clear);
        btnClear.setOnClickListener(view -> {
            request.clearParams();
            adapter.makeNewRequest(request);
        });


        adapter = new RecycleViewAdapter(getApplicationContext());
        recyclerView = findViewById(R.id.activity_search_result_rv_list);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

        adapter.setOnItemClickListener(new OnRecycleViewItemClickListener() {
            @Override
            public void onItemClick(View view, int position, String productId) {
                Intent intent = new Intent(SearchResultActivity.this, ProductInfoActivity.class);
                intent.putExtra("productId", productId);
                startActivity(intent);
            }
        });

        adapter.makeNewRequest(request);


    }


}