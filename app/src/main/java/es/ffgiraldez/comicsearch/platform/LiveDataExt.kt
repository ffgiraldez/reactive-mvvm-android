package es.ffgiraldez.comicsearch.platform

import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import io.reactivex.BackpressureStrategy
import io.reactivex.Flowable
import io.reactivex.android.MainThreadDisposable

fun <T> LiveData<T>.toFlowable(): Flowable<T> =
        Flowable.create({ emitter ->
            val observer = Observer<T> {
                it?.let(emitter::onNext)
            }
            observeForever(observer)

            emitter.setCancellable {
                object : MainThreadDisposable() {

                    override fun onDispose() = removeObserver(observer)
                }
            }
        }, BackpressureStrategy.LATEST)