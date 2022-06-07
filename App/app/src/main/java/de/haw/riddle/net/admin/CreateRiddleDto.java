package de.haw.riddle.net.admin;

import com.google.gson.annotations.SerializedName;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CreateRiddleDto {

    private final String name;
    private final String description;
    @SerializedName("room")
    private final long roomId;




        public boolean isValid() {
            return name != null && !name.isEmpty() && roomId >= 0;

        }

}
