# === QRMagic Custom Rules ===

# Registration key logic - in util.RegistrationKey.kt
-keep class com.songbird.qrmagic.util.RegistrationKey {
    *;
}

# QRTheme enum or class - from Theme.kt in /model
-keep class com.songbird.qrmagic.util.QRCodeStylingOptions {
    *;
}

# If QRTheme is an enum, this helps with enum name lookups
-keepclassmembers enum * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}

# MainActivity full path (fixing package case if needed)
-keep class com.songbird.qrmagic.main.MainActivity {
    *;
}

# Keep QRPayload model class if it’s passed between screens
-keep class com.songbird.qrmagic.model.QRPayload {
    *;
}

# Required for Jetpack Compose
-keep class androidx.compose.** { *; }
-dontwarn androidx.compose.**

# Optional: If using SharedPreferences extensions
-keep class androidx.core.content.** { *; }

# Optional: Prevent reflection-related issues
-keepattributes *Annotation*
