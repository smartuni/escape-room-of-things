package de.haw.riddle.ui.admin.model;

import com.google.gson.annotations.SerializedName;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@ToString
@Getter
@AllArgsConstructor
public class DeviceData {

    private final String serial;
    @SerializedName("pubkey")
    private final String publicKey;
    private final String name;



}
