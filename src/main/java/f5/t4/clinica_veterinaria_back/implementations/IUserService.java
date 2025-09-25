package f5.t4.clinica_veterinaria_back.implementations;

import java.util.List;

public interface IUserService  <T, S> {

    public List<T> getEntities();
    public T getByID(Long id);
    public T updateEntity(Long id, S dto);
    public void deleteEntity(Long id);
    
}