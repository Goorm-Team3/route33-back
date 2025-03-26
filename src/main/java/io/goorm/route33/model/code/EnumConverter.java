package io.goorm.route33.model.code;

import com.google.gson.TypeAdapter;
import jakarta.persistence.AttributeConverter;

public abstract class EnumConverter<T extends Enum<T> & EnumInterface<U>, U> extends TypeAdapter<T> implements AttributeConverter<T, U> {
    protected final T[] enumConstants;
    protected final Class<T> type;

    protected EnumConverter(Class<T> clazz) {
        this.enumConstants = clazz.getEnumConstants();
        this.type = clazz;
    }

    protected String getIllegalArgExceptionMsg(U value) {
        return String.format("type: %s, value: %s", type.getName(), value);
    }
}
