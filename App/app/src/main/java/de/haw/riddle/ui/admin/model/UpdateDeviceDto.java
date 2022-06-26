package de.haw.riddle.ui.admin.model;

import com.google.gson.annotations.SerializedName;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@ToString
@Getter
@AllArgsConstructor
public class UpdateDeviceDto {

    @SerializedName("puzzle")
    private final long parentPuzzle;

    @SerializedName("is_event_device")
    private final boolean isEventDevice;



}
