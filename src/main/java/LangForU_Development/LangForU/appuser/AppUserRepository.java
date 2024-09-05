package LangForU_Development.LangForU.appuser;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
@Transactional(readOnly = true)
public interface AppUserRepository extends JpaRepository<AppUser, Long> {

    Optional<AppUser> findByEmail(String email);

    @Transactional
    @Modifying
    @Query("UPDATE AppUser a " +
            "SET a.enabled = TRUE WHERE a.email = ?1")
    int enableAppUser(String email);


    @Transactional
    @Modifying
    @Query("UPDATE AppUser a SET a.enabled = FALSE WHERE a.id = :id")
    int disableAppUser(@Param("id") Long id);


    @Transactional
    @Modifying
    @Query("UPDATE AppUser a SET a.appUserRole = :role WHERE a.id = :id")
    int updateUserRole(@Param("id") Long id, @Param("role") AppUserRole role);


    List<AppUser> findByAppUserRoleAndEnabled(AppUserRole role, boolean enabled);
}