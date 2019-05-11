package ro.utcn.sd.vasi.SnackOverflow.controller.rest;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ro.utcn.sd.vasi.SnackOverflow.services.AnswerManagementService;
import ro.utcn.sd.vasi.SnackOverflow.services.QuestionManagementService;
import ro.utcn.sd.vasi.SnackOverflow.services.UserManagementService;

@RestController
@RequiredArgsConstructor
public class VoteController {
    private final AnswerManagementService answerManagementService;
    private final QuestionManagementService questionManagementService;
    private final UserManagementService userManagementService;

    //is put the appropiate method here?
    @PutMapping("/questions/{questionId}/answers/{answerId}/votes")
    public void voteAnswer(@PathVariable int answerId, @RequestBody int voteValue) {
        boolean send = voteValue == 1;
        answerManagementService.sendVote(userManagementService.loadCurrentUser().getId(),answerId,send);
    }

    @PutMapping("/questions/{questionId}/votes")
    private void voteQuestion(@PathVariable int questionId, @RequestBody int voteValue) {
        boolean send = voteValue == 1;
        questionManagementService.sendVote(userManagementService.loadCurrentUser().getId(),questionId,send);
    }
}
