package ro.utcn.sd.vasi.SnackOverflow.services;

import lombok.RequiredArgsConstructor;
import org.aspectj.weaver.ast.Not;
import org.springframework.stereotype.Service;
import ro.utcn.sd.vasi.SnackOverflow.exceptions.NotEnoughPermissionsException;
import ro.utcn.sd.vasi.SnackOverflow.exceptions.NotEnoughTagsException;
import ro.utcn.sd.vasi.SnackOverflow.exceptions.QuestionNotFoundException;
import ro.utcn.sd.vasi.SnackOverflow.exceptions.UserNotFoundException;
import ro.utcn.sd.vasi.SnackOverflow.model.*;
import ro.utcn.sd.vasi.SnackOverflow.repository.api.RepositoryFactory;

import org.springframework.transaction.annotation.Transactional;

import javax.persistence.QueryTimeoutException;
import java.time.ZonedDateTime;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;


@Service(value = "QuestionManagementServiceOld")
@RequiredArgsConstructor
public class QuestionManagementService {
    private final RepositoryFactory repositoryFactory;

    @Transactional
    public List<Question> listQuestions() {
        return repositoryFactory.createQuestionRepository().findAll().stream().sorted(Comparator.comparing(Question::getCreationDateTime).reversed()).collect(Collectors.toList());
    }


    @Transactional
    public List<Question> filterQuestionsByTag(Set<Tag> tags) {
        return repositoryFactory.createQuestionRepository().findAll().stream()
                .filter(x->x.getTags().containsAll(tags))
                .sorted(Comparator.comparing(Question::getCreationDateTime).reversed()).collect(Collectors.toList());
    }

    @Transactional
    public List<Question> filterQuestionsByTitle(String titlePattern) {
        return repositoryFactory.createQuestionRepository().findAll().stream()
                .filter(x->x.getTitle().contains(titlePattern))
                .sorted(Comparator.comparing(Question::getCreationDateTime).reversed()).collect(Collectors.toList());
    }

    /**
     * I suppose all the time that the userId is valid
     * @param userId
     * @param title
     * @param text
     * @param tags
     * @return
     */
    @Transactional
    public Question addQuestion(int userId, String title, String text, Set<Tag> tags) {
        User user = repositoryFactory.createUserRepository().findById(userId).orElseThrow(UserNotFoundException::new);
        if(user.getIsBlocked()) throw new NotEnoughPermissionsException();
        if(tags.isEmpty()) throw new NotEnoughTagsException();

        tags = saveQuestionTags(tags);
        return repositoryFactory.createQuestionRepository().save(new Question(userId,title,text, ZonedDateTime.now(), tags,0));
    }

    @Transactional
    public boolean sendVote(int userId, int questionId, boolean value) {
        VoteQuestion currVote = repositoryFactory.createQuestionVoteRepository().findVoteFromUserForPost(userId, questionId).orElse(null);
        Question question = repositoryFactory.createQuestionRepository().findById(questionId).orElseThrow(QuestionNotFoundException::new);

        if(currVote != null && currVote.isValue() == value) return false;
        if(userId == question.getAuthorId()) return false;

        if(currVote == null) currVote = new VoteQuestion(null,userId,question.getAuthorId(),questionId,value);
        currVote.setValue(value);

        repositoryFactory.createQuestionVoteRepository().save(currVote);
        return true;
    }

    @Transactional
    public Set<Tag> listAllTags() {
        return repositoryFactory.createTagRepository().findAll().stream().collect(Collectors.toSet());
    }

    @Transactional
    public void updateQuestion(int userId, int questionId, String newTitle, String newText) {
        User user = repositoryFactory.createUserRepository().findById(userId).orElseThrow(UserNotFoundException::new);
        Question question = repositoryFactory.createQuestionRepository().findById(questionId).orElseThrow(QueryTimeoutException::new);

        if(!user.getIsModerator()) throw new NotEnoughPermissionsException();

        question.setTitle(newTitle);
        question.setText(newText);

        repositoryFactory.createQuestionRepository().save(question);
    }

    @Transactional
    public void deleteQuestion(int userId, int questionId) {
        User user = repositoryFactory.createUserRepository().findById(userId).orElseThrow(UserNotFoundException::new);
        Question question = repositoryFactory.createQuestionRepository().findById(questionId).orElseThrow(QuestionNotFoundException::new);

        if(!question.getAuthorId().equals(user.getId()) && !user.getIsModerator()) throw new NotEnoughPermissionsException();

        repositoryFactory.createQuestionRepository().remove(question);
    }

    @Transactional
    public Question getQuestion(int questionId){
        return repositoryFactory.createQuestionRepository().findById(questionId).orElseThrow(QuestionNotFoundException::new);
    }

    @Transactional
    protected Set<Tag> saveQuestionTags(Set<Tag> tags) {
        Set<Tag> existingTags = new HashSet<Tag>(repositoryFactory.createTagRepository().findAll());
        Set<Tag> newTags = new HashSet<>(tags);
        newTags.removeAll(existingTags);

        newTags.forEach(t->{
            t.setId(null);
            repositoryFactory.createTagRepository().save(t);});

        newTags = new HashSet<Tag>(repositoryFactory.createTagRepository().findAll());
        newTags.retainAll(tags);
        return newTags;
    }
}
