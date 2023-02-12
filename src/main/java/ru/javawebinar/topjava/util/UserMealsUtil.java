package ru.javawebinar.topjava.util;

import ru.javawebinar.topjava.model.UserMeal;
import ru.javawebinar.topjava.model.UserMealWithExcess;
import ru.javawebinar.topjava.repository.InMemoryUserMealRepositoryImpl;

import java.text.CollationElementIterator;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class UserMealsUtil {
    private final static List<UserMeal> USER_MEALS = Arrays.asList(
            new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 30, 10, 0), "Завтрак", 500),
            new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 30, 13, 0), "Обед", 1000),
            new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 30, 20, 0), "Ужин", 500),
            new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 0, 0), "Еда на граничное значение", 100),
            new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 10, 0), "Завтрак", 1000),
            new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 13, 0), "Обед", 500),
            new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 20, 0), "Ужин", 410)
    );

    private final static UserMeal TEST_USER_MEAL_DINNER = new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 19, 15), "Перекус", 300);
    private final static UserMeal TEST_USER_MEAL_BREAKFAST = new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 9, 15), "Перекус", 300);

    public final static Integer CALORIES_PER_DAY = 2000;

    public static void main(String[] args) {
/*        List<UserMealWithExcess> mealsTo = getFilteredWithExceeded(USER_MEALS, LocalTime.of(7, 0), LocalTime.of(12, 0), CALORIES_PER_DAY);
        mealsTo.forEach(System.out::println);*/
        InMemoryUserMealRepositoryImpl repository = new InMemoryUserMealRepositoryImpl();
        Collection<UserMeal> userMealCollection = repository.getAll();
        List<UserMealWithExcess> collection =  UserMealsUtil.getWithExceeded(userMealCollection, UserMealsUtil.CALORIES_PER_DAY);
        collection.forEach(System.out::println);

    }

    public static List<UserMealWithExcess> getWithExceeded (Collection<UserMeal> userMeals,Integer caloriesPerDay){
        return getFilteredWithExceeded(userMeals,LocalTime.MIN,LocalTime.MAX, caloriesPerDay);
    }

    public static List<UserMealWithExcess> getFilteredWithExceeded(Collection<UserMeal> userMeals, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {
        Map<LocalDate, Integer> caloriesSumByDate = userMeals.stream()
                .collect(
                        Collectors.groupingBy(UserMeal::getDate, Collectors.summingInt(UserMeal::getCalories))
//                      Collectors.toMap(Meal::getDate, Meal::getCalories, Integer::sum)
                );

        return userMeals.stream()
                .filter(userMeal -> TimeUtil.isBetweenHalfOpen(userMeal.getTime(), startTime, endTime))
                .map(userMeal -> createWithExcess(userMeal, caloriesSumByDate.get(userMeal.getDate()) > caloriesPerDay))
                .collect(Collectors.toList());
    }
    private static UserMealWithExcess createWithExcess(UserMeal userMeal, boolean excess) {
        return new UserMealWithExcess(userMeal.getId(), userMeal.getDateTime(), userMeal.getDescription(), userMeal.getCalories(), excess);
    }

    public static List<UserMeal> getTestData() {
        return USER_MEALS;
    }
}
