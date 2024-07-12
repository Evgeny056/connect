package com.connectpublications.serializator;

import com.connectpublications.model.entity.User;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;
import java.util.List;

public class UserListSerializer extends JsonSerializer<List<User>> {

    @Override
    public void serialize(List<User> users, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        jsonGenerator.writeStartArray();
        for (User user : users) {
            jsonGenerator.writeStartObject();
            jsonGenerator.writeStringField("userId", user.getUserId().toString());
            jsonGenerator.writeStringField("firstName", user.getFirstName());
            jsonGenerator.writeStringField("lastName", user.getLastName());
            // Добавьте другие необходимые поля для сериализации
            jsonGenerator.writeEndObject();
        }
        jsonGenerator.writeEndArray();
    }
}
