package de.haw.riddle.ui.admin.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Device implements Parcelable {

    private String description;
    private long devIP;
    private final long id;
    @SerializedName("is_event_device")
    private boolean isEventDevice;
    private String name;
    @SerializedName("node_state")
    private String nodeState;
    @SerializedName("pubkey")
    private String publicKey;
    @SerializedName("puzzle")
    private long parentPuzzleId;
    private String serial;
    private String state;


    protected Device(Parcel in) {
        description = in.readString();
        devIP = in.readLong();
        id = in.readLong();
        isEventDevice = in.readByte() != 0;
        name = in.readString();
        nodeState = in.readString();
        publicKey = in.readString();
        parentPuzzleId = in.readLong();
        serial = in.readString();
        state = in.readString();
    }

    public static final Creator<Device> CREATOR = new Creator<Device>() {
        @Override
        public Device createFromParcel(Parcel in) {
            return new Device(in);
        }

        @Override
        public Device[] newArray(int size) {
            return new Device[size];
        }
    };

    public boolean isValid() {
        return name != null && !name.isEmpty();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(description);
        parcel.writeLong(devIP);
        parcel.writeLong(id);
        parcel.writeByte((byte) (isEventDevice ? 1 : 0));
        parcel.writeString(name);
        parcel.writeString(nodeState);
        parcel.writeString(publicKey);
        parcel.writeLong(parentPuzzleId);
        parcel.writeString(serial);
        parcel.writeString(state);
    }
}
