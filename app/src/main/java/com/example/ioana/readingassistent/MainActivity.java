package com.example.ioana.readingassistent;

import android.app.Dialog;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ioana.readingassistent.model.BookList;
import com.example.ioana.readingassistent.model.BookModel;
import com.example.ioana.readingassistent.model.GoogleBooks;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    private Adapter mAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new Adapter();
        recyclerView.setAdapter(mAdapter);

        SearchView schView = (SearchView) findViewById(R.id.sch_view);
       schView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Toast.makeText(MainActivity.this, "You are searching stuff", Toast.LENGTH_SHORT).show();
                fetchBooks(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

    }


    private void fetchBooks(String keyWord) {
        //SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        Call<BookList> booksCall =
                /*GoogleBooks.Service.Get().getUserRepositories(preferences.getString(Contract.Preferences.AUTH_HASH, null),
                        Contract.RepositoryActivity.AFFILIATION);
                        */
                GoogleBooks.Service.Get().getBookList(keyWord);
        booksCall.enqueue(new Callback<BookList>() {
            @Override
            public void onResponse(Call<BookList> call, Response<BookList> response) {
                if(response.isSuccessful()) {
                    List<BookModel> books = response.body().getItems();
                     mAdapter.setData(books);
                     mAdapter.notifyDataSetChanged();
                }
                else {
                    Toast.makeText(MainActivity.this, "An error occurred!", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call<BookList> call, Throwable t) {
                t.printStackTrace();
                Toast.makeText(MainActivity.this, "No Internet connection", Toast.LENGTH_SHORT).show();
            }
        });

    }


    private static class Adapter extends RecyclerView.Adapter {
        List<BookModel> mData;

        public void setData(List<BookModel> data) {
            mData = data;
        }
        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.book_view, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            ((ViewHolder) holder).bind(mData);
        }


        @Override
        public int getItemCount() {
            return mData != null ? mData.size() : 0;
        }

        static class ViewHolder extends RecyclerView.ViewHolder {
//            private final TextView mTitle;
            private final LinearLayout mBooks;

            ViewHolder(View itemView) {
                super(itemView);

                //  Cache all the views we will need when binding the model
//                mTitle = (TextView) itemView.findViewById(R.id.book_title);
                    mBooks = (LinearLayout) itemView.findViewById(R.id.books);
            }

            void bind(List<BookModel> books) {
                //  The views are cached, just set the data
//                mTitle.setText(book.getVolumeInfo().getTitle());
                mBooks.removeAllViews();
                if (books != null) {
                    for (BookModel book : books) {
                        TextView booksView = new TextView(itemView.getContext());
                        booksView.setText(book.getVolumeInfo().getTitle());
                        booksView.setTextColor(ContextCompat.getColor(itemView.getContext(), android.R.color.black));
                        mBooks.addView(booksView);
                    }
                }
//
            }
        }
    }

}
