package com.example.ioana.readingassistent.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Dimensions {

@SerializedName("height")
@Expose
private String height;

public String getHeight() {
return height;
}

public void setHeight(String height) {
this.height = height;
}

}