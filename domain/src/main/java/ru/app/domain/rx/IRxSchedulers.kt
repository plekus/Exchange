package ru.app.domain.rx

import io.reactivex.Scheduler

interface IRxSchedulers {
    val ui: Scheduler
    val io: Scheduler
    val computation: Scheduler
}