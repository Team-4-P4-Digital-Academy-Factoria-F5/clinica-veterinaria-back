package f5.t4.clinica_veterinaria_back.user;

import java.security.Principal;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import f5.t4.clinica_veterinaria_back.implementations.IService;
import f5.t4.clinica_veterinaria_back.user.dtos.UserRequestDTO;
import f5.t4.clinica_veterinaria_back.user.dtos.UserResponseDTO;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping(path = "${api-endpoint}users")
@RequiredArgsConstructor
public class UserController {

    private final IService<UserResponseDTO, UserRequestDTO> userService;

    @GetMapping
    public ResponseEntity<List<UserResponseDTO>> getAllUsers() {
        return ResponseEntity.ok(userService.getEntities());
    }

    @PostMapping
    public ResponseEntity<UserResponseDTO> createUser(@RequestBody UserRequestDTO dto) {
        UserResponseDTO createdUser = userService.createEntity(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdUser);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDTO> getUserById(@PathVariable Long id) {
        return ResponseEntity.ok(userService.getByID(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserResponseDTO> updateUser(
            @PathVariable Long id,
            @RequestBody UserRequestDTO dto) {
        return ResponseEntity.ok(userService.updateEntity(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.deleteEntity(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/patients")
    public void getPatients(Principal principal) {
        System.out.println(principal.getName());
    }
}