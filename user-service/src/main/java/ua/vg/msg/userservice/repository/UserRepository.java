package ua.vg.msg.userservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ua.vg.msg.userservice.repository.entity.UserEntity;

import java.util.Optional;
import java.util.UUID;

/**
 * UserRepository — Repository interface for UserEntity.
 *
 * @author ykalapusha
 * @since 08.08.2026
 */
@Repository
public interface UserRepository extends JpaRepository<UserEntity, UUID> {

    Optional<UserEntity> findByEmail(String email);
}
