package ro.utcn.sd.vasi.SnackOverflow.services;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ro.utcn.sd.vasi.SnackOverflow.dto.UserDTO;
import ro.utcn.sd.vasi.SnackOverflow.exceptions.NotEnoughPermissionsException;
import ro.utcn.sd.vasi.SnackOverflow.exceptions.UserNotFoundException;
import ro.utcn.sd.vasi.SnackOverflow.model.User;

import ro.utcn.sd.vasi.SnackOverflow.repository.api.RepositoryFactory;

import java.util.Collections;
import java.util.Optional;

@Service(value = "UserlManagementServiceOld")
@RequiredArgsConstructor
public class UserManagementService implements UserDetailsService {
    private final RepositoryFactory repositoryFactory;

    @Transactional
    public Optional<User> getLogin(String username, String password, PasswordEncoder passwordEncoder) {
        for(User u : repositoryFactory.createUserRepository().findAll()) {
            if(u.getUsername().equals(username)) {
                if(passwordEncoder.matches(password,u.getPassword()))
                    return Optional.ofNullable(u);
                else return Optional.empty();
            }
        }

        return Optional.empty();
    }

    @Transactional
    public UserDTO getUserDTO(int userId) {
        User user = repositoryFactory.createUserRepository().findById(userId).orElseThrow(UserNotFoundException::new);

        return UserDTO.ofEntity(user);
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

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = repositoryFactory.createUserRepository().findAll().stream().filter(u->u.getUsername().equals(username)).findAny()
                .orElseThrow(() -> new UsernameNotFoundException("Unknown user"));
        ///good place to return roles!!!
        return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(),
                Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER")));
    }
}
