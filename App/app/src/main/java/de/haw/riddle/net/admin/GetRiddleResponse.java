package de.haw.riddle.net.admin;

import java.util.List;

import de.haw.riddle.ui.admin.model.Riddle;
import de.haw.riddle.ui.admin.model.Room;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class GetRiddleResponse {

    private final List<Riddle> puzzles;



}
