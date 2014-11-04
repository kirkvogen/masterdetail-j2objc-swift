//
// Copyright 2014 Kirk C. Vogen
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.
//

import Foundation

//
// This class is to workaround an issue with Objective-C categories when creating a subclass of
// JavaObject. It seems that categories aren't properly considered. For example, if this class is
// declared:
//
//   class SomeClass: NSObject, JavaObject, SomeJavaInterface {
//
// The compiler will report that SomeClass doesn't implement required JavaObject methods like
// notify(), etc. Since there is a category on NSObject for the required JavaObject methods, it
// would seem that the compiler would take the category into account. It doesn't appear to.
//
// This class was added to workaround that issue. It is simply a wrapper to those JavaObject
// methods. In this case, the JavaObject category on NSObject is considered by the compiler.
// 
// This class can then be used to subclass JavaObjects without compiler errors:
//
//   class SomeClass: JavaObjectBridge, SomeJavaInterface {
// 
class JavaObjectBridge: NSObject, JavaObject {
    
    override func clone() -> AnyObject! {
        return super.clone()
    }
    
    override func getClass() -> IOSClass! {
        return super.getClass()
    }
    
    override func notify() {
        super.notify()
    }
    
    override func notifyAll() {
        super.notifyAll()
    }
    
    override func wait() {
        super.wait()
    }
    
    override func waitWithLong(timeout: Int64) {
        super.waitWithLong(timeout)
    }
    
    override func waitWithLong(timeout: Int64, withInt nanos: Int32) {
        super.waitWithLong(timeout, withInt: nanos)
    }
}