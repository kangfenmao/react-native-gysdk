package com.reactnativegysdk;

import android.graphics.Color;

import com.facebook.react.bridge.ReadableMap;

public class Utils {
  public static int getLeftX(ReadableMap map, int defaultValue) {
    return map.hasKey("leftX") ? map.getInt("leftX") : defaultValue;
  }

  public static int getTopY(ReadableMap map, int defaultValue) {
    return map.hasKey("topY") ? map.getInt("topY") : defaultValue;
  }

  public static int getBottomY(ReadableMap map, int defaultValue) {
    return map.hasKey("bottomY") ? map.getInt("bottomY") : defaultValue;
  }

  public static int getWidth(ReadableMap map, int defaultValue) {
    return map.hasKey("width") ? map.getInt("width") : defaultValue;
  }

  public static int getHeight(ReadableMap map, int defaultValue) {
    return map.hasKey("height") ? map.getInt("height") : defaultValue;
  }

  public static int getColor(ReadableMap map, String key, int defaultValue) {
    return map.hasKey(key) ? Color.parseColor(map.getString(key)) : defaultValue;
  }
}
