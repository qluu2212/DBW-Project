package de.hhu.cs.dbs.dbwk.project.presentation.rest;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.regex.Pattern;

@Service
public class Appservice {

    private final JdbcTemplate jdbcTemplate;


    public Appservice(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public boolean emailValidation(String email) {
        String emailRegex = "^[a-zA-Z0-9]+@[a-zA-Z0-9]+\\.[a-zA-Z]+$";
        Pattern pattern = Pattern.compile(emailRegex);
        return pattern.matcher(email).matches();
    }

    public Integer findBerufid(String name) {
        String query = "select ID from Beruf where lower(Name) = lower(?) ";
        try {
            return jdbcTemplate.queryForObject(query, Integer.class, name);
        } catch (EmptyResultDataAccessException ex) {
            return null;
        }
    }
    public Integer findBuergerid(String email) {
        String query = "select rowid from Buerger where lower(EmailAdresse) = lower(?) ";
        try {
            return jdbcTemplate.queryForObject(query, Integer.class, email);
        } catch (EmptyResultDataAccessException ex) {
            return null;
        }
    }

    public String findErsteller(Integer id) {
        String query = "select BuergerEMailAdresse from Anzeige where ID = ? ";
        try {
            return jdbcTemplate.queryForObject(query, String.class, id);
        } catch (EmptyResultDataAccessException ex) {
            return null;
        }
    }
    public String findAutor(Integer id) {
        String query = "select BuergerEMailAdresse from Nachricht where ID = ? ";
        try {
            return jdbcTemplate.queryForObject(query, String.class, id);
        } catch (EmptyResultDataAccessException ex) {
            return null;
        }
    }
}
