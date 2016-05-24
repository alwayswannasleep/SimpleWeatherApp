package vkraevskiy.com.simpleweatherapp.db;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

class TransferStorage {

    private Map<String, Object> objectMap;

    TransferStorage() {
        objectMap = new HashMap<>();
    }

    <T> String saveTransferData(T object) {
        String key = UUID.randomUUID().toString();
        objectMap.put(key, object);

        return key;
    }

    <T> T getTransferData(String key) {
        Object object = objectMap.get(key);

        if (object == null) {
            return null;
        }

        //noinspection unchecked
        return ((T) object);
    }
}
