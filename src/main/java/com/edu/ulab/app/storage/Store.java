package com.edu.ulab.app.storage;

import com.edu.ulab.app.entity.Entity;

import java.util.Collection;

//todo создать хранилище в котором будут содержаться данные
// сделать абстракции через которые можно будет производить операции с хранилищем
// продумать логику поиска и сохранения
// продумать возможные ошибки
// учесть, что при сохранеии юзера или книги, должен генерироваться идентификатор
// продумать что у узера может быть много книг и нужно создать эту связь
// так же учесть, что методы хранилища принимают друго тип данных - учесть это в абстракции

public interface Store<T extends Entity> {

    T save(T t);
    Collection<T> getAll();
    T getById(Long id);
    T update(T t);
    void deleteById(Long id);

}
