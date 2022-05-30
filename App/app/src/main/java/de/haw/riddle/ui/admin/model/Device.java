package de.haw.riddle.ui.admin.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.haw.riddle.ui.admin.device.DeviceFragment;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

@Data
@AllArgsConstructor
public class Device implements Parcelable {

    private final long id;
    private List<String>resources;
    private String name;


    protected Device(Parcel in) {
        id= in.readLong();
        resources = in.createStringArrayList();
        name= in.readString();

    }

    public static final Creator<Device> CREATOR = new Creator<Device>() {
        @Override
        public Device createFromParcel(Parcel in) {
            return new Device(in);
        }

        @Override
        public Device[] newArray(int size) {return new Device[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeLong(id);
        parcel.writeStringList(resources);
        parcel.writeString(name);
    }

    public boolean isValid() {
        return name != null && !name.isEmpty();
    }
}
