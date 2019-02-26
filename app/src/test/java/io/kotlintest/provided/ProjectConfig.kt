package io.kotlintest.provided

import androidx.arch.core.executor.ArchTaskExecutor
import androidx.arch.core.executor.TaskExecutor
import io.kotlintest.AbstractProjectConfig
import io.reactivex.plugins.RxJavaPlugins
import io.reactivex.schedulers.Schedulers
import io.reactivex.schedulers.TestScheduler

object ProjectConfig : AbstractProjectConfig() {

    override fun parallelism(): Int = 2

    override fun beforeAll() {
        super.beforeAll()
        ArchTaskExecutor.getInstance().setDelegate(object : TaskExecutor() {
            override fun executeOnDiskIO(runnable: Runnable) {
                runnable.run()
            }

            override fun postToMainThread(runnable: Runnable) {
                runnable.run()
            }

            override fun isMainThread(): Boolean {
                return true
            }
        })

        RxJavaPlugins.reset()
        RxJavaPlugins.setIoSchedulerHandler { Schedulers.trampoline() }
        RxJavaPlugins.setNewThreadSchedulerHandler { Schedulers.trampoline() }
        RxJavaPlugins.setComputationSchedulerHandler { testScheduler }

    }

    val testScheduler = TestScheduler()

    override fun afterAll() {
        super.afterAll()
        ArchTaskExecutor.getInstance().setDelegate(null)
        RxJavaPlugins.reset()
    }
}