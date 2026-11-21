package com.github.kr328.clash

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import com.github.kr328.clash.remote.StatusClient
import com.github.kr328.clash.util.startClashService
import com.github.kr328.clash.util.stopClashService

/**
 * Explicit broadcast entry point for automation apps such as MacroDroid.
 *
 * Use an explicit component targeting:
 * com.github.metacubex.clash.meta/com.github.kr328.clash.ExternalControlReceiver
 */
class ExternalControlReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val action = intent.action ?: return
        if (action !in ACTIONS) {
            Log.w(TAG, "Ignoring unsupported action: $action")
            return
        }

        try {
            val running = StatusClient(context).currentProfile() != null
            when (action) {
                ACTION_START -> {
                    if (!running) start(context)
                    else Log.i(TAG, "START ignored: Clash is already running")
                }
                ACTION_STOP -> {
                    if (running) {
                        context.stopClashService()
                        Log.i(TAG, "STOP requested")
                    } else {
                        Log.i(TAG, "STOP ignored: Clash is not running")
                    }
                }
                ACTION_TOGGLE -> {
                    if (running) {
                        context.stopClashService()
                        Log.i(TAG, "TOGGLE requested stop")
                    } else {
                        start(context)
                    }
                }
            }
        } catch (e: Exception) {
            Log.e(TAG, "Failed to handle action $action", e)
        }
    }

    private fun start(context: Context) {
        val vpnRequest = context.startClashService()
        if (vpnRequest != null) {
            Log.e(TAG, "VPN permission is not prepared; open Clash Meta and start it manually once")
        } else {
            Log.i(TAG, "START requested")
        }
    }

    private companion object {
        const val TAG = "ExternalControlReceiver"
        const val ACTION_START = "com.github.metacubex.clash.meta.action.START_CLASH"
        const val ACTION_STOP = "com.github.metacubex.clash.meta.action.STOP_CLASH"
        const val ACTION_TOGGLE = "com.github.metacubex.clash.meta.action.TOGGLE_CLASH"
        val ACTIONS = setOf(ACTION_START, ACTION_STOP, ACTION_TOGGLE)
    }
}
