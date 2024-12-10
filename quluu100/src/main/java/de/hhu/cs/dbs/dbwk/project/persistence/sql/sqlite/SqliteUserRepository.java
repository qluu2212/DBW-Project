package de.hhu.cs.dbs.dbwk.project.persistence.sql.sqlite;

import de.hhu.cs.dbs.dbwk.project.model.SimpleRole;
import de.hhu.cs.dbs.dbwk.project.model.SimpleUser;
import de.hhu.cs.dbs.dbwk.project.model.User;
import de.hhu.cs.dbs.dbwk.project.model.UserRepository;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.Set;

@Repository
public class SqliteUserRepository implements UserRepository {
    private final JdbcTemplate jdbcTemplate;

    public SqliteUserRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }
    @Override
    public Optional<User> findUser(String uniqueString) {
        String checkquery = "SELECT COUNT(EMailAdresse) FROM Buerger WHERE EMailAdresse like ?";
        Integer checkexist = jdbcTemplate.queryForObject(checkquery, Integer.class, uniqueString);
        if (checkexist == null || checkexist == 0) {
            throw new UsernameNotFoundException("buerger not found");
        } else {
            String query = "SELECT Password FROM Buerger WHERE EMailAdresse = ?";
            String password = jdbcTemplate.queryForObject(query, String.class, uniqueString);
            return Optional.of(new SimpleUser(uniqueString, "{noop}"+password, Set.of(new SimpleRole("Buerger"))));
        }
        //            return Optional.of(new SimpleUser("abc123@gmail.com", "{noop}Ab12C1", Set.of(new SimpleRole("Buerger"))));
    }
}
