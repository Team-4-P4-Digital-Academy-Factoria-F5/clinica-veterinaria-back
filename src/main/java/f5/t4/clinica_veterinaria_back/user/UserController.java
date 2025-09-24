package f5.t4.clinica_veterinaria_back.user;

import java.security.Principal;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import f5.t4.clinica_veterinaria_back.implementations.IService;
import f5.t4.clinica_veterinaria_back.patient.PatientEntity;
import f5.t4.clinica_veterinaria_back.user.dtos.UserRequestDTO;
import f5.t4.clinica_veterinaria_back.user.dtos.UserResponseDTO;
import f5.t4.clinica_veterinaria_back.user.exceptions.UserAccessDeniedException;
import f5.t4.clinica_veterinaria_back.user.UserRepository;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping(path = "${api-endpoint}/users")
@RequiredArgsConstructor
public class UserController {

    private final IService<UserResponseDTO, UserRequestDTO> userService;
    private final UserRepository userRepository;


    @GetMapping("")
    public ResponseEntity<List<UserResponseDTO>> getAllUsers() {
        return ResponseEntity.ok(userService.getEntities());
    }

    @PostMapping
    public ResponseEntity<UserResponseDTO> createUser(@RequestBody UserRequestDTO dto) {
        UserResponseDTO createdUser = userService.createEntity(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdUser);
    }

   @GetMapping("/{id}")
public ResponseEntity<UserResponseDTO> getUserById(@PathVariable Long id, Principal principal) {
    UserEntity currentUser = userRepository.findByEmail(principal.getName())
            .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado"));

    boolean isAdmin = currentUser.getRoles().stream()
            .anyMatch(r -> r.getName().equals("ROLE_ADMIN"));

    if (!isAdmin && !currentUser.getId_user().equals(id)) {
        throw new UserAccessDeniedException("No puedes acceder a otro usuario");
    }

    return ResponseEntity.ok(userService.getByID(id));
}

   @PutMapping("/{id}")
public ResponseEntity<UserResponseDTO> updateUser(
        @PathVariable Long id,
        @RequestBody UserRequestDTO dto,
        Principal principal) {

    UserEntity currentUser = userRepository.findByEmail(principal.getName())
            .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado"));

    boolean isAdmin = currentUser.getRoles().stream()
            .anyMatch(r -> r.getName().equals("ROLE_ADMIN"));

    if (!isAdmin && !currentUser.getId_user().equals(id)) {
        throw new UserAccessDeniedException("No puedes editar otro usuario");
    }

    return ResponseEntity.ok(userService.updateEntity(id, dto));
}

@DeleteMapping("/{id}")
public ResponseEntity<Void> deleteUser(@PathVariable Long id, Principal principal) {
    UserEntity currentUser = userRepository.findByEmail(principal.getName())
            .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado"));

    boolean isAdmin = currentUser.getRoles().stream()
            .anyMatch(r -> r.getName().equals("ROLE_ADMIN"));

    if (!isAdmin && !currentUser.getId_user().equals(id)) {
        throw new UserAccessDeniedException("No puedes borrar otro usuario");
    }

    userService.deleteEntity(id);
    return ResponseEntity.noContent().build();
}
       @GetMapping("/{id}/patients")
    public ResponseEntity<Set<PatientEntity>> getPatientsByUserId(@PathVariable Long id) {
        Optional<UserEntity> userOpt = userRepository.findById(id);
        if (userOpt.isPresent()) {
            return ResponseEntity.ok(userOpt.get().getPatients());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

}