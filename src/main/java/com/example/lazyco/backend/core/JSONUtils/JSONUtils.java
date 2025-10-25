package com.example.lazyco.backend.core.JSONUtils;

import com.example.lazyco.backend.core.Logger.ApplicationLogger;
import java.util.Iterator;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Utility class for JSON operations including validation, parsing, and cleaning. Provides methods
 * to work with JSONObject and JSONArray instances.
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class JSONUtils {

  /**
   * Validates if a string represents a valid JSON object.
   *
   * @param parameter the string to validate
   * @return true if the string is a valid JSON object, false otherwise
   */
  public static boolean isValidJSONObject(String parameter) {
    if (parameter == null || parameter.trim().isEmpty()) {
      return false;
    }
    try {
      new JSONObject(parameter);
      return true;
    } catch (JSONException e) {
      return false;
    }
  }

  /**
   * Validates if a string represents a valid JSON array.
   *
   * @param parameter the string to validate
   * @return true if the string is a valid JSON array, false otherwise
   */
  public static boolean isValidJSONArray(String parameter) {
    if (parameter == null || parameter.trim().isEmpty()) {
      return false;
    }
    try {
      new JSONArray(parameter);
      return true;
    } catch (JSONException e) {
      return false;
    }
  }

  /**
   * Adds a parameter to a JSON object, automatically parsing the value if it's valid JSON.
   *
   * @param jsonObject the target JSON object
   * @param key the key to add
   * @param value the value to add (will be parsed if it's valid JSON)
   * @throws IllegalArgumentException if jsonObject or key is null
   */
  public static void addParameterToJson(JSONObject jsonObject, String key, String value) {
    if (jsonObject == null) {
      throw new IllegalArgumentException("JSONObject cannot be null");
    }
    if (key == null) {
      throw new IllegalArgumentException("Key cannot be null");
    }

    if (value == null) {
      jsonObject.put(key, JSONObject.NULL);
      return;
    }

    String trimmedValue = value.trim();
    if (isValidJSONObject(trimmedValue)) {
      jsonObject.put(key, new JSONObject(trimmedValue));
    } else if (isValidJSONArray(trimmedValue)) {
      jsonObject.put(key, new JSONArray(trimmedValue));
    } else {
      jsonObject.put(key, value);
    }
  }

  /**
   * Removes integer values and empty/null elements from a JSON object and its nested structures.
   * This method creates a deep copy to avoid modifying the original object.
   *
   * @param jsonObject the JSON object to clean
   * @return a new cleaned JSON object, or null if input is null
   */
  public static JSONObject removeNumbersAndReferences(JSONObject jsonObject) {
    if (jsonObject == null) {
      return null;
    }

    try {
      // Create a deep copy to avoid modifying the original
      JSONObject copy = new JSONObject(jsonObject.toString());
      JSONObject cleaned = removeIntegersFromObject(copy);
      return removeEmptyAndNulls(cleaned);
    } catch (Exception e) {
      ApplicationLogger.debug("Error cleaning JSON object: " + e.getMessage(), e);
      return jsonObject; // Return original if cleaning fails
    }
  }

  /**
   * Removes integer values from a JSON object recursively.
   *
   * @param jsonObject the JSON object to process
   * @return the processed JSON object
   */
  private static JSONObject removeIntegersFromObject(JSONObject jsonObject) {
    if (jsonObject == null) {
      return null;
    }

    try {
      Iterator<String> keys = jsonObject.keys();
      while (keys.hasNext()) {
        String key = keys.next();
        Object value = jsonObject.get(key);

        if (value instanceof JSONArray) {
          JSONArray cleanedArray = removeIntegersFromArray((JSONArray) value);
          jsonObject.put(key, cleanedArray);
        } else if (value instanceof JSONObject) {
          removeIntegersFromObject((JSONObject) value);
        }
      }
    } catch (Exception e) {
      ApplicationLogger.debug("Error removing integers from JSON object: " + e.getMessage(), e);
    }
    return jsonObject;
  }

  /**
   * Removes integer values from a JSON array.
   *
   * @param array the JSON array to process
   * @return a new JSON array without integer values
   */
  private static JSONArray removeIntegersFromArray(JSONArray array) {
    if (array == null) {
      return new JSONArray();
    }

    JSONArray cleanedArray = new JSONArray();
    for (int i = 0; i < array.length(); i++) {
      try {
        Object element = array.get(i);
        if (element instanceof Integer) {
          // Skip integer elements
          continue;
        } else if (element instanceof JSONObject) {
          cleanedArray.put(removeIntegersFromObject((JSONObject) element));
        } else if (element instanceof JSONArray) {
          cleanedArray.put(removeIntegersFromArray((JSONArray) element));
        } else {
          cleanedArray.put(element);
        }
      } catch (Exception e) {
        ApplicationLogger.debug(
            "Error processing array element at index " + i + ": " + e.getMessage(), e);
      }
    }
    return cleanedArray;
  }

  /**
   * Removes empty objects, arrays, and null values from a JSON object recursively.
   *
   * @param jsonObject the JSON object to clean
   * @return the cleaned JSON object, or null if input is null
   */
  public static JSONObject removeEmptyAndNulls(JSONObject jsonObject) {
    if (jsonObject == null) {
      return null;
    }

    try {
      Iterator<String> keys = jsonObject.keys();
      while (keys.hasNext()) {
        String key = keys.next();
        Object value = jsonObject.get(key);

        if (shouldRemoveValue(value)) {
          keys.remove();
        } else if (value instanceof JSONObject) {
          JSONObject cleanedObj = removeEmptyAndNulls((JSONObject) value);
          if (cleanedObj == null || cleanedObj.isEmpty()) {
            keys.remove();
          } else {
            jsonObject.put(key, cleanedObj);
          }
        } else if (value instanceof JSONArray) {
          JSONArray cleanedArray = cleanArray((JSONArray) value);
          if (cleanedArray.isEmpty()) {
            keys.remove();
          } else {
            jsonObject.put(key, cleanedArray);
          }
        }
      }
    } catch (Exception e) {
      ApplicationLogger.debug("Error removing empty and null values: " + e.getMessage(), e);
    }
    return jsonObject;
  }

  /**
   * Determines if a value should be removed (null, empty string, or "null" string).
   *
   * @param value the value to check
   * @return true if the value should be removed
   */
  private static boolean shouldRemoveValue(Object value) {
    return value == null
        || value == JSONObject.NULL
        || (value instanceof String && (((String) value).isEmpty() || "null".equals(value)));
  }

  /**
   * Cleans a JSON array by removing empty and null values recursively.
   *
   * @param array the JSON array to clean
   * @return a new cleaned JSON array
   */
  private static JSONArray cleanArray(JSONArray array) {
    if (array == null) {
      return new JSONArray();
    }

    JSONArray cleanedArray = new JSONArray();
    for (int i = 0; i < array.length(); i++) {
      try {
        Object element = array.get(i);

        if (shouldRemoveValue(element)) {
          continue; // Skip null/empty values
        } else if (element instanceof JSONObject) {
          JSONObject cleanedElement = removeEmptyAndNulls((JSONObject) element);
          if (cleanedElement != null && !cleanedElement.isEmpty()) {
            cleanedArray.put(cleanedElement);
          }
        } else if (element instanceof JSONArray) {
          JSONArray nestedCleaned = cleanArray((JSONArray) element);
          if (!nestedCleaned.isEmpty()) {
            cleanedArray.put(nestedCleaned);
          }
        } else {
          cleanedArray.put(element);
        }
      } catch (Exception e) {
        ApplicationLogger.debug(
            "Error cleaning array element at index " + i + ": " + e.getMessage(), e);
      }
    }
    return cleanedArray;
  }

  /**
   * Removes numbers and references from a JSON string.
   *
   * @param jsonString the JSON string to process
   * @return the cleaned JSON string, or the original string if processing fails
   * @throws IllegalArgumentException if jsonString is null
   */
  public static String removeNumbersAndReferences(String jsonString) {
    if (jsonString == null) {
      throw new IllegalArgumentException("JSON string cannot be null");
    }

    try {
      JSONObject jsonObject = new JSONObject(jsonString);
      JSONObject cleanedJsonObject = removeNumbersAndReferences(jsonObject);
      return cleanedJsonObject != null ? cleanedJsonObject.toString() : jsonString;
    } catch (JSONException e) {
      ApplicationLogger.debug("Invalid JSON string provided: " + e.getMessage(), e);
      return jsonString; // Return original if not valid JSON
    }
  }
}
