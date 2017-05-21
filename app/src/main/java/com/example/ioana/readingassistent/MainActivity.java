package com.example.ioana.readingassistent;

import android.app.Dialog;
import android.content.Intent;
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
import android.widget.Button;
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
    private int bookIndex = 0;
    private int pageSize = 10;
    private int pastVisibleItems, visibileItemCount, totalItemCount;
    private boolean loading  = true;
    private String currentQuery;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RecyclerView mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        final LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new Adapter();
        mRecyclerView.setAdapter(mAdapter);

        SearchView schView = (SearchView) findViewById(R.id.sch_view);
        schView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                //startActivity(new Intent(MainActivity.this, ProgressFormular.class));
                Toast.makeText(MainActivity.this, "You are searching stuff", Toast.LENGTH_SHORT).show();
                bookIndex = 0;
                currentQuery = query;
                fetchBooks(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                visibileItemCount = mLayoutManager.getChildCount();
                totalItemCount = mLayoutManager.getItemCount();
                pastVisibleItems = mLayoutManager.findFirstVisibleItemPosition();

                if (loading) {
                    if (visibileItemCount + pastVisibleItems >= totalItemCount ){
                        loading = false;
                        bookIndex += pageSize;
                        fetchBooks(currentQuery);
                        loading = true;
                    }
                }
            }
        });



    }


    private void fetchBooks(String keyWord) {
        //SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        Call<BookList> booksCall =
                /*GoogleBooks.Service.Get().getUserRepositories(preferences.getString(Contract.Preferences.AUTH_HASH, null),
                        Contract.RepositoryActivity.AFFILIATION);
                        */
                GoogleBooks.Service.Get().getBookList(keyWord, String.valueOf(bookIndex),
                        String.valueOf(pageSize));
        booksCall.enqueue(new Callback<BookList>() {
            @Override
            public void onResponse(Call<BookList> call, Response<BookList> response) {
                if(response.isSuccessful()) {
                    List<BookModel> books = response.body().getItems();
                     mAdapter.setData(books.subList(0, pageSize));
                     mAdapter.notifyDataSetChanged();
                }
                else {
                    Toast.makeText(MainActivity.this, "An error occurred!", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call<BookList> call, Throwable t) {
                t.printStackTrace();
                Toast.makeText(MainActivity.this, "On failure error", Toast.LENGTH_SHORT).show();
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
            ((ViewHolder) holder).bind(mData.get(position));
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
                    mBooks = (LinearLayout) itemView.findViewById(R.id.books);

            }

            void bind(BookModel book) {

               mBooks.removeAllViews();
                if (book != null) {
                        TextView booksView = new TextView(itemView.getContext());
                        booksView.setText(book.getVolumeInfo().getTitle());
                        booksView.setTextColor(ContextCompat.getColor(itemView.getContext(), android.R.color.black));
                    booksView.setLayoutParams(new RecyclerView.LayoutParams(RecyclerView.LayoutParams.WRAP_CONTENT, RecyclerView.LayoutParams.WRAP_CONTENT));
                        mBooks.addView(booksView);
//                    }
                }
//
            }
        }
    }

}
