package de.haw.riddle.ui.admin.model;

import com.google.gson.annotations.SerializedName;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@ToString
@Getter
@AllArgsConstructor
public class UpdateRiddleDto {

    @SerializedName("name")
    private final String name;

    @SerializedName("description")
    private final String description;

    @SerializedName("room")
    private final String room;

    @SerializedName("isVictory")
    private final boolean isVictory;



}
