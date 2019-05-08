package ro.utcn.sd.vasi.SnackOverflow.model;

import lombok.Data;

@Data
public class UserData {
    private String username;
    private int score;

    public UserData(User user) {
        this.username = user.getUsername();
        this.score = user.getScore();
    }
}
