package de.haw.riddle.ui.admin.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Riddle implements Parcelable {
    private final long id;
    private String name;
    private List<Device> devices;


    protected Riddle(Parcel in) {
        name = in.readString();
        devices=in.createTypedArrayList(Device.CREATOR);
        id=in.readLong();
    }

    public static final Creator<Riddle> CREATOR = new Creator<Riddle>() {
        @Override
        public Riddle createFromParcel(Parcel in) {
            return new Riddle(in);
        }

        @Override
        public Riddle[] newArray(int size) {
            return new Riddle[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(name);
        parcel.writeTypedList(devices);
        parcel.writeLong(id);
    }

    public boolean isValid() {
        return name != null && !name.isEmpty();
    }
}
