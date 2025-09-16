package f5.t4.clinica_veterinaria_back.patient;

import java.util.List;

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
import f5.t4.clinica_veterinaria_back.patient.dtos.PatientRequestDTO;
import f5.t4.clinica_veterinaria_back.patient.dtos.PatientResponseDTO;

@RestController
@RequestMapping(path="${api-endpoint}/patients")
public class PatientController {

    private final IService<PatientResponseDTO, PatientRequestDTO> service;

    public PatientController(IService<PatientResponseDTO, PatientRequestDTO> service) {
        this.service = service;
    }

    @GetMapping("")
    public List<PatientResponseDTO> index() {
        return service.getEntities();

    }

    @PostMapping("")
    public ResponseEntity<PatientResponseDTO> createEntity(@RequestBody PatientRequestDTO dtoRequest) {

        if (dtoRequest.name().isBlank()) return ResponseEntity.badRequest().build();

        PatientResponseDTO entityStored = service.createEntity(dtoRequest);

        if (entityStored == null) return ResponseEntity.noContent().build();

        return ResponseEntity.status(201).body(entityStored);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PatientResponseDTO> show(@PathVariable("id") Long id) {
        PatientResponseDTO patient = service.getByID(id);
        return ResponseEntity.ok().body(patient);
    }

    @PutMapping("/{id}")
    public ResponseEntity<PatientResponseDTO> update(@PathVariable("id") Long id, @RequestBody PatientRequestDTO dtoRequest) {

        
        PatientResponseDTO updated = service.updateEntity(id, dtoRequest);
        return ResponseEntity.ok(updated);

    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") Long id) {
       
        service.deleteEntity(id);
        return ResponseEntity.noContent().build();
    }


   


}
