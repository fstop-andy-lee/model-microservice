package tw.com.firstbank.adapter.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import tw.com.firstbank.entity.BlackList;

@RepositoryRestResource
public interface BlackListRepository extends JpaRepository<BlackList, String> {
  
  @Query(value = "SELECT id FROM black_list WHERE position(LOWER(:name) in LOWER(name)) > 0 "
      + " order by id"
      , nativeQuery = true)
  public List<String> find(@Param("name") String name);
}
