package de.hhu.cs.dbs.dbwk.project.model;

import java.util.Locale;

public class SimpleRole implements Role {

    private final String value;

    public SimpleRole(String value) {
        this.value = value.toUpperCase(Locale.ENGLISH);
    }

    @Override
    public String getValue() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SimpleRole that = (SimpleRole) o;

        return getValue().equals(that.getValue());
    }

    @Override
    public int hashCode() {
        return getValue().hashCode();
    }
}
