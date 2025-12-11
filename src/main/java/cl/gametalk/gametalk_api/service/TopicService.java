package cl.gametalk.gametalk_api.service;

import cl.gametalk.gametalk_api.model.dto.TopicCreateDTO;
import cl.gametalk.gametalk_api.model.dto.TopicDTO;
import cl.gametalk.gametalk_api.model.entities.Category;
import cl.gametalk.gametalk_api.model.entities.Topic;
import cl.gametalk.gametalk_api.model.entities.User;
import cl.gametalk.gametalk_api.model.repository.CategoryRepository;
import cl.gametalk.gametalk_api.model.repository.TopicRepository;
import cl.gametalk.gametalk_api.model.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TopicService {
    
    private final TopicRepository topicRepository;
    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;
    
    @Transactional(readOnly = true)
    public List<TopicDTO> getAllTopics() {
        return topicRepository.findAllByOrderByLastActivityDesc().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    @Transactional(readOnly = true)
    public TopicDTO getTopicById(Integer id) {
        Topic topic = topicRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Topic no encontrado con id: " + id));
        
        // Incrementar viewsCount
        topic.setViewsCount(topic.getViewsCount() + 1);
        topicRepository.save(topic);
        
        return convertToDTO(topic);
    }
    
    @Transactional(readOnly = true)
    public List<TopicDTO> getTopicsByCategory(Integer categoryId) {
        return topicRepository.findByCategoryIdOrderByLastActivityDesc(categoryId).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    @Transactional(readOnly = true)
    public List<TopicDTO> getTopicsByUser(Integer userId) {
        return topicRepository.findByUserId(userId).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    @Transactional
    public TopicDTO createTopic(TopicCreateDTO topicCreateDTO) {
        // Validar que existan category y user
        Category category = categoryRepository.findById(topicCreateDTO.getCategoryId())
                .orElseThrow(() -> new RuntimeException("Categoría no encontrada"));
        userRepository.findById(topicCreateDTO.getUserId())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        
        long currentTime = System.currentTimeMillis();
        
        Topic topic = new Topic();
        topic.setCategoryId(topicCreateDTO.getCategoryId());
        topic.setUserId(topicCreateDTO.getUserId());
        topic.setTitle(topicCreateDTO.getTitle());
        topic.setDescription(topicCreateDTO.getDescription());
        topic.setCreatedAt(currentTime);
        topic.setLastActivity(currentTime);
        topic.setRepliesCount(0);
        topic.setViewsCount(0);
        
        Topic savedTopic = topicRepository.save(topic);
        
        // Actualizar topicsCount en la categoría
        category.setTopicsCount(category.getTopicsCount() + 1);
        categoryRepository.save(category);
        
        return convertToDTO(savedTopic);
    }
    
    @Transactional
    public TopicDTO updateTopic(Integer id, TopicCreateDTO topicCreateDTO) {
        Topic topic = topicRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Topic no encontrado con id: " + id));
        
        topic.setTitle(topicCreateDTO.getTitle());
        topic.setDescription(topicCreateDTO.getDescription());
        topic.setLastActivity(System.currentTimeMillis());
        
        Topic updatedTopic = topicRepository.save(topic);
        return convertToDTO(updatedTopic);
    }
    
    @Transactional
    public void deleteTopic(Integer id) {
        Topic topic = topicRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Topic no encontrado con id: " + id));
        
        // Actualizar topicsCount en la categoría
        Category category = categoryRepository.findById(topic.getCategoryId())
                .orElseThrow(() -> new RuntimeException("Categoría no encontrada"));
        category.setTopicsCount(Math.max(0, category.getTopicsCount() - 1));
        categoryRepository.save(category);
        
        topicRepository.deleteById(id);
    }
    
    private TopicDTO convertToDTO(Topic topic) {
        String categoryName = categoryRepository.findById(topic.getCategoryId())
                .map(Category::getName)
                .orElse(null);
        
        String username = userRepository.findById(topic.getUserId())
                .map(User::getUsername)
                .orElse(null);
        
        return new TopicDTO(
                topic.getId(),
                topic.getCategoryId(),
                topic.getUserId(),
                topic.getTitle(),
                topic.getDescription(),
                topic.getCreatedAt(),
                topic.getRepliesCount(),
                topic.getViewsCount(),
                topic.getLastActivity(),
                categoryName,
                username
        );
    }
}
