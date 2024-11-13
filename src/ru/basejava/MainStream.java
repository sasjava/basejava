package ru.basejava;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
/*
1) ����������� ����� ����� ����� int minValue(int[] values).
����� ��������� ������ ���� �� 1 �� 9, ���� ������� ���������� � ������� ���������� ��������� �����,
������������ �� ���� ���������� ����.
�� ������������ �������������� � ������ � �������. �������� {1,2,3,3,2,3} ������ 123, � {9,8} ������ 89

2) ����������� ����� List<Integer> oddOrEven(List<Integer> integers)
���� ����� ���� ����� �������� - ������� ��� ��������, ���� ������ - ������� ��� ������.
��������� ��������� ������ ���� O(N). Optional - ������� � ���� �����.
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
