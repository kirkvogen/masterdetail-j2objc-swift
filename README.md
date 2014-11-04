masterdetail-j2objc-swift
=========================

This sample project uses [J2ObjC](http://j2objc.org) to share Java business logic between Android and iOS master-detail apps. The shared code uses the [Model View ViewModel pattern](http://en.wikipedia.org/wiki/Model_View_ViewModel) in order to increase the amount of UI logic that can be shared. Besides the view model, the shared code includes a model class and service for persisting the model.

There are three projects:

- **masterdetail**: The shared code as an Eclipse project
- **masterdetail-android**: The Android Eclipse project
- **masterdetail-ios**: The iOS Xcode project
 
Building for iOS
--------

1. Install the [latest release of J2ObjC](https://github.com/google/j2objc/releases)
2. Open the Xcode project in masterdetail-ios/masterdetail-ios.xcodeproj
3. Open the project settings in Xcode and navigate to **Build Settings**. In the *User-Defined* section, update `J2OBJC_HOME` to the location of your J2ObjC distribution. For example, if the distribution is two directories up from the Xcode project in a folder named `j2objc-dist`, it would have a value of `${SRCROOT}/../../j2objc-dist`
4. There appears to be a build order issue with Xcode in that it will attempt to compile Swift code before dependent code (i.e. the Objective-C that was converted from Java). You may see compilation errors in the Swift code. To workaround this issue, go to a Terminal session and run `make` from the masterdetail folder. The makefile will translate all of the shared Java code to Objective-C.
5. Back in Xcode, any Swift errors should be gone.
6. Run the app on the simulator.

Building for Android
--------
Open the masterdetail and masterdetail-android projects in Eclipse with Android Developer Tools (ADT). Run masterdetail-android as you would any Android app.

License
----
[Apache License Version 2.0](http://www.apache.org/licenses/LICENSE-2.0), January 2004

