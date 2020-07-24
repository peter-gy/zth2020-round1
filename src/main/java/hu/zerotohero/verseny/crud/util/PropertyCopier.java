package hu.zerotohero.verseny.crud.util;

import hu.zerotohero.verseny.crud.entity.Location;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;

import java.beans.FeatureDescriptor;
import java.util.stream.Stream;

public class PropertyCopier {

    public static void copyNonNullProperties(Object source, Object target) {
        BeanUtils.copyProperties(source, target, getNullPropertyNames(source));
    }

    private static String[] getNullPropertyNames(Object source) {
        final BeanWrapper wrappedSource = new BeanWrapperImpl(source);
        return Stream.of(wrappedSource.getPropertyDescriptors())
                .map(FeatureDescriptor::getName)
                .filter(propertyName -> wrappedSource.getPropertyValue(propertyName) == null)
                .toArray(String[]::new);
    }

    public static void main(String[] args) {
        Location src = new Location();
        src.setName("name");
        src.setAddress("address");
        System.out.println("src = " + src);

        Location target = new Location();
        System.out.println("target = " + target);

        copyNonNullProperties(src, target);

        System.out.println("target = " + target);

    }

}
