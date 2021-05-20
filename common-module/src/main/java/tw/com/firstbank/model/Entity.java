package tw.com.firstbank.model;

import java.io.Serializable;

public abstract class Entity<ID> implements Serializable {

    private static final long serialVersionUID = 1L;

    protected final ID id;

    public Entity(ID id) {
        this.id = id;
    }

    public ID getId() {
        return id;
    }
}
