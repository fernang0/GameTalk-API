package cl.gametalk.gametalk_api.controller;

import cl.gametalk.gametalk_api.model.dto.TopicCreateDTO;
import cl.gametalk.gametalk_api.model.dto.TopicDTO;
import cl.gametalk.gametalk_api.service.TopicService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/topics")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class TopicController {
    
    private final TopicService topicService;
    
    @GetMapping
    public ResponseEntity<List<TopicDTO>> getAllTopics(
            @RequestParam(required = false) Integer categoryId,
            @RequestParam(required = false) Integer userId) {
        
        if (categoryId != null) {
            return ResponseEntity.ok(topicService.getTopicsByCategory(categoryId));
        }
        if (userId != null) {
            return ResponseEntity.ok(topicService.getTopicsByUser(userId));
        }
        return ResponseEntity.ok(topicService.getAllTopics());
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<TopicDTO> getTopicById(@PathVariable Integer id) {
        try {
            return ResponseEntity.ok(topicService.getTopicById(id));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    @PostMapping
    public ResponseEntity<?> createTopic(@RequestBody TopicCreateDTO topicCreateDTO) {
        try {
            TopicDTO createdTopic = topicService.createTopic(topicCreateDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdTopic);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<?> updateTopic(
            @PathVariable Integer id,
            @RequestBody TopicCreateDTO topicCreateDTO) {
        try {
            TopicDTO updatedTopic = topicService.updateTopic(id, topicCreateDTO);
            return ResponseEntity.ok(updatedTopic);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteTopic(@PathVariable Integer id) {
        try {
            topicService.deleteTopic(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
