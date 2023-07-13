[![](https://jitpack.io/v/U-WHY/Android-NtpTime.svg)](https://jitpack.io/#U-WHY/Android-NtpTime)

# Install

**Step 1.** Add the JitPack repository to your build file

Add it in your root build.gradle at the end of repositories:

```css
    allprojects {
        repositories {
            ...
            maven { url 'https://jitpack.io' }
        }
    }
```

**Step 2.** Add the dependency

```css
    dependencies {
            implementation 'com.github.U-WHY:Android-NtpTime:1.0.0'
    }
```



# Usage

For Kotlin

```kotlin
// Get the NTP timestamp, throw an exception if synchronization is not completed.
NtpTime.timeMillis

// Get the NTP timestamp, return the system timestamp if synchronization is not completed.
NtpTime.safeTimeMillis
```

For Java

```java
// Get the NTP timestamp, throw an exception if synchronization is not completed.
NtpTime.INSTANCE.getTimeMillis();

// Get the NTP timestamp, return the system timestamp if synchronization is not completed.
NtpTime.INSTANCE.getSafeTimeMillis();
```





## NTP server configuration

Generally, NTP Time automatically synchronizes time using the default NTP time servers.

The following are the default NTP time servers used by NTP Time.

**time.android.com**

**time.google.com**

**pool.ntp.org**

**time.windows.com**

If needed, you can set the NTP time servers that NTP Time relies on by invoking methods or configuring the AndroidManifest.

### 1.modify in code

Using the `syncTime(vararg ntpServers: String)` method.

For Kotlin

```kotlin
NtpTime.syncTime("ntp.host1.com","ntp.host2.com","ntp.host3.com"...)
```

For Java

```java
NtpTime.INSTANCE.syncTime("ntp.host1.com","ntp.host2.com","ntp.host3.com"...)
```

Alternatively, use`syncTime(ntpConfig: NtpConfig)`for more detailed settings.

## 2.modify in AndroidManifest

Set the NTP server list like that.

```xml
<?xml version="1.0" encoding="utf-8"?>
<manifest>
    <application>
        ...
        <meta-data android:name="KNTP_LIST" android:value="ntp.host1.com,ntp.host2.com"/>
    </application>
</manifest>
```

or set the NTP server list by referencing a string array resource.

```xml
<?xml version="1.0" encoding="utf-8"?>
<manifest>
    <application>
        ...
        <meta-data android:name="KNTP_LIST" android:value="@array/ntp_hosts_array"/>
    </application>
</manifest>
```

```xml
<?xml version="1.0" encoding="utf-8"?>
<resources>
    <!--  Define a string array in the resource file  -->
    <array name="ntp_hosts_array">
        <item>ntp.host1.com</item>
        <item>ntp.host2.com</item>
    </array>
</resources>
```

# README

[English](./README.md)

[中文](./README-ZH.md)
