package ro.utcn.sd.vasi.SnackOverflow.event;

import lombok.Data;
import lombok.EqualsAndHashCode;
import ro.utcn.sd.vasi.SnackOverflow.dto.AnswerDTO;

@Data
@EqualsAndHashCode(callSuper = true)
public class AnswerDeletedEvent extends BaseEvent {
    private int answerId;

    public AnswerDeletedEvent(int answerId) {
        super(EventType.ANSWER_DELETED);
        this.answerId = answerId;
    }
}
