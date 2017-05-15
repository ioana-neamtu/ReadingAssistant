package com.example.ioana.readingassistent.model;

import java.util.List;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by ioana on 15.05.2017.
 */

public interface GoogleBooks {

    @GET("volumes")
    Call<BookList> getBookList(@Query("q") String keyWord);

    class Service {
        private static GoogleBooks sInstace;
        public static GoogleBooks Get() {
            if (sInstace == null) {
                sInstace = new Retrofit.Builder()
                        .baseUrl("https://www.googleapis.com/books/v1/").addConverterFactory(GsonConverterFactory.create())
                        .build().create(GoogleBooks.class);
            }
            return sInstace;
        }
    }
}
