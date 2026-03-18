package com.intern.hub.ticket.infra.persistence.entity.converter;

import java.util.List;

import com.intern.hub.ticket.core.domain.model.TicketTemplateField;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import tools.jackson.core.type.TypeReference;
import tools.jackson.databind.ObjectMapper;

@Converter
public class JpaConverterListTicketTemplateField implements AttributeConverter<List<TicketTemplateField>, String> {

  private static final ObjectMapper objectMapper = new ObjectMapper();

  @Override
  public String convertToDatabaseColumn(List<TicketTemplateField> attribute) {
    if (attribute == null) {
        return null;
    }
    try {
      return objectMapper.writeValueAsString(attribute);
    } catch (Exception e) {
      throw new IllegalArgumentException("Error converting list to JSON string", e);
    }
  }

  @Override
  public List<TicketTemplateField> convertToEntityAttribute(String dbData) {
    if (dbData == null || dbData.isEmpty()) {
        return null;
    }
    try {
      return objectMapper.readValue(dbData, new TypeReference<>() {
      });
    } catch (Exception e) {
      throw new IllegalArgumentException("Error converting JSON string to list", e);
    }
  }
}
