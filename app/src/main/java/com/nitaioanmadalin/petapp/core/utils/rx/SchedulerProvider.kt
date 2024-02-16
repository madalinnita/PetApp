package com.nitaioanmadalin.petapp.core.utils.rx

import io.reactivex.rxjava3.core.Scheduler

interface SchedulerProvider {
    fun io(): Scheduler
    fun main(): Scheduler
    fun computation(): Scheduler
}