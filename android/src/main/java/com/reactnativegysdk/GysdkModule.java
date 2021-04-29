package com.reactnativegysdk;

import android.content.IntentFilter;
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
import com.reactnativegysdk.model.AuthCheckMessage;
import com.reactnativegysdk.model.AuthFailureMessage;
import com.reactnativegysdk.model.AuthSuccessMessage;

@ReactModule(name = GysdkModule.NAME)
public class GysdkModule extends ReactContextBaseJavaModule {
  public static final String NAME = "Gysdk";

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
    if (GYReceiver.gyuid != null) {
      WritableMap map = Arguments.createMap();
      if (GYReceiver.initSuccess) {
        map.putInt("code", GYReceiver.initSuccess ? 200 : -1);
        map.putString("message", GYReceiver.initSuccess ? "初始化成功" : "初始化失败");
      } else {
        map.putInt("code", GYReceiver.code);
        map.putString("message", GYReceiver.message);
      }
      map.putBoolean("success", GYReceiver.initSuccess);
      map.putString("gyuid", GYReceiver.gyuid);
      promise.resolve(map);
      return;
    }
    ReactApplicationContext content = getReactApplicationContext();
    GYReceiver gyReceiver = new GYReceiver(promise);
    IntentFilter filter = new IntentFilter("com.getui.gy.action." + appId);
    String permission = getReactApplicationContext().getPackageName() + ".permission.GYRECEIVER";
    content.registerReceiver(gyReceiver, filter, permission, null);
    GYManager.getInstance().init(content);
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
        AuthCheckMessage message = JSON.parseObject(response.getMsg(), AuthCheckMessage.class);
        result.putString("msg", message.getMsg());
        result.putDouble("expireTime", message.getExpireTime());
        result.putString("operatorType", message.getOperatorType());
        result.putString("processID", message.getProcess_id());
        result.putString("number", message.getNumber());
        result.putString("gyuid", response.getGyuid());
        result.putInt("code", response.getCode());
        result.putBoolean("success", true);
        promise.resolve(result);
        Log.d("=======预登录成功:", "response:" + response);
      }

      @Override
      public void onFailed(GYResponse response) {
        AuthFailureMessage message = JSON.parseObject(response.getMsg(), AuthFailureMessage.class);

        result.putString("metadata", JSON.toJSONString(message.getMetadata()));
        result.putString("msg", message.getMetadata().getError_data());
        result.putString("operatorType", message.getOperatorType());
        result.putString("processID", message.getProcess_id());
        result.putInt("errorCode", message.getErrorCode());
        result.putString("gyuid", response.getGyuid());
        result.putInt("code", response.getCode());
        result.putBoolean("success", false);
        promise.resolve(result);

        Log.d("=======预登录失败:", "response:" + response);
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
      .setLogoImgPath("gt_one_login_logo")
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
      .setNavReturnImgPath("gt_one_login_left_back")
      .setReturnImgOffsetX(Utils.getLeftX(backButtonRect, 12))
      .setReturnImgWidth(Utils.getWidth(backButtonRect, 24))
      .setReturnImgHeight(Utils.getHeight(backButtonRect, 24))
      .setReturnImgOffsetY(Utils.getTopY(backButtonRect, 0));

    GYManager.getInstance().eAccountLogin(builder.build(), new GyCallBack() {
      @Override
      public void onSuccess(GYResponse response) {
        AuthSuccessMessage message = JSON.parseObject(response.getMsg(), AuthSuccessMessage.class);

        result.putDouble("expiredTime", message.getData().getExpiredTime());
        result.putString("token", message.getData().getToken());
        result.putString("gyuid", response.getGyuid());
        result.putInt("code", response.getCode());
        result.putBoolean("success", true);
        promise.resolve(result);

        Log.d("------登录成功", "response:" + response);

        GYManager.getInstance().finishAuthActivity();
      }

      @Override
      public void onFailed(GYResponse response) {
        AuthFailureMessage message = JSON.parseObject(response.getMsg(), AuthFailureMessage.class);
        result.putString("metadata", JSON.toJSONString(message.getMetadata()));
        result.putString("msg", message.getMetadata().getError_data());
        result.putString("operatorType", message.getOperatorType());
        result.putString("processID", message.getProcess_id());
        result.putInt("errorCode", message.getErrorCode());
        result.putString("gyuid", response.getGyuid());
        result.putInt("code", response.getCode());
        result.putBoolean("success", false);
        promise.resolve(result);

        Log.d("------登录失败", "response:" + response);

        GYManager.getInstance().finishAuthActivity();
      }
    });
  }
}
