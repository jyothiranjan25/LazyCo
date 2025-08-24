package com.example.lazyco.backend.core.JSONUtils;

import com.example.lazyco.backend.core.Logger.ApplicationLogger;
import java.util.Iterator;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class JSONUtils {
  public static boolean isValidJSONObject(String parameter) {
    try {
      new JSONObject(parameter);
    } catch (JSONException e1) {
      return false;
    }
    return true;
  }

  public static boolean isValidJSONArray(String parameter) {
    try {
      new JSONArray(parameter);
    } catch (JSONException e1) {
      return false;
    }
    return true;
  }

  public static void addParameterToJson(JSONObject jsonObject, String key, String value) {
    if (isValidJSONObject(value)) {
      jsonObject.put(key, new JSONObject(value));
    } else if (isValidJSONArray(value)) {
      jsonObject.put(key, new JSONArray(value));
    } else {
      jsonObject.put(key, value);
    }
  }

  public static boolean isValidJSON(String parameter) {
    return isValidJSONObject(parameter) || isValidJSONArray(parameter);
  }

  public static boolean isValidJSON(Object parameter) {
    return parameter instanceof JSONObject
        || (parameter instanceof String
            && (isValidJSONObject((String) parameter) || isValidJSONArray((String) parameter)));
  }

  public static JSONObject removeNumbersAndRef(JSONObject jsonObject) {
    try {
      for (String key : jsonObject.keySet()) {
        Object value = jsonObject.get(key);

        if (value instanceof JSONArray array) {
          for (int i = array.length() - 1; i >= 0; i--) {
            Object element = array.get(i);
            if (element instanceof Integer) {
              array.remove(i); // Remove numeric elements
            } else if (element instanceof JSONObject) {
              removeNumbersAndReferences((JSONObject) element); // Recursively clean nested objects
            }
          }
        } else if (value instanceof JSONObject) {
          removeNumbersAndReferences((JSONObject) value); // Recursively clean nested objects
        }
      }
    } catch (Exception e) {
      ApplicationLogger.debug(e);
    }
    return jsonObject;
  }

  public static JSONObject removeEmptyAndNulls(JSONObject jsonObject) {
    try {
      Iterator<String> keys = jsonObject.keys();
      while (keys.hasNext()) {
        String key = keys.next();
        Object value = jsonObject.get(key);

        if (value instanceof JSONObject) {
          JSONObject cleanedObj = removeEmptyAndNulls((JSONObject) value);
          if (cleanedObj.isEmpty()) {
            keys.remove(); // remove empty object
          } else {
            jsonObject.put(key, cleanedObj);
          }
        } else if (value instanceof JSONArray array) {
          JSONArray cleanedArray = new JSONArray();
          for (int i = 0; i < array.length(); i++) {
            Object element = array.get(i);
            if (element instanceof JSONObject) {
              JSONObject cleanedElement = removeEmptyAndNulls((JSONObject) element);
              if (!cleanedElement.isEmpty()) {
                cleanedArray.put(cleanedElement);
              }
            } else if (element instanceof JSONArray) {
              JSONArray cleanedNestedArray = cleanArray((JSONArray) element);
              if (!cleanedNestedArray.isEmpty()) {
                cleanedArray.put(cleanedNestedArray);
              }
            } else if (element != null
                && element != JSONObject.NULL
                && !(element instanceof String && ((String) element).isEmpty())) {
              cleanedArray.put(element);
            }
          }
          if (cleanedArray.isEmpty()) {
            keys.remove(); // remove key if array is empty after cleaning
          } else {
            jsonObject.put(key, cleanedArray);
          }
        } else if (value == null
            || value == JSONObject.NULL
            || (value instanceof String && ("null".equals(value) || ((String) value).isEmpty()))) {
          keys.remove(); // remove null or empty string
        }
      }
    } catch (Exception e) {
      ApplicationLogger.debug(e.getMessage(), e.getClass());
    }
    return jsonObject;
  }

  private static JSONArray cleanArray(JSONArray array) {
    JSONArray cleanedArray = new JSONArray();
    for (int i = 0; i < array.length(); i++) {
      Object element = array.get(i);
      if (element instanceof JSONObject) {
        JSONObject cleanedElement = removeEmptyAndNulls((JSONObject) element);
        if (!cleanedElement.isEmpty()) {
          cleanedArray.put(cleanedElement);
        }
      } else if (element instanceof JSONArray) {
        JSONArray nestedCleaned = cleanArray((JSONArray) element);
        if (!nestedCleaned.isEmpty()) {
          cleanedArray.put(nestedCleaned);
        }
      } else if (element != null && !(element instanceof String && ((String) element).isEmpty())) {
        cleanedArray.put(element);
      }
    }
    return cleanedArray;
  }

  public static JSONObject removeNumbersAndReferences(JSONObject jsonObject) {
    JSONObject object = removeNumbersAndRef(jsonObject);
    return removeEmptyAndNulls(object);
  }

  public static String removeNumbersAndReferences(String jsonString) {
    JSONObject jsonObject = new JSONObject(jsonString);
    JSONObject cleanedJsonObject = removeNumbersAndReferences(jsonObject);
    return cleanedJsonObject.toString();
  }

  public static String removeNumbersAndReferences(Object jsonObject) {
    if (jsonObject instanceof JSONObject) {
      return removeNumbersAndReferences(jsonObject.toString());
    } else if (jsonObject instanceof String) {
      return removeNumbersAndReferences((String) jsonObject);
    } else {
      throw new IllegalArgumentException("Invalid JSON object provided");
    }
  }
}
