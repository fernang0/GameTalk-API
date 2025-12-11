package cl.gametalk.gametalk_api.service;

import cl.gametalk.gametalk_api.model.dto.UserCreateDTO;
import cl.gametalk.gametalk_api.model.dto.UserDTO;
import cl.gametalk.gametalk_api.model.entities.User;
import cl.gametalk.gametalk_api.model.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {
    
    private final UserRepository userRepository;
    
    @Transactional(readOnly = true)
    public List<UserDTO> getAllUsers() {
        return userRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    @Transactional(readOnly = true)
    public UserDTO getUserById(Integer id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con id: " + id));
        return convertToDTO(user);
    }
    
    @Transactional
    public UserDTO createUser(UserCreateDTO userCreateDTO) {
        if (userRepository.existsByEmail(userCreateDTO.getEmail())) {
            throw new RuntimeException("El email ya está en uso");
        }
        if (userRepository.existsByUsername(userCreateDTO.getUsername())) {
            throw new RuntimeException("El username ya está en uso");
        }
        
        User user = new User();
        user.setEmail(userCreateDTO.getEmail());
        user.setPassword(userCreateDTO.getPassword()); // TODO: Hashear password
        user.setUsername(userCreateDTO.getUsername());
        
        User savedUser = userRepository.save(user);
        return convertToDTO(savedUser);
    }
    
    @Transactional
    public UserDTO updatePassword(Integer id, String newPassword) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con id: " + id));
        
        user.setPassword(newPassword); // TODO: Hashear password
        User updatedUser = userRepository.save(user);
        return convertToDTO(updatedUser);
    }
    
    private UserDTO convertToDTO(User user) {
        return new UserDTO(user.getId(), user.getEmail(), user.getUsername());
    }
}
