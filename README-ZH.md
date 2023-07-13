[![](https://jitpack.io/v/U-WHY/Android-NtpTime.svg)](https://jitpack.io/#U-WHY/Android-NtpTime)

# 如何接入NTP Time

**第一步.** 添加jitpack仓库

在你的项目根目录下的 build.gradle 中添加 jitpack 仓库

```css
    allprojects {
        repositories {
            ...
            maven { url 'https://jitpack.io' }
        }
    }
```

**第二步.** 添加依赖

```css
    dependencies {
            implementation 'com.github.U-WHY:Android-NtpTime:1.0.0'
    }
```



# 如何获取时间

对于Kotlin

```kotlin
// 获取NTP同步时间戳，如果此时没有同步完成，则抛出异常。
NtpTime.timeMillis

// 获取NTP同步时间戳，如果此时没有同步完成，则返回系统时间。
NtpTime.safeTimeMillis
```

对于Java

```java
// 获取NTP同步时间戳，如果此时没有同步完成，则抛出异常。
NtpTime.INSTANCE.getTimeMillis();

// 获取NTP同步时间戳，如果此时没有同步完成，则返回系统时间。
NtpTime.INSTANCE.getSafeTimeMillis();
```





## NTP服务器设置

一般来说，NTP Time 会自动同步时间，以下是NTP Time默认使用的NTP 时间服务器。

**time.android.com**

**time.google.com**

**pool.ntp.org**

**time.windows.com**

如果你需要，也可以通过以下方法改变NTP Time所依赖的NTP时间服务器

### 1、通过代码修改NTP服务器列表

通过`syncTime(vararg ntpServers: String)`方法修改服务器列表

对于Kotlin

```kotlin
NtpTime.syncTime("ntp.host1.com","ntp.host2.com","ntp.host3.com"...)
```

对于Java

```java
NtpTime.INSTANCE.syncTime("ntp.host1.com","ntp.host2.com","ntp.host3.com"...)
```

或是使用`syncTime(ntpConfig: NtpConfig)`进行更加细致的设置

### 2、通过*AndroidManifest*中的*meta-data*标签配置

直接设置NTP服务器列表

```xml
<?xml version="1.0" encoding="utf-8"?>
<manifest>
    <application>
        ...
        <meta-data android:name="KNTP_LIST" android:value="ntp.host1.com,ntp.host2.com"/>
    </application>
</manifest>
```

或是通过引用字符串数组资源设置NTP服务器列表

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
    <!--  在资源文件中定义字符串数组  -->
    <array name="ntp_hosts_array">
        <item>ntp.host1.com</item>
        <item>ntp.host2.com</item>
    </array>
</resources>
```

# README

[English](./README.md)

[中文](./README-ZH.md)
