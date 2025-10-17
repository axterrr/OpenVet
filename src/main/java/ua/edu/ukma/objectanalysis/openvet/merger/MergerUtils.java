package ua.edu.ukma.objectanalysis.openvet.merger;

import java.util.function.Consumer;

public class MergerUtils {

    private MergerUtils() {}

    public static <T> void ifNotNull(T value, Consumer<T> consumer) {
        if (value != null) {
            consumer.accept(value);
        }
    }
}
