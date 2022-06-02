package ru.javawebinar.topjava.util;
import ru.javawebinar.topjava.model.UserMeal;
import ru.javawebinar.topjava.model.UserMealWithExcess;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.util.*;
import java.util.stream.Collectors;

public class UserMealsUtil {
    public static void main(String[] args) {
        List<UserMeal> meals = Arrays.asList(
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 30, 10, 0), "Завтрак", 10000),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 30, 13, 0), "Обед", 100),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 30, 20, 0), "Ужин", 50),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 0, 0), "Еда на граничное значение", 100),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 10, 0), "Завтрак", 1),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 13, 0), "Обед", 50),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 20, 0), "Ужин", 10)
        );

        List<UserMealWithExcess> mealsTo = filteredByCycles(meals, LocalTime.of(0, 0), LocalTime.of(23, 59), 1500);
        mealsTo.forEach(System.out::println);

//        System.out.println(filteredByStreams(meals, LocalTime.of(7, 0), LocalTime.of(12, 0), 2000) + "\n");
    }

    public static List<UserMealWithExcess> filteredByCycles(List<UserMeal> meals, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {
        Map<LocalDate, Integer> caloriesSumByDate = new HashMap<>();
        for (UserMeal userMeal : meals) {
            int total;
            if(caloriesSumByDate.containsKey(userMeal.getDateTime().toLocalDate())){
                total = caloriesSumByDate.get(userMeal.getDateTime().toLocalDate());
                total += userMeal.getCalories();
                caloriesSumByDate.put(userMeal.getDateTime().toLocalDate(),total);
            }
            else{
                caloriesSumByDate.put(userMeal.getDateTime().toLocalDate(),userMeal.getCalories());
            }
        }
        List<UserMealWithExcess> list = new ArrayList<>();
        for(UserMeal userMeal: meals){
            if(caloriesPerDay <= caloriesSumByDate.get(userMeal.getDateTime().toLocalDate())){
                list.add(new UserMealWithExcess(userMeal.getDateTime(),userMeal.getDescription(),userMeal.getCalories(),true));
            }
            else {
                list.add(new UserMealWithExcess(userMeal.getDateTime(),userMeal.getDescription(),userMeal.getCalories(),false));
            }
        }
        return list;
    }

    public static List<UserMealWithExcess> filteredByStreams(List<UserMeal> meals, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {
        Map<LocalDate, Integer> caloriesSumByDate = meals.stream().collect(Collectors.groupingBy(um -> um.getDateTime().toLocalDate(), Collectors.summingInt(UserMeal::getCalories)));
        return meals.stream().
                filter(um -> TimeUtil.isBetweenHalfOpen(um.getDateTime().toLocalTime(), startTime, endTime))
                .map(um -> new UserMealWithExcess(um.getDateTime(), um.getDescription(), um.getCalories(),
                        caloriesSumByDate.get(um.getDateTime().toLocalDate()) > caloriesPerDay))
                .collect(Collectors.toList());
    }
}
