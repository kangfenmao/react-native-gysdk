package com.reactnativegysdk;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;

import androidx.annotation.NonNull;

import com.alibaba.fastjson.JSON;
import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.ReadableArray;
import com.facebook.react.bridge.ReadableMap;
import com.facebook.react.bridge.WritableArray;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.module.annotations.ReactModule;
import com.g.gysdk.GYManager;
import com.g.gysdk.GYResponse;
import com.g.gysdk.GyCallBack;
import com.g.gysdk.cta.ELoginThemeConfig;
import com.reactnativegysdk.model.AuthExceptionMessage;
import com.reactnativegysdk.model.AuthCheckMessage;
import com.reactnativegysdk.model.AuthFailureMessage;
import com.reactnativegysdk.model.AuthSuccessMessage;

@ReactModule(name = GysdkModule.NAME)
public class GysdkModule extends ReactContextBaseJavaModule {
  public static final String NAME = "Gysdk";
  private final String TAG = "GYSDK";

  public GysdkModule(ReactApplicationContext reactContext) {
    super(reactContext);
  }

  @Override
  @NonNull
  public String getName() {
    return NAME;
  }

  @ReactMethod
  public void init(String appId, Promise promise) {
    Context context = getReactApplicationContext();
    WritableMap map = Arguments.createMap();
    GYManager.getInstance().init(context, new GyCallBack() {
      @Override
      public void onSuccess(GYResponse response) {
        Log.d(TAG, "初始化成功 response:" + response);
        map.putInt("code", response.getCode());
        map.putString("message", "初始化成功 response:" + response.getMsg());
        map.putString("gyuid", response.getGyuid());
        promise.resolve(map);
      }

      @Override
      public void onFailed(GYResponse response) {
        Log.e(TAG, "初始化失败 response:" + response);
        map.putInt("code", response.getCode());
        map.putString("message", "初始化失败 response:" +  response.getMsg());
        map.putString("gyuid", response.getGyuid());
        promise.resolve(map);
      }
    });
  }

  @ReactMethod
  public void debug(boolean debug) {
    GYManager.getInstance().setDebug(debug);
  }

  @ReactMethod
  public void check(ReadableMap config, Promise promise) {
    WritableMap result = Arguments.createMap();
    int timeout = config.hasKey("timeout") ? config.getInt("timeout") * 1000 : 5000;

    GYManager.getInstance().ePreLogin(timeout, new GyCallBack() {
      @Override
      public void onSuccess(GYResponse response) {
        Log.d(TAG + ":预登录成功:", "response:" + response);

        AuthCheckMessage message = JSON.parseObject(response.getMsg(), AuthCheckMessage.class);
        result.putString("msg", message.getMsg());
        result.putDouble("expireTime", message.getExpiredTime());
        result.putString("operatorType", message.getOperatorType());
        result.putString("processID", message.getProcess_id());
        result.putString("number", message.getNumber());
        result.putString("gyuid", response.getGyuid());
        result.putInt("code", response.getCode());
        result.putBoolean("success", true);
        promise.resolve(result);
      }

      @Override
      public void onFailed(GYResponse response) {
        Log.e(TAG + ":预登录失败:", "response:" + response);

        AuthFailureMessage message = JSON.parseObject(response.getMsg(), AuthFailureMessage.class);
        String errorData = message.getMetadata().getString("error_data");
        String errorMsg = message.getMetadata().getString("msg");

        result.putString("metadata", message.getMetadata().toString());
        result.putString("msg", errorData != null ? errorData : errorMsg);
        result.putString("operatorType", message.getOperatorType());
        result.putString("processID", message.getProcess_id());
        result.putInt("errorCode", message.getErrorCode());
        result.putString("gyuid", response.getGyuid());
        result.putInt("code", response.getCode());
        result.putBoolean("success", false);
        promise.resolve(result);
      }
    });
  }

  @ReactMethod
  public void login(ReadableMap config, Promise promise) {
    WritableMap result = Arguments.createMap();

    ELoginThemeConfig.Builder builder = new ELoginThemeConfig.Builder();

    ReadableMap logoRect = config.getMap("logoRect");
    ReadableMap phoneNumRect = config.getMap("phoneNumRect");
    ReadableMap authButtonRect = config.getMap("authButtonRect");
    ReadableMap switchButtonRect = config.getMap("switchButtonRect");
    ReadableMap sloganRect = config.getMap("sloganRect");
    ReadableMap termsRect = config.getMap("termsRect");
    ReadableMap backButtonRect = config.getMap("backButtonRect");

    WritableArray privacyClauseDefaultText = Arguments.createArray();
    privacyClauseDefaultText.pushString("登录即表示同意");
    privacyClauseDefaultText.pushString("与");
    privacyClauseDefaultText.pushString("、");
    privacyClauseDefaultText.pushString("");
    ReadableArray privacyClauseText = config.hasKey("auxiliaryPrivacyWords") ? config.getArray("auxiliaryPrivacyWords") : privacyClauseDefaultText;

    String clauseNameOne =  config.hasKey("agreements") ? config.getArray("agreements").getMap(0).getString("title") : null;
    String clauseUrlOne =  config.hasKey("agreements") ? config.getArray("agreements").getMap(0).getString("url") : null;
    String clauseNameTwo = config.hasKey("agreements") ? config.getArray("agreements").getMap(1).getString("title") : null;
    String clauseUrlTwo = config.hasKey("agreements") ? config.getArray("agreements").getMap(1).getString("url") : null;

    builder
      .setLogoOffsetX(Utils.getLeftX(logoRect, 0))
      .setLogoOffsetY(Utils.getTopY(logoRect, 125))
      .setLogoOffsetY_B(Utils.getBottomY(logoRect, 0))
      .setLogoWidth(Utils.getWidth(logoRect, 71))
      .setLogoHeight(Utils.getHeight(logoRect, 71))
      .setNumFieldOffsetX(Utils.getLeftX(phoneNumRect, 0))
      .setNumFieldOffsetY(Utils.getTopY(phoneNumRect, 200))
      .setNumFieldOffsetY_B(Utils.getBottomY(phoneNumRect, 0))
      .setNumberSize(config.hasKey("phoneNumFontSize") ? config.getInt("phoneNumFontSize") : 24)
      .setNumberColor(Utils.getColor(config, "phoneNumColor", 0xFF3D424C))
      .setLoginButtonText(config.hasKey("authButtonTitle") ? config.getString("authButtonTitle") : "一键登录")
      .setLoginButtonColor(Utils.getColor(config, "authButtonColor", 0xFFFFFFFF))
      .setLogBtnOffsetX(Utils.getLeftX(authButtonRect, 0))
      .setLogBtnOffsetY(Utils.getTopY(authButtonRect, 324))
      .setLogBtnOffsetY_B(Utils.getBottomY(authButtonRect, 0))
      .setLogBtnWidth(Utils.getWidth(authButtonRect, 268))
      .setLogBtnHeight(Utils.getHeight(authButtonRect, 36))
      .setSwitchOffsetX(Utils.getLeftX(switchButtonRect, 0))
      .setSwitchAccOffsetY(Utils.getTopY(switchButtonRect, 249))
      .setSwitchOffsetY_B(Utils.getBottomY(switchButtonRect, 0))
      .setSwitchColor(Utils.getColor(config, "switchButtonColor", 0xFF3973FF))
      .setSwitchSize(config.hasKey("switchButtonFontSize") ? config.getInt("switchButtonFontSize") : 14)
      .setSwitchText(config.hasKey("switchButtonText") ? config.getString("switchButtonText") : "切换账号")
      .setSloganOffsetX(Utils.getLeftX(sloganRect, 0))
      .setSloganOffsetY(Utils.getTopY(sloganRect, 382))
      .setSloganOffsetY_B(Utils.getBottomY(sloganRect, 0))
      .setSloganColor(Utils.getColor(config, "sloganColor", 0xFFA8A8A8))
      .setSloganSize(config.hasKey("sloganFontSize") ? config.getInt("sloganFontSize") : 10)
      .setPrivacyTextView(
        privacyClauseText.getString(0),
        privacyClauseText.getString(1),
        privacyClauseText.getString(2),
        privacyClauseText.getString(3))
      .setStatusBar(Color.WHITE, Color.WHITE, true)
      .setAuthNavLayout(Color.WHITE, 49, true, false)
      .setPrivacyLayoutWidth(Utils.getWidth(termsRect, 256))
      .setPrivacyClauseText(null, null, clauseNameOne,  clauseUrlOne, clauseNameTwo, clauseUrlTwo)
      .setStatusBar(Color.WHITE, Color.WHITE, true)
      .setAuthNavLayout(Color.WHITE, 49, true, false)
      .setReturnImgOffsetX(Utils.getLeftX(backButtonRect, 12))
      .setReturnImgWidth(Utils.getWidth(backButtonRect, 24))
      .setReturnImgHeight(Utils.getHeight(backButtonRect, 24))
      .setReturnImgOffsetY(Utils.getTopY(backButtonRect, 0))
      .setLogoImgPath("gt_one_login_logo")
      .setLoginImgPath("gt_one_login_btn_normal")
      .setNavReturnImgPath("gt_one_login_left_back");

    GYManager.getInstance().eAccountLogin(builder.build(), new GyCallBack() {
      @Override
      public void onSuccess(GYResponse response) {
        Log.d(TAG + ":登录成功", "response:" + response);

        AuthSuccessMessage message = JSON.parseObject(response.getMsg(), AuthSuccessMessage.class);

        result.putDouble("expiredTime", message.getData().getExpiredTime());
        result.putString("token", message.getData().getToken());
        result.putString("gyuid", response.getGyuid());
        result.putInt("code", response.getCode());
        result.putBoolean("success", true);
        promise.resolve(result);

        GYManager.getInstance().finishAuthActivity();
      }

      @Override
      public void onFailed(GYResponse response) {
        Log.e(TAG + ":登录失败", "response:" + response);

        if (response.getMsg().indexOf("process_id") == -1) {
          AuthExceptionMessage message = JSON.parseObject(response.getMsg(), AuthExceptionMessage.class);
          result.putBoolean("success", false);
          result.putInt("code", response.getCode());
          result.putInt("errorCode", message.getErrorCode());
          result.putString("metadata", message.getMetadata());
          result.putString("msg", message.getErrorDesc());
          result.putString("operatorType", response.getOperator());
          result.putString("gyuid", response.getGyuid());
          promise.resolve(result);
          GYManager.getInstance().finishAuthActivity();
          return;
        }

        AuthFailureMessage message = JSON.parseObject(response.getMsg(), AuthFailureMessage.class);
        String errorData = message.getMetadata().getString("error_data");
        String errorMsg = message.getMetadata().getString("msg");

        result.putInt("code", response.getCode());
        result.putInt("errorCode", message.getErrorCode());
        result.putBoolean("success", false);
        result.putString("metadata", message.getMetadata().toString());
        result.putString("msg", errorData != null ? errorData : errorMsg);
        result.putString("operatorType", message.getOperatorType());
        result.putString("processID", message.getProcess_id());
        result.putString("gyuid", response.getGyuid());

        promise.resolve(result);

        GYManager.getInstance().finishAuthActivity();
      }
    });
  }
}
