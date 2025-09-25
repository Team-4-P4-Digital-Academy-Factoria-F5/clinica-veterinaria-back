package f5.t4.clinica_veterinaria_back.implementations;

import java.util.List;

public interface ITreatmentService <T,S>{

    public List<T> getEntities();
    public T createEntity(S dto);
    
}
