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

    public static Integer calcMealCaloriesPerDate(List<UserMeal> meals, LocalDate date) {
        Integer tmpCalcCalories = 0;
        for (int i = 0; i < meals.size() ; i++) {
            LocalDate tmpDate = meals.get(i).getDateTime().toLocalDate();
            if (date.isEqual(tmpDate))
                tmpCalcCalories = tmpCalcCalories + meals.get(i).getCalories();
        }
        return tmpCalcCalories;
    }

    public static List<UserMealWithExcess> filteredByCycles(List<UserMeal> meals, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {
        List<UserMealWithExcess> filteredUserMealList = new ArrayList<>();
        for (int i = 0; i < meals.size() ; i++) {
            LocalDateTime fullTmpDate = meals.get(i).getDateTime();
            LocalTime tmpTime = fullTmpDate.toLocalTime();
            LocalDate tmpDate = fullTmpDate.toLocalDate();
            if (TimeUtil.isBetweenHalfOpen(tmpTime,startTime,endTime)) {
                Integer calculatedCaloriesPerDay = calcMealCaloriesPerDate(meals,tmpDate);
                filteredUserMealList.add(new UserMealWithExcess(meals.get(i).getDateTime(), meals.get(i).getDescription(),
                        meals.get(i).getCalories(), (calculatedCaloriesPerDay>caloriesPerDay)));
            }
        }
        return filteredUserMealList;
    }

    public static Integer calcMealCaloriesPerDateStreams(List<UserMeal> meals, LocalDate date) {
        Map<LocalDate, Integer> tmp = meals.stream().filter(x->x.getDateTime().toLocalDate().isEqual(date))
                .collect(Collectors.groupingBy(x->x.getDateTime().toLocalDate(),Collectors.summingInt(x->x.getCalories())));
        return tmp.get(date);
    }

    public static List<UserMealWithExcess> filteredByStreams(List<UserMeal> meals, LocalTime
            startTime, LocalTime endTime, int caloriesPerDay) {
        return meals.stream().filter((x ->TimeUtil.isBetweenHalfOpen(x.getDateTime().toLocalTime(),startTime,endTime)))
                .map(x->new UserMealWithExcess(x.getDateTime(),x.getDescription(), x.getCalories(),calcMealCaloriesPerDateStreams(meals,x.getDateTime().toLocalDate())>caloriesPerDay))
                .collect(Collectors.toList());
    }
}
