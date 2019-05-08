package ro.utcn.sd.vasi.SnackOverflow.repository.memory;

import ro.utcn.sd.vasi.SnackOverflow.model.User;
import ro.utcn.sd.vasi.SnackOverflow.repository.api.UserRepository;

public class InMemoryUserRepository extends InMemoryGeneralRepository<User> implements UserRepository {
    public InMemoryUserRepository(InMemoryRepositoryFactory factory) {
        super(factory);
    }
}
