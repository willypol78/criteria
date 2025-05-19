package dev.willypol.to;

import dev.willypol.Criteria;
import dev.willypol.errors.InvalidFilter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

public class PostgreSQLUsersTestRepository {
	private static final Logger              log = LogManager.getLogger(PostgreSQLUsersTestRepository.class);
	private final        UsersTestRepository repository;

	public PostgreSQLUsersTestRepository(final UsersTestRepository repository) {
		this.repository = repository;
	}

	public Page<UserTestEntity> search(final Criteria criteria) {
		try {
			Specification<UserTestEntity> spec     = CriteriaToJPASpecificationConverter.toSpecification(criteria);
			Pageable                      pageable = CriteriaToJPASpecificationConverter.toPageable(criteria);

			return repository.findAll(spec, pageable);
		} catch (InvalidFilter | InvalidDataAccessApiUsageException e) {
			log.error("Error creating filter: {}", e.getMessage());
			return Page.empty();
		}
	}
}
