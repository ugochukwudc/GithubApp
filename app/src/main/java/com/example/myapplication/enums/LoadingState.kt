package com.example.myapplication.enums;

enum class LoadingState {
    NOT_STARTED,
    STARTED,
    ERROR,
    COMPLETE;

    fun isLoading(): Boolean = this == STARTED

    fun isComplete(): Boolean = this == COMPLETE

    fun hasStarted(): Boolean = this > NOT_STARTED

    fun isError(): Boolean = this == ERROR
}