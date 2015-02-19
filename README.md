masterdetail-j2objc-swift
=========================

This sample project uses [J2ObjC](http://j2objc.org) to share Java application logic between Android and iOS master-detail apps. For iOS, the Java code is translated into Objective-C. Those classes are then used in the app, which is coded in Swift.

The application logic implements the [Model View ViewModel pattern (MVVM)](http://en.wikipedia.org/wiki/Model_View_ViewModel) to share UI logic between the Android and iOS implementations. Besides the view model, the application logic includes a model class and service for persisting the model.

There are three projects:

- **masterdetail-lib**: The shared application logic as an Android Studio project
- **masterdetail-android**: The Android project
- **masterdetail-ios**: The iOS Xcode project

Building for iOS
--------

1. Install the [latest release of J2ObjC](https://github.com/google/j2objc/releases)
2. Open the project masterdetail-lib in Android Studio. This step will ensure that certain project dependencies get initialized properly. In the future, it would be convenient for this step to be handled via the makefile (noted a few steps below).
3. Open the Xcode project in masterdetail-ios/masterdetail-ios.xcodeproj
4. Open the project settings in Xcode and navigate to **Build Settings**. In the *User-Defined* section, update `J2OBJC_HOME` to the location of your J2ObjC distribution. For example, if the distribution is two directories up from the Xcode project in a folder named `j2objc-dist`, it would have a value of `${SRCROOT}/../../j2objc-dist`
5. Open a Terminal session, navigate to the `masterdetail-lib` folder, and run `make`. This step is necessary as there appears to be a build order issue with Xcode in that it will attempt to compile Swift code before dependent code (i.e. the dependent Java code which is to be translated via a build rule). The makefile will translate all of the shared Java code to Objective-C.
6. Back in Xcode, any Swift errors should be gone. Note that future edits to the Java code shouldn't result in compilation errors in the Swift code. Just be sure to make any Java changes prior to the Swift changes.
7. Run the app on the simulator.

Building for Android
--------
Open the masterdetail-j2objc-swift project in Android Studio. Run masterdetail-android as you would any Android app.

License
----
[Apache License Version 2.0](http://www.apache.org/licenses/LICENSE-2.0), January 2004
