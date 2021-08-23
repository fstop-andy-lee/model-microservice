package tw.com.firstbank.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import tw.com.firstbank.entity.Position;

@RepositoryRestResource
public interface PositionRepository extends JpaRepository<Position, String> {

}
