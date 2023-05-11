package com.atibusinessgroup.amanyaman.repository;

// import com.atibusinessgroup.amanyaman.domain.Product;
import com.atibusinessgroup.amanyaman.domain.User;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.time.Instant;

/**
 * Spring Data JPA repository for the {@link User} entity.
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long>, JpaSpecificationExecutor<User> {

    String USERS_BY_LOGIN_CACHE = "usersByLogin";

    String USERS_BY_EMAIL_CACHE = "usersByEmail";

    Optional<User> findOneByActivationKey(String activationKey);

    List<User> findAllByActivatedIsFalseAndActivationKeyIsNotNullAndCreatedDateBefore(Instant dateTime);

    Optional<User> findOneByResetKey(String resetKey);

    Optional<User> findOneByEmailIgnoreCase(String email);

    Optional<User> findOneByLogin(String login);

    // @EntityGraph(attributePaths = {"authorities", "travelAgent"})
    @Cacheable(cacheNames = USERS_BY_LOGIN_CACHE)
    @EntityGraph(attributePaths = {"authorities"})
    Optional<User> findOneWithAuthoritiesByLogin(String login);

    @EntityGraph(attributePaths = {"authorities"})
    @Cacheable(cacheNames = USERS_BY_LOGIN_CACHE)
    Optional<User> findOneWithAuthoritiesById(Long id);

    // @EntityGraph(attributePaths = {"authorities", "travelAgent"})
    @Cacheable(cacheNames = USERS_BY_EMAIL_CACHE)
    @EntityGraph(attributePaths = {"authorities"})
    Optional<User> findOneWithAuthoritiesByEmailIgnoreCase(String email);

    @Query("UPDATE User u SET u.failedAttempt = ?1 WHERE u.login = ?2")
    @Modifying
    void updateFailedAttempt(int failedAttempt, String login);

    Page<User> findAllByLoginNot(Pageable pageable, String login);

    List<User> findAllByIdIn(List<Long> userIdList);

    @Modifying
    @Query(value = "update jhi_user set last_latest_feed = NOW() where id = :userId",
        nativeQuery = true)
    void updateUserLastLatestFeed(long userId);
}
