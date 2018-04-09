# RxMVVM

My way to MVVM using RxJava with new Android databinding

## Summary
* Use [MVVM][1] using [architecture components][6] with to separate Android Framework with a [clean architecture][2] to my domain logic.
* Use [Android Databinding][3] to glue view model and Android
* Asynchronous communications implemented with [Rx][4].
* Rest API from [ComicVine][5]

## Dependencies
* architecture components
* rx-java
* floating search
* okhttp
* retrofit
* koin
* picasso

TODO LIST
---------

* Better UI, with Material Design concepts and so on
* Add unit tests, allways fail on that :( 
* Implement a local datasource with Realm to test it


Developed By
------------

Fernando Franco Giráldez - <ffrancogiraldez@gmail.com>

<a href="https://twitter.com/thanerian">
  <img alt="Follow me on Twitter" src="/images/twitter_icon.png" height="128"/>
</a>
<a href="http://es.linkedin.com/pub/fernando-franco-giraldez/22/803/b44/es">
  <img alt="Add me to Linkedin" src="/images/linkedin_icon.png" height="128"/>
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
[3]: https://developer.android.com/topic/libraries/data-binding/index.html
[4]: http://reactivex.io/
[5]: http://www.comicvine.com/api/
[6]: https://developer.android.com/topic/libraries/architecture/index.html
