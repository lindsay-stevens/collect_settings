# Collect Settings


## Contents
- [Introduction](#introduction)
- [Available Settings](#available-settings)
- [Testing](#testing)
    + [Integration / Smoke Test](#integration-smoke-test)


## Introduction
The ODK Collect Android app has a facility to save app preferences to a file. 
This is a convenient way to distribute and re-use settings across devices.

Currently (as of v.1.4.10 rev 1061) the app preferences file is saved using 
Java Object serialization. This is a binary format that is not easily editable 
as plain text.

This library provides a converter, which will take a plain text Java Properties 
format file, and produce serialized Java Object equivalent to what is produced 
by the Collect app.

This means that administrators or users can define the desired settings in 
plain text, without needing to do it in the app first and then exporting it.


## Available Settings
A template properties file is currently located at "/src/test/resources/collect.properties".

When the Collect app writes the settings to file, it serializes two objects, in 
the following order: User settings, Admin settings. The mapping between the 
preferences defined at \[1\] and these two objects seems to be that all the  
"admin_preferences.xml" preferences go into the Admin object, and everything 
else (including aggregate, google, other) go into the User object. 

\[1\] https://github.com/opendatakit/collect/tree/master/collect_app/src/main/res/xml

Similarly, enum values are defined at \[2\]:

\[2\] https://github.com/opendatakit/collect/tree/master/collect_app/src/main/res/values

Note: in MainTest.java, there is a utility function for printing (to stdout)
the values from a collect.settings file, e.g. if you wanted to convert an 
existing settings file into a properties file for storage / management but 
don't want to go through the hassle of figuring out enum values.


## Usage
The tool accepts one command line argument: the path to a collect.properties 
file. The corresponding collect.settings file will be saved to the same directory.

For example, to use the JAR compiled version of the tool:
```
java -jar collect_settings.jar /local/path/to/collect.properties
```


## Testing
There are a few unit tests to cover basic settings mapping functionality, which 
can be run using JUnit.

For integration tests against the ODK Collect app, it is easiest to do so using 
an emulated Android device, following the steps described in the next section. 

The difficulty in using a real device is that the settings file is deleted and 
converted / saved as app preference XML in the protected app storage directory, 
so the only way to recover from a bad settings file being loaded by the app 
is to re-install the Collect app. Not impossible, just a tad tedious.


### Integration / Smoke Test
- Install Android Studio
- Create an emulated device
- Start the emulated device
- Find the "adb" tool (aka Android Debug Bridge) 
    + Should be somewhere like %USER%/AppData/Local/Android/sdk/platform-tools.
- Obtain an apk of ODK Collect (compile from GitHub, download from GitHub, 
    or download from opendatakit.org)
- Install ODK on the device using adb:
```
adb install "/path/to/the/odk/collect/apk/file.apk"
```
- Generate a "collect.settings" file by running the Collect_Settings tool.
- Copy the settings file to the device using adb:
```
adb push "local/path/to/collect.settings" "/storage/emulated/0/odk/collect.settings"
```
- If necessary, uninstall Collect using adb (then re-install as above):
```
adb uninstall org.odk.collect.android
```
- If necessary, copy a saved settings file off the emulated device:
```
adb pull "/storage/emulated/0/odk/settings/collect.settings" "local/path/to/save/collect.settings"
```
