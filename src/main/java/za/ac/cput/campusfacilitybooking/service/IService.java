//Jada Rinquest 12/07/2026
//222871296

package za.ac.cput.campusfacilitybooking.service;

public interface IService<T, ID> {

    T create(T t);

    T read(ID id);

    T update(T t);

    boolean delete(ID id);

}