package de.presti.ree6.sql.converter;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import java.util.Base64;

/**
 * An AttributeConverter to allow us the usage of byte arrays in entities.
 */
@Converter(autoApply = false)
public class ByteToStringAttributeConverter implements AttributeConverter<byte[], String> {

    /**
     * @inheritDoc
     */
    @Override
    public byte[] convertToEntityAttribute(String attribute) {
        return Base64.getDecoder().decode(attribute);
    }

    /**
     * @inheritDoc
     */
    @Override
    public String convertToDatabaseColumn(byte[] dbData) {
        return Base64.getEncoder().encodeToString(dbData);
    }
}
