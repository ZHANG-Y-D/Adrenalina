package adrenaline;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public enum Color implements Serializable {

    @SerializedName("RED")
    RED,

    @SerializedName("BLUE")
    BLUE,

    @SerializedName("YELLOW")
    YELLOW,

    @SerializedName("WHITE")
    WHITE,

    @SerializedName("BLACK")
    BLACK,

    @SerializedName("PURPLE")
    PURPLE,

    @SerializedName("GREEN")
    GREEN,

    @SerializedName("GRAY")
    GRAY
}
