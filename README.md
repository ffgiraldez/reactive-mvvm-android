#RxMVVM

My way to MVVM using RxJava with new Android databinding

##Summary
* Use [MVVM][1] to separate Android Framework with a [clean architecture][2] to my domain logic.
* Use [Android Databinding][3] to glue view model and Android
* Asynchronous communications implemented with [Rx][4].
* Rest API from [ComicVine][5]
* Use [Frodo][6] to debug Rx

TODO LIST
---------

* Better UI, with Material Design concepts and so on
* Add unit tests, allways fail on that :(
* Add Dependency Injection
* [WIP] Implement an Annotation processor to remove most of the View Model boilerplate code 
* Implement a local datasource with Realm to test it


Developed By
------------

Fernando Franco Giráldez - <ffrancogiraldez@gmail.com>

<a href="https://twitter.com/thanerian">
  <img alt="Follow me on Twitter" src="http://imageshack.us/a/img812/3923/smallth.png" />
</a>
<a href="http://es.linkedin.com/pub/fernando-franco-giraldez/22/803/b44/es">
  <img alt="Add me to Linkedin" src="http://imageshack.us/a/img41/7877/smallld.png" />
</a>

License
-------

    Copyright 2015 Fernando Franco Giráldez
    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
    
[1]: https://en.wikipedia.org/wiki/Model_View_ViewModel
[2]: http://blog.8thlight.com/uncle-bob/2012/08/13/the-clean-architecture.html
[3]: http://developer.android.com/intl/es/tools/data-binding/guide.html
[4]: http://reactivex.io/
[5]: http://www.comicvine.com/api/
[6]: https://github.com/android10/frodo