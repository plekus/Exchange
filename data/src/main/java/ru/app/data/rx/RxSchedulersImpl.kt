package ru.app.data.rx

import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers.mainThread
import io.reactivex.schedulers.Schedulers.computation
import io.reactivex.schedulers.Schedulers.io
import ru.app.domain.rx.IRxSchedulers

class RxSchedulersImpl: IRxSchedulers {
    override val ui: Scheduler = mainThread()
    override val io: Scheduler = io()
    override val computation: Scheduler = computation()
}