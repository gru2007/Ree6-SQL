package de.presti.ree6.sql.converter;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import javax.sql.rowset.serial.SerialBlob;
import java.sql.Blob;
import java.sql.SQLException;

/**
 * An AttributeConverter to allow us the usage of byte arrays in entities.
 */
@Converter(autoApply = false)
public class ByteToBlobAttributeConverter implements AttributeConverter<byte[], Blob> {

    /**
     * @inheritDoc
     */
    @Override
    public byte[] convertToEntityAttribute(Blob attribute) {
        try {
            return attribute.getBytes(1, (int) attribute.length());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * @inheritDoc
     */
    @Override
    public Blob convertToDatabaseColumn(byte[] dbData) {
        try {
            return new SerialBlob(dbData);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
