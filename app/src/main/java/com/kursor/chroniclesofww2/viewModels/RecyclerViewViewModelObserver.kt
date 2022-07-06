package com.kursor.chroniclesofww2.viewModels

interface RecyclerViewViewModelObserver {

    fun itemInserted(index: Int) {}

    fun itemRemoved(index: Int) {}

    fun itemChanged(index: Int) {}

}