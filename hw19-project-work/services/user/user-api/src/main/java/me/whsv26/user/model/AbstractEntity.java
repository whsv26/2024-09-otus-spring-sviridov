package me.whsv26.user.model;

import lombok.Getter;
import org.hibernate.Hibernate;

import java.util.UUID;

@Getter
public abstract class AbstractEntity {

    abstract UUID getId();

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
