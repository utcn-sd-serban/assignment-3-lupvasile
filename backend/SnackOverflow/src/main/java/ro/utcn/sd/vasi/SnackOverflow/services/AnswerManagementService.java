package ro.utcn.sd.vasi.SnackOverflow.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ro.utcn.sd.vasi.SnackOverflow.exceptions.AnswerNotFoundException;
import ro.utcn.sd.vasi.SnackOverflow.exceptions.NotEnoughPermissionsException;
import ro.utcn.sd.vasi.SnackOverflow.exceptions.QuestionNotFoundException;
import ro.utcn.sd.vasi.SnackOverflow.exceptions.UserNotFoundException;
import ro.utcn.sd.vasi.SnackOverflow.model.*;
import ro.utcn.sd.vasi.SnackOverflow.repository.api.RepositoryFactory;

import java.time.ZonedDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service(value = "AnswerManagementServiceOld")
@RequiredArgsConstructor
public class AnswerManagementService {
    private final RepositoryFactory repositoryFactory;

    @Transactional
    public List<Answer> listAnswersForQuestion(int questionId) {
        /*Question question = repositoryFactory.createQuestionRepository().findById(questionId).orElse(null);
        if(question == null) {
            return new ArrayList<>();
        }*/
        Question question = repositoryFactory.createQuestionRepository().findById(questionId).orElseThrow(QuestionNotFoundException::new);

        return repositoryFactory.createAnswerRepository().findAllbyQuestionId(questionId).stream().sorted(Comparator.comparing(Answer::getVoteCount).reversed()).collect(Collectors.toList());
    }

    @Transactional
    public boolean sendVote(int userId, int answerId, boolean value) {
        VoteAnswer currVote = repositoryFactory.createAnswerVoteRepository().findVoteFromUserForPost(userId, answerId).orElse(null);
        Answer answer = repositoryFactory.createAnswerRepository().findById(answerId).orElseThrow(AnswerNotFoundException::new);

        if(currVote != null && currVote.isValue() == value) return false;
        if(userId == answer.getAuthorId()) return false;

        if(currVote == null) currVote = new VoteAnswer(null,userId,answer.getAuthorId(),answerId,value);
        currVote.setValue(value);

        repositoryFactory.createAnswerVoteRepository().save(currVote);
        return true;
    }

    @Transactional
    public Answer addAnswer(int userId, int questionId, String text) {
        User user = repositoryFactory.createUserRepository().findById(userId).orElseThrow(UserNotFoundException::new);
        if(user.getIsBlocked()) return null;

        Question question = repositoryFactory.createQuestionRepository().findById(questionId).orElseThrow(QuestionNotFoundException::new);

        return repositoryFactory.createAnswerRepository().save(new Answer(userId,text,ZonedDateTime.now(),questionId,0));
    }

    @Transactional
    public void updateAnswerText(int userId, int answerId, String newText) {
        User user = repositoryFactory.createUserRepository().findById(userId).orElseThrow(UserNotFoundException::new);
        Answer answer = repositoryFactory.createAnswerRepository().findById(answerId).orElseThrow(AnswerNotFoundException::new);

        if(!answer.getAuthorId().equals(user.getId()) && !user.getIsModerator()) throw new NotEnoughPermissionsException();

        answer.setText(newText);
        repositoryFactory.createAnswerRepository().save(answer);
    }

    @Transactional
    public void deleteAnswer(int userId, int answerId) {
        User user = repositoryFactory.createUserRepository().findById(userId).orElseThrow(UserNotFoundException::new);
        Answer answer = repositoryFactory.createAnswerRepository().findById(answerId).orElseThrow(AnswerNotFoundException::new);

        if(!answer.getAuthorId().equals(user.getId()) && !user.getIsModerator()) throw new NotEnoughPermissionsException();

        repositoryFactory.createAnswerRepository().remove(answer);
    }
}
