package de.presti.ree6.sql.converter;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import jakarta.persistence.AttributeConverter;

import javax.sql.rowset.serial.SerialBlob;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.sql.Blob;
import java.sql.SQLException;
import java.util.Collections;
import java.util.List;

public class ListAttributeConverter<R> implements AttributeConverter<List<R>, Blob> {

    /**
     * Instance of GSON.
     */
    public static Gson gson = new GsonBuilder().setPrettyPrinting().create();

    @Override
    public Blob convertToDatabaseColumn(List<R> attribute) {
        try {
            return new SerialBlob(gson.toJson(attribute).getBytes());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<R> convertToEntityAttribute(Blob dbData) {
        if (dbData == null)
            return Collections.emptyList();

        Type typeOfT = TypeToken.getParameterized(List.class, getClass().getTypeParameters()[0]).getType();

        StringBuilder content = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(dbData.getBinaryStream()))) {

            for (String read; (read = reader.readLine()) != null; ) {
                content.append(read);
            }
        } catch (Exception ignore) {
        }

        if (content.length() == 0)
            return Collections.emptyList();

        return gson.fromJson(content.toString(), typeOfT);
    }
}
