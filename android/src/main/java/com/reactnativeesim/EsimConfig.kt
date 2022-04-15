package com.reactnativeesim

import com.facebook.react.bridge.ReadableMap

class EsimConfig(config: ReadableMap) {
  val address = if (config.hasKey("address")) config.getString("address") ?: "" else ""
  val confirmationCode =
    if (config.hasKey("confirmationCode")) config.getString("confirmationCode") ?: "" else ""
  val eid = if (config.hasKey("eid")) config.getString("eid") ?: "" else ""
  val iccid = if (config.hasKey("iccid")) config.getString("iccid") ?: "" else ""
  val matchingId = if (config.hasKey("matchingId")) config.getString("matchingId") ?: "" else ""
  val oid = if (config.hasKey("oid")) config.getString("oid") ?: "" else ""
}
