package ru.javawebinar.topjava.util;

import ru.javawebinar.topjava.model.UserMeal;
import ru.javawebinar.topjava.model.UserMealWithExceed;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * GKislin
 * 31.05.2015.
 */
public class UserMealsUtil {
    public static void main(String[] args) {
        List<UserMeal> mealList = Arrays.asList(
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 30, 10, 0), "Завтрак", 500),
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 30, 13, 0), "Обед", 1000),
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 30, 20, 0), "Ужин", 500),
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 31, 10, 0), "Завтрак", 1000),
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 31, 13, 0), "Обед", 500),
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 31, 20, 0), "Ужин", 510)
        );
        System.out.println(getFilteredMealsWithExceeded(mealList, LocalTime.of(7, 0), LocalTime.of(12, 0), 2000));
//        .toLocalDate();
//        .toLocalTime();
    }

    /**
     * Возращает список записей согласно заданному интервалу времени
     *
     * @param mealList       список записей еды
     * @param startTime      начало временного интервала
     * @param endTime        конец временного интервала
     * @param caloriesPerDay дневная норма каллорий
     * @return список записей
     */
    public static List<UserMealWithExceed> getFilteredMealsWithExceeded(List<UserMeal> mealList, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {
        List<UserMealWithExceed> list = new ArrayList<>();
        Map<LocalDate, List<UserMeal>> map = mealList
                .stream()
                .collect(Collectors.groupingBy(userMeal -> userMeal.getDateTime().toLocalDate()));

        for (Map.Entry<LocalDate, List<UserMeal>> pair : map.entrySet()) {
            Stream<UserMeal> stream = pair.getValue().stream();
            Integer sumCalories = stream.mapToInt(UserMeal::getCalories).sum();
            list
                    .addAll(stream
                            .map(userMeal ->
                                    new UserMealWithExceed(userMeal.getDateTime(),
                                            userMeal.getDescription(),
                                            userMeal.getCalories(),
                                           sumCalories > caloriesPerDay))
                            .collect(Collectors.toList()));
        }


        return list
                .stream()
                .filter(userMealWithExceed -> TimeUtil.isBetween(userMealWithExceed.getDateTime().toLocalTime(), startTime, endTime))
                .collect(Collectors.toList());

    }
}
