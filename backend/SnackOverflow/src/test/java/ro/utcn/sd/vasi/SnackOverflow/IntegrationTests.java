package ro.utcn.sd.vasi.SnackOverflow;

import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.data.jpa.repository.support.JpaRepositoryFactory;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.junit4.SpringRunner;
import ro.utcn.sd.vasi.SnackOverflow.exceptions.NotEnoughPermissionsException;
import ro.utcn.sd.vasi.SnackOverflow.exceptions.QuestionNotFoundException;
import ro.utcn.sd.vasi.SnackOverflow.model.Answer;
import ro.utcn.sd.vasi.SnackOverflow.model.Question;
import ro.utcn.sd.vasi.SnackOverflow.model.User;
import ro.utcn.sd.vasi.SnackOverflow.repository.api.RepositoryFactory;
import ro.utcn.sd.vasi.SnackOverflow.repository.data.DataRepositoryFactory;
import ro.utcn.sd.vasi.SnackOverflow.repository.jdbc.JdbcRepositoryFactory;
import ro.utcn.sd.vasi.SnackOverflow.repository.jdbc.JdbcTagRepository;
import ro.utcn.sd.vasi.SnackOverflow.repository.memory.InMemoryRepositoryFactory;
import ro.utcn.sd.vasi.SnackOverflow.seed.ProgramSeed;
import ro.utcn.sd.vasi.SnackOverflow.services.AnswerManagementService;
import ro.utcn.sd.vasi.SnackOverflow.services.QuestionManagementService;
import ro.utcn.sd.vasi.SnackOverflow.services.UserManagementService;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.Optional;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
public class IntegrationTests {

    @Autowired
    private AnswerManagementService answerManagementService;
    @Autowired
    private QuestionManagementService questionManagementService;
    @Autowired
    private UserManagementService userManagementService;
    @Autowired
    private RepositoryFactory factory;

    private static boolean firstProgramSeed = true;

    @Before
    public void addProgramSeed() {
        if(!firstProgramSeed) return;
        factory.createUserRepository().save(new User(null,"u1","pass",0,false,false));
        factory.createUserRepository().save(new User(null,"moderatorUser","pass",0,true,false));
        factory.createUserRepository().save(new User(null,"blockedUser","pass",0,false,true));

        factory.createQuestionRepository().save(new Question(1,"q1","q1t", ZonedDateTime.now(),new HashSet<>(),0));

        factory.createAnswerRepository().save(new Answer(null,1,"a1",ZonedDateTime.now(),1,0));
        factory.createAnswerRepository().save(new Answer(null,2,"a2",ZonedDateTime.now(),1,0));
        factory.createAnswerRepository().save(new Answer(null,3,"a3",ZonedDateTime.now(),1,0));

        firstProgramSeed = false;
    }

    @Test
    public void getLogin() {
        UserManagementService service = userManagementService;

        Optional<User> user = service.getLogin("u1","pass");
        assertEquals(new Integer(1),user.get().getId());
    }

    @Test
    public void getLoginInexistent() {
        UserManagementService service = userManagementService;

        Optional<User> user = service.getLogin("u1111","pass");
        assertFalse(user.isPresent());
    }

    @Test
    public void banUserNotModerator() {
        UserManagementService service = userManagementService;

        service.banUser(2,1);
        User user = service.getLogin("u1","pass").get();
        assertTrue(user.getIsBlocked());
    }

    @Test(expected = NotEnoughPermissionsException.class)
    public void banUserModerator() {
        UserManagementService service = userManagementService;

        service.banUser(1,2);
    }

    @Test
    public void listAnswersForQuestion() {
        AnswerManagementService service = answerManagementService;

        Assert.assertEquals(3,service.listAnswersForQuestion(1).size());
    }

    @Test(expected = QuestionNotFoundException.class)
    public void listAnswersForQuestionThrowsNotExistingQuestion() {

        AnswerManagementService service = answerManagementService;

        service.listAnswersForQuestion(999);
    }

    @Test
    public void sendVote() {

        AnswerManagementService service = answerManagementService;

        assertTrue(service.sendVote(2,1,false));
        Answer answer = service.listAnswersForQuestion(1).stream().filter(a->a.getId() == 1).findFirst().get();
        assertEquals(-1,answer.getVoteCount());
    }

    @Test
    public void sendVoteSelf() {

        AnswerManagementService service = answerManagementService;

        assertFalse(service.sendVote(1,1,true));
    }

    @Test
    public void addAnswer() {

        AnswerManagementService service = answerManagementService;

        service.addAnswer(1,1,"asdfasdf");

        assertEquals(1,service.listAnswersForQuestion(1).stream().filter(x->x.getText().equals("asdfasdf")).count());
        assertEquals(4,service.listAnswersForQuestion(1).size());

        service.deleteAnswer(1,4);
    }
}
