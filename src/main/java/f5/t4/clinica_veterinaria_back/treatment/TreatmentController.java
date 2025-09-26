package f5.t4.clinica_veterinaria_back.treatment;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import f5.t4.clinica_veterinaria_back.treatment.dtos.TreatmentRequestDTO;
import f5.t4.clinica_veterinaria_back.treatment.dtos.TreatmentResponseDTO;

@RestController
@RequestMapping("${api-endpoint}/treatments")
public class TreatmentController {

    private final InterfaceTreatmentService treatmentService;

    public TreatmentController(InterfaceTreatmentService treatmentService) {
        this.treatmentService = treatmentService;
    }

    // /api/treatments/{id} Obtiene un tratamiento concreto por su ID
    @GetMapping("/{id}")
    public ResponseEntity<TreatmentResponseDTO> getTreatmentById(@PathVariable Long id) {
        TreatmentResponseDTO treatment = treatmentService.getTreatmentById(id);
        return ResponseEntity.ok(treatment);
    }

    // Obtiene todos los tratamientos de un paciente
    @GetMapping("/patient/{patientId}")
    public ResponseEntity<List<TreatmentResponseDTO>> getTreatmentsByPatient(@PathVariable Long patientId) {
        List<TreatmentResponseDTO> treatments = treatmentService.getTreatmentsByPatient(patientId);
        return ResponseEntity.ok(treatments);
    }

    // Crea un nuevo tratamiento asociado a un paciente
    @PostMapping("/patient/{patientId}")
    public ResponseEntity<TreatmentResponseDTO> createTreatment(
            @PathVariable Long patientId,
            @RequestBody TreatmentRequestDTO dtoRequest) {

        TreatmentResponseDTO newTreatment = treatmentService.createTreatment(patientId, dtoRequest);
        return ResponseEntity.status(201).body(newTreatment);
    }
}
