package f5.t4.clinica_veterinaria_back.patient.dtos;

public record PatientRequestDTO(
String identificationNumber,
String name,
String image,
int age,
String family,
String breed,
String sex,
Long tutor) {

}