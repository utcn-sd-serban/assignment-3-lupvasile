package ro.utcn.sd.vasi.SnackOverflow.repository.jdbc;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import ro.utcn.sd.vasi.SnackOverflow.model.User;
import ro.utcn.sd.vasi.SnackOverflow.repository.api.UserRepository;
import ro.utcn.sd.vasi.SnackOverflow.repository.jdbc.JdbcGeneralRepository;


public class JdbcUserRepository extends JdbcGeneralRepository<User> implements UserRepository {

    public JdbcUserRepository(JdbcTemplate template) {
        super(template, User.class, new UserMapper());
    }
}
