package tw.com.firstbank.model;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public abstract class AggregateRoot<ID> extends Entity<ID> {

  private static final long serialVersionUID = 497028591926583471L;
  private final List<DomainEvent> domainEvents;
  protected boolean isDeleted;

  public AggregateRoot(ID id) {
    super(id);
    this.domainEvents = new CopyOnWriteArrayList<>();
  }

  public void addDomainEvent(DomainEvent domainEvent) {
    domainEvents.add(domainEvent);
  }

  public List<DomainEvent> getDomainEvents() {
    return Collections.unmodifiableList(domainEvents);
  }

  public void clearDomainEvents() {
    domainEvents.clear();
  }
}
