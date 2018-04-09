package es.ffgiraldez.comicsearch.platform

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.Observer
import io.reactivex.Observable
import io.reactivex.android.MainThreadDisposable

fun <T> LiveData<T>.toObservable(): Observable<T> =
        Observable.create { emitter ->
            val observer = Observer<T> {
                it?.let { emitter.onNext(it) }
            }
            this@toObservable.observeForever(observer)

            emitter.setCancellable {
                object : MainThreadDisposable() {

                    override fun onDispose() = removeObserver(observer)
                }
            }
        }