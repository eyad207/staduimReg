package en.staduimreg.repository;

import en.staduimreg.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUsername(String username);

    Optional<User> findByEmail(String email);

    boolean existsByUsername(String username);

    boolean existsByEmail(String email);

    List<User> findByRole(User.Role role);

    @Query("SELECT u FROM User u ORDER BY u.createdAt DESC")
    List<User> findTop10ByOrderByCreatedAtDesc();

    @Query("SELECT COUNT(u) FROM User u WHERE u.role = ?1")
    long countByRole(User.Role role);

    @Query("SELECT u FROM User u WHERE u.firstName LIKE %?1% OR u.lastName LIKE %?1% OR u.username LIKE %?1% OR u.email LIKE %?1%")
    List<User> searchUsers(String searchTerm);
}
