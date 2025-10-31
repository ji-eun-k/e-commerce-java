package kr.hhplus.be.server.user.infrastructure.persistence.repository;

import kr.hhplus.be.server.user.infrastructure.persistence.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserJpaRepository extends JpaRepository<UserEntity, Long> {
    UserEntity findById(long id);
}
