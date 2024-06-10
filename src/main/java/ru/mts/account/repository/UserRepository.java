package ru.mts.account.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.mts.account.entity.User;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
}
