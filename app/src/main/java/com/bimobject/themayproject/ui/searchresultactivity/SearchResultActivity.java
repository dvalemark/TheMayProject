package com.bimobject.themayproject.ui.searchresultactivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.bimobject.themayproject.constants.URL;
import com.bimobject.themayproject.dto.Product;
import com.bimobject.themayproject.adapters.ProductListAdapter;
import com.bimobject.themayproject.R;
import com.bimobject.themayproject.helpers.RequestService;

import java.util.ArrayList;
import java.util.List;

public class SearchResultActivity extends AppCompatActivity {

    private static int page;
    private static ProductListAdapter adapter;
    private static String search;
    private LoadListItemsTask loadListItemsTask = new LoadListItemsTask();
    private ListView listView;
    private String category = "137";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_result);
        page = 1;

        if (getIntent().hasExtra("search")) {
            search = getIntent().getStringExtra("search");
            EditText searchBox = findViewById(R.id.searchBoxTopBar);
            searchBox.setText(search);
            System.out.println(search);
        }

        adapter = new ProductListAdapter(SearchResultActivity.this, R.layout.list_item_layout, new ArrayList<Product>());
        listView = findViewById(R.id.activity_search_result_lv_list);
        listView.setAdapter(adapter);

        loadListItemsTask.execute(search, category);

        Button buttonFilter = findViewById(R.id.activity_serch_result_btn_filter);
        buttonFilter.setOnClickListener(view -> {
            EditText searchBox = view.getRootView().findViewById(R.id.searchBoxTopBar);
            String topBarSearch = searchBox.getText().toString();

            if (isTaskFinished()) {
                getAdapter().clear();
                setPage(1);
                setSearch(topBarSearch);
                setLoadListItemsTask(createNewTask());
                getLoadListItemsTask().execute(topBarSearch, category);
            }
        });

        listView.setOnScrollListener(new AbsListView.OnScrollListener() {

            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

                if (isTaskFinished() && view.getLastVisiblePosition() == totalItemCount - 1) {
                    loadListItemsTask = new LoadListItemsTask();
                    loadListItemsTask.execute(search);
                }
            }
        });
    }


    public class LoadListItemsTask extends AsyncTask<String, String, List<Product>> {

        public LoadListItemsTask() {
            super();
        }

        @Override
        protected void onPreExecute() {
            if (adapter.getCount() != 0) {
                Toast.makeText(SearchResultActivity.this, "Fetching more products..", Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        protected void onPostExecute(List<Product> products) {

            adapter.addAll(products);
            adapter.notifyDataSetChanged();

        }


        @Override
        protected List<Product>doInBackground(String... strings){
            return RequestService.getRequest(strings[0], URL.GET_PRODUCTS, page++, strings[1]);

            
        }
    }

    public LoadListItemsTask getLoadListItemsTask() {
        return loadListItemsTask;
    }

    public void setLoadListItemsTask(LoadListItemsTask loadListItemsTask) {
        this.loadListItemsTask = loadListItemsTask;
    }

    public ProductListAdapter getAdapter() {
        return adapter;
    }

    public boolean isTaskFinished() {
        return loadListItemsTask.getStatus() == AsyncTask.Status.FINISHED;
    }

    public LoadListItemsTask createNewTask() {
        return new LoadListItemsTask();
    }

    public void setPage(int page) {
        SearchResultActivity.page = page;
    }

    public void setSearch(String search) {
        SearchResultActivity.search = search;
    }
}
