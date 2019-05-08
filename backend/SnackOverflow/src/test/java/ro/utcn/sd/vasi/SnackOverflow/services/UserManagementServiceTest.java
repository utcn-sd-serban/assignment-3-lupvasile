package ro.utcn.sd.vasi.SnackOverflow.services;

import org.junit.Test;
import ro.utcn.sd.vasi.SnackOverflow.data_assembler.AssemblerFactory;
import ro.utcn.sd.vasi.SnackOverflow.exceptions.NotEnoughPermissionsException;
import ro.utcn.sd.vasi.SnackOverflow.model.Answer;
import ro.utcn.sd.vasi.SnackOverflow.model.Question;
import ro.utcn.sd.vasi.SnackOverflow.model.User;
import ro.utcn.sd.vasi.SnackOverflow.repository.api.RepositoryFactory;
import ro.utcn.sd.vasi.SnackOverflow.repository.memory.InMemoryRepositoryFactory;

import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.Optional;

import static org.junit.Assert.*;

public class UserManagementServiceTest {
    private static RepositoryFactory createMockedFactory() {
        RepositoryFactory factory = new AssemblerFactory(new InMemoryRepositoryFactory());
        factory.createUserRepository().save(new User(null,"u1","pass",0,false,false));
        factory.createUserRepository().save(new User(null,"moderatorUser","pass",0,true,false));
        factory.createUserRepository().save(new User(null,"blockedUser","pass",0,false,true));

        return factory;
    }

    @Test
    public void getLogin() {
        RepositoryFactory factory = createMockedFactory();
        UserManagementService service = new UserManagementService(factory);

        Optional<User> user = service.getLogin("u1","pass");
        assertEquals(new Integer(1),user.get().getId());
    }

    @Test
    public void getLoginInexistent() {
        RepositoryFactory factory = createMockedFactory();
        UserManagementService service = new UserManagementService(factory);

        Optional<User> user = service.getLogin("u1111","pass");
        assertFalse(user.isPresent());
    }

    @Test
    public void banUserNotModerator() {
        RepositoryFactory factory = createMockedFactory();
        UserManagementService service = new UserManagementService(factory);

        service.banUser(2,1);
        User user = service.getLogin("u1","pass").get();
        assertTrue(user.getIsBlocked());
    }

    @Test(expected = NotEnoughPermissionsException.class)
    public void banUserModerator() {
        RepositoryFactory factory = createMockedFactory();
        UserManagementService service = new UserManagementService(factory);

        service.banUser(1,2);
    }
}