# Repackages optimized classes into %packageName%.repacked package
# in resulting AIX. Repackaging is necessary to avoid clashes with
# the other extensions that might be using same libraries as you.
-repackageclasses %packageName%.repacked

-android
-optimizationpasses 5
-allowaccessmodification
-mergeinterfacesaggressively
-overloadaggressively
-useuniqueclassmembernames
-dontskipnonpubliclibraryclasses
-dontskipnonpubliclibraryclassmember

# Keep Google Mobile Ads classes
-keep class com.google.android.gms.ads.** { *; }
-keep class com.google.ads.** { *; }

# Keep extension classes
-keep class id.kangris.iklanadmob.iklanadmob.** { *; }

-dontwarn com.google.android.gms.ads.**
