package cl.gametalk.gametalk_api.model.repository;

import cl.gametalk.gametalk_api.model.entities.Topic;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TopicRepository extends JpaRepository<Topic, Integer> {
    
    List<Topic> findByCategoryId(Integer categoryId);
    
    List<Topic> findByUserId(Integer userId);
    
    List<Topic> findByCategoryIdOrderByLastActivityDesc(Integer categoryId);
    
    List<Topic> findAllByOrderByLastActivityDesc();
}
