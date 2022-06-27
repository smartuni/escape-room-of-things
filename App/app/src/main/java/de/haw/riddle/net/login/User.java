package de.haw.riddle.net.login;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class User {

    private final String username;
    private final String password;
}
