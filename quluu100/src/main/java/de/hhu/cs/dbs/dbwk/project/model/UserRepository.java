package de.hhu.cs.dbs.dbwk.project.model;

import java.util.Optional;

public interface UserRepository {

    /**
     * Gibt einen {@link User} anhand von {@code uniqueString} wie bspw. Benutzername oder
     * E-Mail-Adresse zurück.
     *
     * @param uniqueString Eindeutige Zeichenkette, die für genau einen {@link User} steht.
     * @return {@link Optional<User>}, der einen {@link User} mit {@code username} zurückgibt.
     */
    Optional<User> findUser(String uniqueString);
}
