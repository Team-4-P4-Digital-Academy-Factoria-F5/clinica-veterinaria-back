package f5.t4.clinica_veterinaria_back.implementations;

public interface IRegisterService<T, S> {
    S register(T request);
}