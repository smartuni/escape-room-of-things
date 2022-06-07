package de.haw.riddle.ui.admin.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Room implements Parcelable {

    private final long id;
    private String name;
    private String description;
    private String state; //TODO convert to enum or int constants as soon as we know all possible states
    @SerializedName("puzzles")
    private List<Riddle> riddles;

    protected Room(Parcel in) {
        id = in.readLong();
        name = in.readString();
        description = in.readString();
        state = in.readString();
        riddles = in.createTypedArrayList(Riddle.CREATOR);
    }

    public static final Creator<Room> CREATOR = new Creator<Room>() {
        @Override
        public Room createFromParcel(Parcel in) {
            return new Room(in);
        }

        @Override
        public Room[] newArray(int size) {
            return new Room[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeLong(id);
        parcel.writeString(name);
        parcel.writeString(description);
        parcel.writeString(state);
        parcel.writeTypedList(riddles);
    }


}
