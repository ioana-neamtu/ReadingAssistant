package com.example.ioana.readingassistent.model;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class BookList {

@SerializedName("kind")
@Expose
private String kind;
@SerializedName("totalItems")
@Expose
private Integer totalItems;
@SerializedName("items")
@Expose
private List<BookModel> items = null;

public String getKind() {
return kind;
}

public void setKind(String kind) {
this.kind = kind;
}

public Integer getTotalItems() {
return totalItems;
}

public void setTotalItems(Integer totalItems) {
this.totalItems = totalItems;
}

public List<BookModel> getItems() {
return items;
}

public void setItems(List<BookModel> items) {
this.items = items;
}

}