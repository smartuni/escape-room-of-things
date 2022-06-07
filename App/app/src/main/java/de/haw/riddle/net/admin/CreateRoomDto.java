package de.haw.riddle.net.admin;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CreateRoomDto {

    private final String name;
    private final String description;



        public boolean isValid() {
            return name != null && !name.isEmpty();
        }

}
