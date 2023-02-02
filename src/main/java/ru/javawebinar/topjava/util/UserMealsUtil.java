package ru.javawebinar.topjava.util;

import ru.javawebinar.topjava.model.UserMeal;
import ru.javawebinar.topjava.model.UserMealWithExcess;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.util.*;
import java.util.stream.Collectors;

import static java.util.Objects.isNull;

public class UserMealsUtil {
    public static void main(String[] args) {
        List<UserMeal> meals = Arrays.asList(
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 30, 10, 0), "Завтрак", 500),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 30, 13, 0), "Обед", 1000),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 30, 20, 0), "Ужин", 500),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 0, 0), "Еда на граничное значение", 100),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 10, 0), "Завтрак", 1000),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 13, 0), "Обед", 500),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 20, 0), "Ужин", 410)
        );

        List<UserMealWithExcess> mealsTo = filteredByCycles(meals, LocalTime.of(7, 0), LocalTime.of(12, 0), 2000);
        mealsTo.forEach(System.out::println);

        List<UserMealWithExcess> mealsToS = filteredByStreams(meals, LocalTime.of(7, 0), LocalTime.of(12, 0), 2000);
        mealsToS.forEach(System.out::println);

//        System.out.println(filteredByStreams(meals, LocalTime.of(7, 0), LocalTime.of(12, 0), 2000));
    }

    public static Map<LocalDate, Integer> calcMealCaloriesPerDate(List<UserMeal> meals) {
        Map<LocalDate, Integer> caloriesByDate = new HashMap<>();
        for (UserMeal userMeal : meals) {
            LocalDate userMealDate = userMeal.getDateTime().toLocalDate();
            Integer calories = caloriesByDate.get(userMealDate);
            if (isNull(calories))
                caloriesByDate.put(userMealDate, userMeal.getCalories());
            else
                caloriesByDate.put(userMealDate, calories + userMeal.getCalories());
        }
        return caloriesByDate;
    }

    public static List<UserMealWithExcess> filteredByCycles(List<UserMeal> meals, LocalTime startTime,
                                                            LocalTime endTime, int caloriesPerDay) {
        List<UserMealWithExcess> filteredUserMealList = new ArrayList<>();
        Map<LocalDate, Integer> caloriesByDate = calcMealCaloriesPerDate(meals);
        for (UserMeal meal : meals) {
            LocalDateTime mealDateTime = meal.getDateTime();
            if (TimeUtil.isBetweenHalfOpen(mealDateTime.toLocalTime(), startTime, endTime)) {
                filteredUserMealList.add(new UserMealWithExcess(meal.getDateTime(), meal.getDescription(),
                        meal.getCalories(), (caloriesByDate.get(mealDateTime.toLocalDate()) > caloriesPerDay)));
            }
        }
        return filteredUserMealList;
    }

    public static Map<LocalDate, Integer> calcMealCaloriesPerDateStreams(List<UserMeal> meals) {
        return meals.stream().collect(Collectors.groupingBy(meal -> meal.getDateTime().toLocalDate(),
                Collectors.summingInt(UserMeal::getCalories)));
    }

    public static List<UserMealWithExcess> filteredByStreams(List<UserMeal> meals, LocalTime
            startTime, LocalTime endTime, int caloriesPerDay) {
        Map<LocalDate, Integer> caloriesByDate = calcMealCaloriesPerDateStreams(meals);
        return meals.stream().filter((meal -> TimeUtil.isBetweenHalfOpen(meal.getDateTime().toLocalTime(), startTime, endTime)))
                .map(meal -> new UserMealWithExcess(meal.getDateTime(), meal.getDescription(), meal.getCalories(),
                        caloriesByDate.get(meal.getDateTime().toLocalDate()) > caloriesPerDay))
                .collect(Collectors.toList());
    }
}