package ua.vg.msg.userservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ua.vg.msg.userservice.repository.entity.AddressEntity;

import java.util.List;
import java.util.UUID;

/**
 * AddressRepository — JPA repository for AddressEntity.
 *
 * @author ykalapusha
 * @since 08.08.2026
 */
public interface AddressRepository extends JpaRepository<AddressEntity, Long> {

    List<AddressEntity> findAllByUserId(UUID userId);
}
