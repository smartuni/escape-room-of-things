package de.haw.riddle.net;

import com.google.gson.annotations.SerializedName;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@ToString
@Getter
@RequiredArgsConstructor
public class ApiResponse<T> {

    @SerializedName(value = "rooms",alternate = {"devices","puzzles"})

    private final T data;
}
