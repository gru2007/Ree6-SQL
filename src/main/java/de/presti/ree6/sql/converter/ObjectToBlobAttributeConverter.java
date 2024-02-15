package de.presti.ree6.sql.converter;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import jakarta.persistence.AttributeConverter;

import javax.sql.rowset.serial.SerialBlob;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.sql.Blob;
import java.sql.SQLException;

/**
 * An AttributeConverter to allow us the usage of {@link T} in entities.
 */
public class ObjectToBlobAttributeConverter<T> implements AttributeConverter<T, Blob> {

    /**
     * Instance of GSON.
     */
    public Gson gson = new GsonBuilder().setPrettyPrinting().create();

    /**
     * @inheritDoc
     */
    @Override
    public Blob convertToDatabaseColumn(T attribute) {
        try {
            return new SerialBlob(gson.toJson(attribute).getBytes());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * @inheritDoc
     */
    @Override
    public T convertToEntityAttribute(Blob dbData) {
        if (dbData == null)
            return null;

        StringBuilder content = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(dbData.getBinaryStream()))) {

            for (String read; (read = reader.readLine()) != null; ) {
                content.append(read);
            }
        } catch (Exception ignore) {
        }

        if (content.length() == 0)
            return null;

        return gson.fromJson(content.toString(),new TypeToken<T>(){}.getType());
    }
}
