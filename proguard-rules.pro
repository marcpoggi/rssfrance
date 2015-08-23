# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in C:/Users/mpoggi/sdk/tools/proguard/proguard-android.txt
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
-dontskipnonpubliclibraryclasses
-dontskipnonpubliclibraryclassmembers
-keepattributes Signature
-keepattributes *Annotation*
-keep class sun.misc.Unsafe { *; }
-keep public class com.feeltest.rssfrance.model.** { *; }
-keepclassmembers class com.feeltest.rssfrance.model.** { *; }
-keep public class com.google.gson.**
-dontwarn com.google.**
-dontwarn com.squareup.picasso.**
-keep public class com.squareup.picasso.** { *; }
-keep public class com.google.code.** { *; }
-dontwarn org.simpleframework.**
-keep public class org.simpleframework.**{ *; }
-keep class org.simpleframework.xml.core.**{ *; }
-keep class org.simpleframework.xml.util.**{ *; }
-dontwarn org.springframework.**
-keep class org.jdom.**{ *; }
-dontwarn org.jdom.**

# Explicitly preserve all serialization members. The Serializable interface
# is only a marker interface, so it wouldn't save them.
-keepclassmembers class * implements java.io.Serializable {
    static final long serialVersionUID;
    private static final java.io.ObjectStreamField[] serialPersistentFields;
    private void writeObject(java.io.ObjectOutputStream);
    private void readObject(java.io.ObjectInputStream);
    java.lang.Object writeReplace();
    java.lang.Object readResolve();
}
# Preserve the special static methods that are required in all enumeration classes.
-keepclassmembers enum * { public static **[] values(); public static ** valueOf(java.lang.String); }


# Here using -keep would NOT work, that's result of several days' tinkering.
# With this the obfuscated app will actually work!
-keepclassmembers public class * {
    public protected *;
}
-keep class * implements android.os.Parcelable {
  public static final android.os.Parcelable$Creator *;
}
