package com.reactnativeesim

import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Build
import android.telephony.euicc.EuiccManager
import android.telephony.euicc.EuiccManager.*
import android.util.Log
import androidx.annotation.RequiresApi
import com.facebook.react.bridge.*

class EsimModule(reactContext: ReactApplicationContext) : ReactContextBaseJavaModule(reactContext) {

  private val DOWNLOAD_ACTION = "download_subscription"
  private val SWITCH_ACTION = "switch_to_subscription"
  private val DELETE_ACTION = "delete_subscription"

  @RequiresApi(Build.VERSION_CODES.P)
  private val euiccManager: EuiccManager =
    reactContext.getSystemService(Context.EUICC_SERVICE) as EuiccManager

  override fun getName(): String {
    return "RNESimManager"
  }

  @ReactMethod
  fun setupEsim(config: ReadableMap, promise: Promise) {
    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.P || !euiccManager.isEnabled) {
      promise.resolve(EsimSetupResultStatus.Fail)
      return
    }
    val esimConfig = EsimConfig(config)
    Log.d("ESIM_DEBUG", esimConfig.address)

    reactApplicationContext.registerReceiver(object : BroadcastReceiver() {
      @RequiresApi(Build.VERSION_CODES.P)
      override fun onReceive(context: Context, intent: Intent) {
        if (intent.action != SWITCH_ACTION) {
          return
        }
        when (intent.getIntExtra(EXTRA_EMBEDDED_SUBSCRIPTION_DETAILED_CODE, 0)) {
          // TODO: handle other errors
          0 -> promise.resolve(EsimSetupResultStatus.Fail)
          else -> promise.resolve(EsimSetupResultStatus.Success)
        }
        reactApplicationContext.unregisterReceiver(this)
      }
    }, IntentFilter(SWITCH_ACTION))

    // Switch to a subscription asynchronously.
    val intent = Intent(SWITCH_ACTION)
    val callbackIntent = PendingIntent.getBroadcast(
      reactApplicationContext, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT
    )
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
      // TODO: figure out subscription id
      euiccManager.switchToSubscription(1, callbackIntent)
    }
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
