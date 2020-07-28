package hu.zerotohero.verseny.crud.util;

import hu.zerotohero.verseny.crud.entity.Employee;
import hu.zerotohero.verseny.crud.entity.Equipment;
import hu.zerotohero.verseny.crud.entity.Location;

import java.util.Objects;
import java.util.stream.Stream;

public class EntityValidator {

    public static void validateLocation(Location location) {
        if (Stream.of(
                location.getId(),
                location.getAddress(),
                location.getName())
                .anyMatch(Objects::isNull))
            throw new IllegalArgumentException("Location cannot have null field(s)");
        validateStrings(location.getAddress(), location.getName());
        validateLocationName(location.getName());
        validateLocationAddress(location.getAddress());
    }

    public static void validateEquipment(Equipment equipment) {
        if (Stream.of(
                equipment.getId(),
                equipment.getName(),
                equipment.getType(),
                equipment.getLocatedAt())
                .anyMatch(Objects::isNull))
            throw new IllegalArgumentException("Equipment cannot have null field(s)");
        validateLocation(equipment.getLocatedAt());
        validateStrings(equipment.getName());
    }

    public static void validateEmployee(Employee employee) {
        if (Stream.of(
                employee.getId(),
                employee.getName(),
                employee.getJob(),
                employee.getSalary(),
                employee.getWorksAt(),
                employee.getOperates())
                .anyMatch(Objects::isNull))
            throw new IllegalArgumentException("Employee cannot have null field(s)");
        validateLocation(employee.getWorksAt());
        validateEquipment(employee.getOperates());
        validateStrings(employee.getName());
        validateEmployeeName(employee.getName());
    }

    private static void validateStrings(String... strings) {
        for (String string : strings) validateString(string);
    }

    private static void validateString(String string) {
        if (string.isEmpty() || string.trim().isEmpty())
            throw new IllegalArgumentException("String cannot be empty nor blank");
    }

    private static void validateLocationName(String name) {
        int numWords = name.split(" ").length;
        if (numWords > 2)
            throw new IllegalArgumentException("Location names cannot consist of more than 2 words, instead of ," + numWords);
    }

    private static void validateLocationAddress(String address) {
        if (!address.matches("^\\d{4}"))
            throw new IllegalArgumentException("Location address must start with a 4-digit post code");
    }

    private static void validateEmployeeName(String name) {
        int numWords = name.split(" ").length;
        if (!(2 <= numWords && numWords <= 3))
            throw new IllegalArgumentException("Employee name must consist of 2 or 3 words, instead of " + numWords);
    }

}
