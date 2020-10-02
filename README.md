# Reactive MVVM

My way to MVVM using KotlinX Coroutines with Android databinding

## Summary
* Use [MVVM][1] using [architecture components][6] with to separate Android Framework with a [clean architecture][2] to my domain logic.
* Use [Android Databinding][3] wih [LiveData][8] to glue [ViewModel][9] and Android
* Asynchronous communications implemented with [KotlinX Coroutines][4].
* Rest API from [ComicVine][5]
* Store data using [Room][7]

## Dependencies
* architecture components
  * livedata
  * room
  * viewmodel
* kotlinx coroutines
* floating search
* okhttp
* retrofit
* koin
* picasso

### Testing
* [Kotest][10]
* [Android Junit 5][11]
* [Turbine][12]

TODO LIST
---------

* Better UI, with Material Design concepts and so on
* ~~Add unit tests, allways fail on that :(~~ :white_check_mark:

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

    Copyright 2019 Fernando Franco Giráldez
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
[4]: https://github.com/Kotlin/kotlinx.coroutines
[5]: http://www.comicvine.com/api/
[6]: https://developer.android.com/topic/libraries/architecture/index.html
[7]: https://developer.android.com/topic/libraries/architecture/room.html
[8]: https://developer.android.com/topic/libraries/architecture/livedata.html
[9]: https://developer.android.com/topic/libraries/architecture/viewmodel.html
[10]: https://kotest.io
[11]: https://github.com/mannodermaus/android-junit5
[12]: https://github.com/cashapp/turbine
