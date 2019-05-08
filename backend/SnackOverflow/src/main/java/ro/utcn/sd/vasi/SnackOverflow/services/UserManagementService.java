package ro.utcn.sd.vasi.SnackOverflow.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ro.utcn.sd.vasi.SnackOverflow.exceptions.NotEnoughPermissionsException;
import ro.utcn.sd.vasi.SnackOverflow.exceptions.UserNotFoundException;
import ro.utcn.sd.vasi.SnackOverflow.model.User;
import ro.utcn.sd.vasi.SnackOverflow.model.UserData;
import ro.utcn.sd.vasi.SnackOverflow.repository.api.RepositoryFactory;

import java.util.Optional;

@Service(value = "UserlManagementServiceOld")
@RequiredArgsConstructor
public class UserManagementService {
    private final RepositoryFactory repositoryFactory;

    @Transactional
    public Optional<User> getLogin(String username, String password) {
        for(User u : repositoryFactory.createUserRepository().findAll()) {
            if(u.getUsername().equals(username)) {
                if(u.getPassword().equals(password))
                    return Optional.ofNullable(u);
                else return Optional.empty();
            }
        }

        return Optional.empty();
    }

    @Transactional
    public Optional<UserData> getUserData(int userId) {
        User optUser = repositoryFactory.createUserRepository().findById(userId).orElse(null);
        if(optUser == null) return Optional.empty();

        return Optional.of(new UserData(optUser));
    }

    @Transactional
    public void banUser(int requesterUserId, int toBeBannedUserId) {
        changeUserStatus(requesterUserId,toBeBannedUserId,true);
    }

    @Transactional
    public void changeUserStatus(int requesterUserId, int toBeBannedUserId, Boolean banStatus) {
        User requesterUser = repositoryFactory.createUserRepository().findById(requesterUserId).orElseThrow(UserNotFoundException::new);
        User toBeBannedUser = repositoryFactory.createUserRepository().findById(toBeBannedUserId).orElseThrow(UserNotFoundException::new);

        if(!requesterUser.getIsModerator()) throw new NotEnoughPermissionsException();
        if(toBeBannedUser.getIsBlocked()) return;

        toBeBannedUser.setIsBlocked(banStatus);

        repositoryFactory.createUserRepository().save(toBeBannedUser);
    }
}
