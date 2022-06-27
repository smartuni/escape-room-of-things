package de.haw.riddle.ui.admin.model;

import com.google.gson.annotations.SerializedName;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@ToString
@Getter
@AllArgsConstructor
public class UpdateStateDto {

    @SerializedName("state")

    private final String state;




}
