package ru.basejava;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
/*
1) реализовать метод через стрим int minValue(int[] values).
Метод принимает массив цифр от 1 до 9, надо выбрать уникальные и вернуть минимально возможное число,
составленное из этих уникальных цифр.
Не использовать преобразование в строку и обратно. Например {1,2,3,3,2,3} вернет 123, а {9,8} вернет 89

2) реализовать метод List<Integer> oddOrEven(List<Integer> integers)
если сумма всех чисел нечетная - удалить все нечетные, если четная - удалить все четные.
Сложность алгоритма должна быть O(N). Optional - решение в один стрим.
 */

public class MainStream {

    public static void main(String[] args) {
        int minValue = minValue(new int[]{1, 9, 3, 3, 2, 3});
        System.out.println(minValue);

        List<Integer> integerList = oddOrEven(new ArrayList<>(Arrays.asList(1, 4, 3, 3, 2, 5, 6)));
        integerList.forEach(System.out::println);
    }

    private static int minValue(int[] values) {
        return Arrays.stream(values)
                .sorted()
                .distinct()
                .reduce(0, (acc, x) -> (acc * 10 + x));
    }

    private static List<Integer> oddOrEven(List<Integer> integers) {
//        System.out.println("Sum " + sum(integers));
        return integers.stream()
                .collect(
                        Collectors.collectingAndThen(
                                Collectors.partitioningBy(MainStream::isEven),
                                map -> isEven(map.get(false).size()) ? map.get(false) : map.get(true)
                        )
                );
    }

    private static boolean isEven(Integer x) {
        return (x & 1) == 0;
    }
//    private static int sum(List<Integer> integerList) {
//        return integerList.stream().mapToInt(v -> v).sum();
//    }


}
