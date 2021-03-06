package com.github.salilvnair.utils.rest;

import java.lang.reflect.Type;
import java.util.Collection;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

public class GsonCollectionAdapter implements JsonSerializer<Collection<?>>{

    public JsonElement serialize(Collection<?> src, Type typeOfSrc, JsonSerializationContext context) {
        if (src == null || src.isEmpty()) {
            return null;
        }
        JsonArray array = new JsonArray();
        for (Object child : src) {
            JsonElement element = context.serialize(child);
            array.add(element);
        }

        return array;
    }

}