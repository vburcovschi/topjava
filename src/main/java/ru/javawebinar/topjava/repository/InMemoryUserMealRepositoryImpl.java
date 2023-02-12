package ru.javawebinar.topjava.repository;

import ru.javawebinar.topjava.model.UserMeal;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class InMemoryUserMealRepositoryImpl implements UserMealRepository{
    private Map<Integer, UserMeal> storage = new ConcurrentHashMap<>();
    private AtomicInteger counter = new AtomicInteger(0);

    {
        save(new UserMeal(LocalDateTime.of(2023, Month.JANUARY, 30, 10, 0), "Завтрак", 500));
        save(new UserMeal(LocalDateTime.of(2023, Month.JANUARY, 30, 13, 0), "Обед", 1000));
        save(new UserMeal(LocalDateTime.of(2023, Month.JANUARY, 30, 20, 0), "Ужин", 500));
        save(new UserMeal(LocalDateTime.of(2023, Month.JANUARY, 31, 0, 0), "Еда на граничное значение", 100));
        save(new UserMeal(LocalDateTime.of(2023, Month.JANUARY, 31, 10, 0), "Завтрак", 1000));
        save(new UserMeal(LocalDateTime.of(2023, Month.JANUARY, 31, 13, 0), "Обед", 500));
        save(new UserMeal(LocalDateTime.of(2023, Month.JANUARY, 31, 20, 0), "Ужин", 410));
    }
    @Override
    public UserMeal save(UserMeal userMeal) {
        if(userMeal.isNew()){
            userMeal.setId(counter.incrementAndGet());
        }
        return storage.put(userMeal.getId(),userMeal);
    }

    @Override
    public void delete(Integer id) {
        storage.remove(id);
    }

    @Override
    public UserMeal get(Integer id) {
        return storage.get(id);
    }

    @Override
    public Collection<UserMeal> getAll() {
        return storage.values();
    }
}
