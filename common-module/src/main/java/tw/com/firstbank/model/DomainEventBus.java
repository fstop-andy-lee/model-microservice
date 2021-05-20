package tw.com.firstbank.model;

public interface DomainEventBus {
    void post(DomainEvent domainEvent);
    @SuppressWarnings("rawtypes")
    void postAll(AggregateRoot aggregateRoot);
    void register(Object object);
    void unregister(Object object);
}
