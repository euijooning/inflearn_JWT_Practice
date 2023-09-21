package inflearn.freejwt.repository;

import inflearn.freejwt.entity.User;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    // 해당 쿼리 수행 될 때 Eager 조회로 authorities 정보를 같이 가져오게 된다.
    @EntityGraph(attributePaths = "authorities")
    Optional<User> findOneWithAuthoritiesByUsername(String username);
    //username을 기준으로 User 정보를 가져올 때 권한 정보도 같이 가져오게 된다.
}
