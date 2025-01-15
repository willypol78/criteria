package com.codely.criteria.to;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UsersTestRepository extends JpaRepository<UserTestEntity, String> {
	Page<UserTestEntity> findAll(Specification<UserTestEntity> spec, Pageable pageable);
}
