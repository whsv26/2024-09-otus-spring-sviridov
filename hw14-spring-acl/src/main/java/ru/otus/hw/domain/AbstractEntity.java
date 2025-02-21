package ru.otus.hw.domain;

import lombok.Getter;
import org.hibernate.Hibernate;

@Getter
public abstract class AbstractEntity {

    abstract Long getId();

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) {
            return false;
        }
        var that = (AbstractEntity) o;
        return getId() != null && getId().equals(that.getId());
    }

    @Override
    public int hashCode() {
        return Hibernate.getClass(this).hashCode();
    }
}
