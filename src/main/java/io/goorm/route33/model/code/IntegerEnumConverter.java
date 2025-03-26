package io.goorm.route33.model.code;

import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.util.stream.Stream;

public abstract class IntegerEnumConverter<T extends Enum<T> & EnumInterface<Integer>> extends EnumConverter<T, Integer> {
    protected IntegerEnumConverter(Class<T> clazz) {
        super(clazz);
    }

    @Override
    public Integer convertToDatabaseColumn(T integerCode) {
        if (integerCode == null) {
            return null;
        }
        return integerCode.value();
    }

    @Override
    public T convertToEntityAttribute(Integer value) {
        if (value == null) {
            return null;
        }

        return Stream.of(super.enumConstants)
                .filter(c -> value.equals(c.value()))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException(getIllegalArgExceptionMsg(value)));
    }

    @Override
    public T read(JsonReader jsonReader) throws IOException {
        if (jsonReader.peek() == JsonToken.NULL) {
            jsonReader.nextNull();
            return null;
        } else if (jsonReader.peek() == JsonToken.STRING) {
            return convertToEntityAttribute(Integer.valueOf(jsonReader.nextString()));
        }
        return convertToEntityAttribute(jsonReader.nextInt());
    }

    @Override
    public void write(JsonWriter jsonWriter, T t) throws IOException {
        jsonWriter.value(t == null ? null : convertToDatabaseColumn(t));
    }
}
