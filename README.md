# gdx-jvdf-linkedlist
[![Release](https://jitpack.io/v/AreteS0ftware/gdx-quadtree.svg)](https://jitpack.io/v/AreteS0ftware/gdx-jvdf-linkedlist)
[![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg)](https://opensource.org/licenses/Apache-2.0)
[![Versioning](https://img.shields.io/badge/semver-2.0.0-blue)](https://semver.org/)

A parser for Valve Software's KeyValues ("VDF") format, commonly used by Source engine games, written entirely in Java. 

Forked from <a href="https://github.com/AreteS0ftware/jvdf-linkedlist">jvdf-linkedlist</a>, it adds functionality to support easier handling of various <a href="https://github.com/libgdx/libgdx">libGDX</a> objects, such as `Color`, `Vector3` & `Vector2`.

### Install
gdx-jvdf-linkedlist is available via JitPack. Make sure you have JitPack declared as a repository in your root <code>build.gradle</code> file:

```
allprojects {
    repositories {
        // ...
        maven { url 'https://jitpack.io' }
    }
}
```
Then add gdx-quadtree as dependency in your core project:
```
project(":core") {
    dependencies {
    	// ...
        implementation 'com.github.AreteS0ftware:gdx-jvdf-linkedlist:0.0.1'
    }
}
```


## License

Apache-2.0 license

Copyright (c) 2023 Arete

Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at

http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.
