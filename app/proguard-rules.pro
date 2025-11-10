# Add project specific ProGuard rules here.

# Keep Netty classes
-keep class io.netty.** { *; }
-dontwarn io.netty.**

# Keep CloudburstMC classes
-keep class org.cloudburstmc.** { *; }
-dontwarn org.cloudburstmc.**

# Keep all annotations
-keepattributes *Annotation*

# Keep source file and line numbers for debugging
-keepattributes SourceFile,LineNumberTable
