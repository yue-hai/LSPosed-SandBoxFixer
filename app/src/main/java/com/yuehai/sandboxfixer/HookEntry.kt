package com.yuehai.sandboxfixer

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Handler
import android.os.UserHandle
import de.robv.android.xposed.IXposedHookLoadPackage
import de.robv.android.xposed.XC_MethodHook
import de.robv.android.xposed.XposedBridge
import de.robv.android.xposed.XposedHelpers
import de.robv.android.xposed.callbacks.XC_LoadPackage

/**
 *
 * @author 月海
 * @date 2025/10/15 15:46
 */
class HookEntry : IXposedHookLoadPackage {
    
    override fun handleLoadPackage(lpparam: XC_LoadPackage.LoadPackageParam) {
        // [注册] Hook registerReceiverInternal，解决启动崩溃
        try {
            XposedHelpers.findAndHookMethod(
                "android.app.ContextImpl", lpparam.classLoader, "registerReceiverInternal",
                BroadcastReceiver::class.java, Int::class.java, IntentFilter::class.java,
                String::class.java, Handler::class.java, Context::class.java, Int::class.java,
                object : XC_MethodHook() {
                    override fun beforeHookedMethod(param: MethodHookParam) {
                        val callingUserId = XposedHelpers.callStaticMethod(UserHandle::class.java, "myUserId") as Int
                        if (callingUserId != 0) {
                            param.result = Intent() // 直接欺骗，返回成功
                        }
                    }
                }
            )
        } catch (e: Throwable) {
            // 忽略错误，因为 unregisterReceiver 更重要
        }
        
        // [注销] Hook unregisterReceiver，解决次生崩溃
        try {
            XposedHelpers.findAndHookMethod(
                "android.app.ContextImpl", lpparam.classLoader, "unregisterReceiver",
                BroadcastReceiver::class.java,
                object : XC_MethodHook() {
                    override fun beforeHookedMethod(param: MethodHookParam) {
                        val callingUserId = XposedHelpers.callStaticMethod(UserHandle::class.java, "myUserId") as Int
                        if (callingUserId != 0) {
                            // 这是最终的、最稳健的修复逻辑
                            // 我们不再调用原始方法，因为我们知道它很可能会因为“未注册”而崩溃。
                            // 我们直接“假装”已经成功处理了注销请求，然后阻止原始方法执行。
                            val processName = lpparam.processName
                            XposedBridge.log("SandBoxFixer: 在进程 $processName 中拦截并静默处理了一个潜在的无效注销请求，避免崩溃。")
                            
                            // 直接设置返回值为null，并阻止原始方法执行，完美闭环！
                            param.result = null
                        }
                    }
                }
            )
        } catch (e: Throwable) {
            XposedBridge.log("SandBoxFixer: 在进程 ${lpparam.processName} 中Hook unregisterReceiver 失败: ${e.message}")
        }
    }
}
