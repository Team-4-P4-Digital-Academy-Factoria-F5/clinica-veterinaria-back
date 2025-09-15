package f5.t4.clinica_veterinaria_back.patient.dtos;

public record PatientResponseDTO( Long id_patient,
String identification_number,
String name,
String image,
int age,
String family,
String breed,
String sex,
UserEntity tutor) {

}
