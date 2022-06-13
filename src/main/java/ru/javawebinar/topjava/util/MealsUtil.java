package ru.javawebinar.topjava.util;

import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.MealTo;
import ru.javawebinar.topjava.repository.InMemoryMealRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.util.*;
import java.util.stream.Collectors;

import static ru.javawebinar.topjava.util.DateTimeUtil.isBetweenHalfOpen;

public class MealsUtil {
    public final static int DEFAULT_CALORIES_PER_DAY = 2000;
    public static List<Meal> meals = Arrays.asList(
            new Meal(LocalDateTime.of(2020, Month.JANUARY, 30, 10, 0), "Завтрак", 500),
            new Meal(LocalDateTime.of(2020, Month.JANUARY, 30, 13, 0), "Обед", 1000),
            new Meal(LocalDateTime.of(2020, Month.JANUARY, 30, 20, 0), "Ужин", 500),
            new Meal(LocalDateTime.of(2020, Month.JANUARY, 31, 0, 0), "Еда на граничное значение", 100),
            new Meal(LocalDateTime.of(2020, Month.JANUARY, 31, 10, 0), "Завтрак", 1000),
            new Meal(LocalDateTime.of(2020, Month.JANUARY, 31, 13, 0), "Обед", 500),
            new Meal(LocalDateTime.of(2020, Month.JANUARY, 31, 20, 0), "Ужин", 410)
    );
    public static LocalTime startTime = LocalTime.of(7, 0);
    public static LocalTime endTime = LocalTime.of(12, 0);

    public static void main(String[] args) {
//        meals.forEach(System.out::println);
//        List<MealTo> mealsTo = filteredByStreams(meals, startTime, endTime, DEFAULT_CALORIES_PER_DAY);
//        mealsTo.forEach(System.out::println);
//        System.out.println(filteredByCycles(meals, startTime, endTime, caloriesPerDay));
       getMealsTo().forEach(System.out::println);
    }


    public static List<MealTo> filteredByStreams(List<Meal> meals, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {
        Map<LocalDate, Integer> caloriesSumByDate = meals.stream()
                .collect(
                        Collectors.groupingBy(Meal::getDate, Collectors.summingInt(Meal::getCalories))
//                      Collectors.toMap(Meal::getDate, Meal::getCalories, Integer::sum)
                );

        return meals.stream()
                .filter(meal -> isBetweenHalfOpen(meal.getTime(), startTime, endTime))
                .map(meal -> createTo(meal, caloriesSumByDate.get(meal.getDate()) > caloriesPerDay))
                .collect(Collectors.toList());
    }

    private static MealTo createTo(Meal meal, boolean excess) {
        return new MealTo(meal.getDateTime(), meal.getDescription(), meal.getCalories(), excess);
    }

    public static List<MealTo> getMealsTo() {
        return filteredByStreams(meals, LocalTime.MIN, LocalTime.MAX, DEFAULT_CALORIES_PER_DAY);
    }

    public static List<MealTo> getMealsTo(List<Meal> mealList) {
        return filteredByStreams(mealList, LocalTime.MIN, LocalTime.MAX, DEFAULT_CALORIES_PER_DAY);
    }
    public static List<MealTo> getAllMeals(){

        return new ArrayList<>();
    }

    public static MealTo createWithExcess (Meal meal, boolean excess) {
        return new MealTo(meal.getDateTime(),meal.getDescription(), meal.getCalories(),excess);
    }
}
