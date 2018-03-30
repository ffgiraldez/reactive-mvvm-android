package es.ffgiraldez.comicsearch.platform

import android.arch.lifecycle.LifecycleOwner
import android.arch.lifecycle.LiveData
import android.arch.lifecycle.LiveDataReactiveStreams
import io.reactivex.Observable

fun <T> LiveData<T>.toObservable(lifecycleOwner: LifecycleOwner): Observable<T> =
        Observable.fromPublisher(LiveDataReactiveStreams.toPublisher(lifecycleOwner, this))