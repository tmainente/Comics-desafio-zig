package com.example.comics.presenter

import com.example.comics.repository.ItemModel

interface IPresenter {

    fun setupList(list: ItemModel)

    fun error()
}