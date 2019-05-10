package ro.utcn.sd.vasi.SnackOverflow.event;

import lombok.Data;
import lombok.EqualsAndHashCode;
import ro.utcn.sd.vasi.SnackOverflow.dto.AnswerDTO;

@Data
@EqualsAndHashCode(callSuper = true)
public class UserBannedEvent extends BaseEvent {
    private int userId;

    public UserBannedEvent(int userId) {
        super(EventType.USER_BANNED);
        this.userId = userId;
    }
}
