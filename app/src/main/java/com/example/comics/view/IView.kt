package com.example.comics.view

interface IView {

    fun viewList(list: List<ItemVO>)

    fun refrash()

    fun error()

}