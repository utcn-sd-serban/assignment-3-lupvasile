package ro.utcn.sd.vasi.SnackOverflow.dto;

import lombok.Data;
import ro.utcn.sd.vasi.SnackOverflow.model.User;

@Data
public class UserLoggedInDTO {
    private int id;
    private String username;
    private int score;
    private boolean isModerator;
    private boolean isBlocked;

    public static UserLoggedInDTO ofEntity(User user) {
        UserLoggedInDTO userLoggedInDTO = new UserLoggedInDTO();
        userLoggedInDTO.setId(user.getId());
        userLoggedInDTO.setUsername(user.getUsername());
        userLoggedInDTO.setScore(user.getScore());
        userLoggedInDTO.setModerator(user.getIsModerator());
        userLoggedInDTO.setBlocked(user.getIsBlocked());

        return userLoggedInDTO;
    }
}
