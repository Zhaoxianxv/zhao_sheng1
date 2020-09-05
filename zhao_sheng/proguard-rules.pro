# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in F:\sdk/tools/proguard/proguard-android.txt
# You can edit the include path and order by changing the proguardFiles
# directive in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# Add any project specific keep options here:

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}
# 不优化
-dontoptimize

# 代码循环优化次数，0-7，默认为5
-optimizationpasses 5

# 包名不使用大小写混合 aA Aa
-dontusemixedcaseclassnames

# 不混淆第三方引用的库
-dontskipnonpubliclibraryclasses

# 不做预校验
-dontpreverify

# 忽略警告
-ignorewarning
-dontwarn

# 混淆时所采用的优化规则
-optimizations !code/simplification/arithmetic,!field/*,!class/merging/*

# 关闭log
-assumenosideeffects class android.util.Log {
    public static boolean isLoggable(java.lang.String, int);
    public static int v(...);
    public static int i(...);
    public static int w(...);
    public static int d(...);
    public static int e(...);
}


# 混淆后生产映射文件 map 类名->转化后类名的映射
# 存放在app\build\outputs\mapping\release中
-verbose

# 混淆前后的映射
-printmapping mapping.txt

# apk 包内所有 class 的内部结构
-dump class_files.txt

# 未混淆的类和成员
-printseeds seeds.txt

# 列出从 apk 中删除的代码
-printusage unused.txt
# 抛出异常时保留代码行号
# 这个最后release的时候关闭掉
-keepattributes SourceFile,LineNumberTable
-keep public class * extends android.app.Fragment
-keep public class * extends android.app.Activity
-keep public class * extends android.app.Application
-keep public class * extends android.app.Service
-keep public class * extends android.content.BroadcastReceiver
-keep public class * extends android.content.ContentProvider
-keep public class * extends android.app.backup.BackupAgentHelper
-keep public class * extends android.preference.Preference
# 如果有引用v4包可以添加下面这行
-keep public class * extends android.support.v4.app.Fragment

# 如果引用了v4或者v7包
-dontwarn android.support.**
# 保持 native 方法不被混淆
-keepclasseswithmembernames class * {
    native <methods>;
}


#保留Keep注解的类名和方法
#-keep,allowobfuscation @interface android.support.annotation.Keep
#-keep @android.support.annotation.Keep class *
#-keepclassmembers class * {
#    @android.support.annotation.Keep *;
#}
# 对于带有回调函数的onXXEvent、**On*Listener的，不能被混淆
#-keepclassmembers class * {
#    void *(**On*Event);
#    void *(**On*Listener);
#}
# 保护注解
-keepattributes *Annotation*
-keep class * extends java.lang.annotation.Annotation {*;}

# 泛型与反射
-keepattributes Signature
-keepattributes EnclosingMethod

# 不混淆内部类
-keepattributes InnerClasses
#保持所有实现 Serializable 接口的类成员
-keepclassmembers class * implements java.io.Serializable {
    static final long serialVersionUID;
    private static final java.io.ObjectStreamField[] serialPersistentFields;
    private void writeObject(java.io.ObjectOutputStream);
    private void readObject(java.io.ObjectInputStream);
    java.lang.Object writeReplace();
    java.lang.Object readResolve();
}
# 保留Parcelable序列化类不被混淆
-keep class * implements android.os.Parcelable {
    public static final android.os.Parcelable$Creator *;
}



#不混淆某个类
#-keep public class com.biaobiao.example.Test { *; }
#不混淆某个包所有的类
-dontwarn butterknife.internal.**
-keep class **$$ViewInjector { *; }
-keepnames class * { @butterknife.InjectView *;}


# rx
-dontwarn rx.**
-keepclassmembers class rx.** { *; }
# retrolambda
-dontwarn java.lang.invoke.*
#glide
-keep public class * implements com.bumptech.glide.module.GlideModule
-keep public enum com.bumptech.glide.load.resource.bitmap.ImageHeaderParser$** {
  **[] $VALUES;
  public *;
}


#==================gson==========================
-dontwarn com.google.**
-keep class com.google.gson.** {*;}

#==================protobuf======================
-dontwarn com.google.**
-keep class com.google.protobuf.** {*;}

