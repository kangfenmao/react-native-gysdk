package com.reactnativegysdk;

import android.content.Context;
import android.util.Log;

import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.WritableMap;
import com.g.gysdk.GYResponse;
import com.g.gysdk.GyMessageReceiver;

public class GYReceiver extends GyMessageReceiver {
  private static final String TAG = "GYReceiver";
  public static String gyuid;
  public static boolean initSuccess;
  public static int code;
  public static String message;
  Promise promise;

  public GYReceiver(Promise promise) {
    this.promise = promise;
  }

  /**
   * sdk 初始化结果
   *
   * @param context context.
   * @param result true : 初始化成功。false : 初始化失败。
   */
  @Override
  public void onInit(Context context, boolean result) {
    this.initSuccess = result;
  }

  /**
   * 错误返回。
   *
   * @param context context.
   * @param response .
   */
  @Override
  public void onError(Context context, GYResponse response) {
    WritableMap map = Arguments.createMap();
    map.putInt("code", response.getCode());
    map.putString("message", response.getMsg());
    map.putBoolean("success", false);
    map.putString("gyuid", this.gyuid);
    this.code = response.getCode();
    this.message = response.getMsg();
    promise.resolve(map);
  }

  /**
   * 返回 gyUid.
   * 在每次初始化 SDK 的时候，会通过该接口返回 gyUid.
   *
   * @param context context.
   * @param gyUid gyUid.
   */
  @Override
  public void onGyUidReceived(Context context, String gyUid) {
    this.gyuid = gyUid;
    WritableMap map = Arguments.createMap();
    map.putInt("code", this.initSuccess ? 200 : -1);
    map.putString("message", this.initSuccess ? "初始化成功" : "初始化失败");
    map.putBoolean("success", this.initSuccess);
    map.putString("gyuid", GYReceiver.gyuid);
    promise.resolve(map);
  }
}
