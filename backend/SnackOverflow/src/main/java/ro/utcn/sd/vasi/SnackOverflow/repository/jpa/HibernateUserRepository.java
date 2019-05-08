package ro.utcn.sd.vasi.SnackOverflow.repository.jpa;

import ro.utcn.sd.vasi.SnackOverflow.model.User;
import ro.utcn.sd.vasi.SnackOverflow.repository.api.GeneralRepository;
import ro.utcn.sd.vasi.SnackOverflow.repository.api.UserRepository;

import javax.persistence.EntityManager;

public class HibernateUserRepository extends HibernateGeneralRepository<User> implements UserRepository {
    public HibernateUserRepository(EntityManager entityManager) {
        super(entityManager, User.class);
    }
}
