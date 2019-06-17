package org.digitalhealthintegration.utils;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.facebook.react.bridge.ActivityEventListener;
import com.facebook.react.bridge.BaseActivityEventListener;
import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.ReadableArray;
import com.facebook.react.bridge.WritableArray;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.bridge.WritableNativeArray;
import com.facebook.react.bridge.WritableNativeMap;

import java.util.Iterator;
import java.util.Set;
import java.util.logging.Logger;

public class IntentModule extends ReactContextBaseJavaModule {
  private Promise responsePromise;

  public IntentModule(ReactApplicationContext reactContext) {
    super(reactContext);
    reactContext.addActivityEventListener(mActivityEventListener);
  }

  @Override
  public String getName() {
    return "RNIntentModule"; // name to use this when calling from JS
  }

  private static WritableMap convertJsonToMap(JSONObject jsonObject) throws JSONException {
    WritableMap map = new WritableNativeMap();

    Iterator<String> iterator = jsonObject.keys();
    while (iterator.hasNext()) {
      String key = iterator.next();
      Object value = jsonObject.get(key);
      if (value instanceof JSONObject) {
        map.putMap(key, convertJsonToMap((JSONObject) value));
      } else if (value instanceof JSONArray) {
        map.putArray(key, convertJsonToArray((JSONArray) value));
      } else if (value instanceof Boolean) {
        map.putBoolean(key, (Boolean) value);
      } else if (value instanceof Integer) {
        map.putInt(key, (Integer) value);
      } else if (value instanceof Double) {
        map.putDouble(key, (Double) value);
      } else if (value instanceof String) {
        map.putString(key, (String) value);
      } else {
        map.putString(key, value.toString());
      }
    }
    return map;
  }

  private static final JSONObject bundleToJson(Bundle bundle) {
    JSONObject json = new JSONObject();
    Set<String> keys = bundle.keySet();
    for (String key : keys) {
      try{
        json.put(key, bundle.get(key));
      }catch (Exception e){
        e.printStackTrace();
      }
    }
    return json;
  }

  private static WritableArray convertJsonToArray(JSONArray jsonArray) throws JSONException {
    WritableArray array = new WritableNativeArray();

    for (int i = 0; i < jsonArray.length(); i++) {
      Object value = jsonArray.get(i);
      if (value instanceof JSONObject) {
        array.pushMap(convertJsonToMap((JSONObject) value));
      } else if (value instanceof JSONArray) {
        array.pushArray(convertJsonToArray((JSONArray) value));
      } else if (value instanceof Boolean) {
        array.pushBoolean((Boolean) value);
      } else if (value instanceof Integer) {
        array.pushInt((Integer) value);
      } else if (value instanceof Double) {
        array.pushDouble((Double) value);
      } else if (value instanceof String) {
        array.pushString((String) value);
      } else {
        array.pushString(value.toString());
      }
    }
    return array;
  }

  private final ActivityEventListener mActivityEventListener = new BaseActivityEventListener() {
    @Override
    public void onActivityResult(Activity activity, int requestCode, int resultCode, Intent data) {
      try {
        Bundle dataBundle = data.getExtras();
        JSONObject jsonObj = bundleToJson(dataBundle);
        if (responsePromise != null) {
          WritableMap dataToReturn = convertJsonToMap(jsonObj);
          responsePromise.resolve(dataToReturn);
          responsePromise = null;
        }
      } catch (JSONException e) {
        e.printStackTrace();
        if (responsePromise != null) {
          responsePromise.reject(e);
          responsePromise = null;
        }
      }
    }
  };

  @ReactMethod
  public void openIntent(String action, String title, ReadableArray extraKey, ReadableArray extraValue,
                         String requestCode, String type, final Promise promise) {
    Activity currentActivity = getCurrentActivity();

    if (currentActivity == null) {
      promise.reject("Current Activity doesn't exist");
      return;
    }
    responsePromise = promise;
    try {
      Intent sendIntent = new Intent();
      sendIntent.setAction(action);

      if (extraKey != null) {
        for (int i = 0; i < extraKey.size(); i++) {
          sendIntent.putExtra(extraKey.getString(i), extraValue.getString(i)); // loop here to read all keys and value
        }
      }
      sendIntent.setType(type);
      Intent chooser = Intent.createChooser(sendIntent, title);
      if (sendIntent.resolveActivity(getReactApplicationContext().getPackageManager()) != null) {
        getReactApplicationContext().startActivityForResult(chooser, Integer.parseInt(requestCode), Bundle.EMPTY);
      }
    } catch (Exception e) {
      responsePromise.reject(e);
      responsePromise = null;
    }
  }
}