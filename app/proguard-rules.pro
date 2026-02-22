# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

# Uncomment this to preserve the line number information for
# debugging stack traces.
#-keepattributes SourceFile,LineNumberTable

# If you keep the line number information, uncomment this to
# hide the original source file name.
#-renamesourcefileattribute SourceFile

# 保留数据类
-keep class com.sjshb57.kana.model.Kana { *; }

# 保留自定义View（XML里引用）
-keep class com.sjshb57.kana.view.** { *; }

# 保留Fragment（FragmentManager用类名反射）
-keep class com.sjshb57.kana.module.** extends androidx.fragment.app.Fragment { *; }

# 保留Activity
-keep class com.sjshb57.kana.module.*.** extends android.app.Activity { *; }
-keep class com.sjshb57.kana.** extends androidx.appcompat.app.AppCompatActivity { *; }

# 移除日志
-assumenosideeffects class android.util.Log {
    public static int v(...);
    public static int d(...);
    public static int i(...);
    public static int w(...);
    public static int e(...);
}

# 激进优化
-optimizationpasses 5
-dontusemixedcaseclassnames
-allowaccessmodification
-mergeinterfacesaggressively