package de.hhu.cs.dbs.dbwk.project.persistence;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jdbc.repository.config.EnableJdbcRepositories;

import static org.springframework.data.repository.query.QueryLookupStrategy.Key.USE_DECLARED_QUERY;

@Configuration
@EnableJdbcRepositories(queryLookupStrategy = USE_DECLARED_QUERY)
public class PersistenceConfiguration {}

