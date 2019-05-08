package ro.utcn.sd.vasi.SnackOverflow.services;

import org.junit.Assert;
import org.junit.Test;
import ro.utcn.sd.vasi.SnackOverflow.data_assembler.AssemblerFactory;
import ro.utcn.sd.vasi.SnackOverflow.exceptions.QuestionNotFoundException;
import ro.utcn.sd.vasi.SnackOverflow.model.Answer;
import ro.utcn.sd.vasi.SnackOverflow.model.Question;
import ro.utcn.sd.vasi.SnackOverflow.model.User;
import ro.utcn.sd.vasi.SnackOverflow.repository.api.RepositoryFactory;
import ro.utcn.sd.vasi.SnackOverflow.repository.memory.InMemoryRepositoryFactory;

import java.time.ZonedDateTime;
import java.util.HashSet;

import static org.junit.Assert.*;

public class AnswerManagementServiceTest {

    private static RepositoryFactory createMockedFactory() {
        RepositoryFactory factory = new AssemblerFactory(new InMemoryRepositoryFactory());
        factory.createUserRepository().save(new User(null,"u1","pass",0,false,false));
        factory.createUserRepository().save(new User(null,"moderatorUser","pass",0,true,false));
        factory.createUserRepository().save(new User(null,"blockedUser","pass",0,false,true));

        factory.createQuestionRepository().save(new Question(1,"q1","q1t", ZonedDateTime.now(),new HashSet<>(),0));

        factory.createAnswerRepository().save(new Answer(null,1,"a1",ZonedDateTime.now(),1,0));
        factory.createAnswerRepository().save(new Answer(null,2,"a2",ZonedDateTime.now(),1,0));
        factory.createAnswerRepository().save(new Answer(null,3,"a3",ZonedDateTime.now(),1,0));

        return factory;
    }

    @Test
    public void listAnswersForQuestion() {
        RepositoryFactory factory = createMockedFactory();
        AnswerManagementService service = new AnswerManagementService(factory);

        Assert.assertEquals(3,service.listAnswersForQuestion(1).size());
    }

    @Test(expected = QuestionNotFoundException.class)
    public void listAnswersForQuestionThrowsNotExistingQuestion() {
        RepositoryFactory factory = createMockedFactory();
        AnswerManagementService service = new AnswerManagementService(factory);

        service.listAnswersForQuestion(999);
    }

    @Test
    public void sendVote() {
        RepositoryFactory factory = createMockedFactory();
        AnswerManagementService service = new AnswerManagementService(factory);

        assertTrue(service.sendVote(2,1,false));
        Answer answer = service.listAnswersForQuestion(1).stream().filter(a->a.getId() == 1).findFirst().get();
        assertEquals(-1,answer.getVoteCount());
    }

    @Test
    public void sendVoteSelf() {
        RepositoryFactory factory = createMockedFactory();
        AnswerManagementService service = new AnswerManagementService(factory);

        assertFalse(service.sendVote(1,1,true));
    }

    @Test
    public void addAnswer() {
        RepositoryFactory factory = createMockedFactory();
        AnswerManagementService service = new AnswerManagementService(factory);

        service.addAnswer(1,1,"asdfasdf");

        assertEquals(1,service.listAnswersForQuestion(1).stream().filter(x->x.getText().equals("asdfasdf")).count());
        assertEquals(4,service.listAnswersForQuestion(1).size());
    }
}