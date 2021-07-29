package tw.com.firstbank.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import tw.com.firstbank.entity.Journal;
import tw.com.firstbank.entity.JournalKey;

@Repository
public interface JournalRepository extends JpaRepository<Journal, JournalKey> {

}
