package recipebox.com;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    public User findUserByEmail(String email);
    public User findUserByUserName(String name);

    Optional<User> findById(long id);
}