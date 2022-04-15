package com.reactnativeesim

import android.content.Context
import android.os.Build
import android.telephony.euicc.EuiccManager
import androidx.annotation.RequiresApi
import com.facebook.react.bridge.Promise
import com.facebook.react.bridge.ReactApplicationContext
import com.facebook.react.bridge.ReactContextBaseJavaModule
import com.facebook.react.bridge.ReactMethod
import com.facebook.react.bridge.ReadableMap

class EsimModule(reactContext: ReactApplicationContext) : ReactContextBaseJavaModule(reactContext) {

  @RequiresApi(Build.VERSION_CODES.P)
  private var euiccManager: EuiccManager =
    reactContext.getSystemService(Context.EUICC_SERVICE) as EuiccManager

  override fun getName(): String {
    return "RNESimManager"
  }

  @ReactMethod
  fun setupEsim(config: ReadableMap, promise: Promise) {
    promise.resolve(true)
  }

  @ReactMethod
  fun isEsimSupported(promise: Promise) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
      promise.resolve(euiccManager.isEnabled)
    } else {
      promise.resolve(false)
    }
  }
}
