package server.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public enum Color implements Serializable {

    @SerializedName("red")
    RED,

    @SerializedName("blue")
    BLUE,

    @SerializedName("yellow")
    YELLOW,

    @SerializedName("white")
    WHITE,

    @SerializedName("black")
    BLACK,

    @SerializedName("purple")
    PURPLE,

    @SerializedName("green")
    GREEN
}
