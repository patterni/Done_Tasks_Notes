package com.example.yes.util

sealed class OrderType{
    object Ascending: OrderType()
    object Descending: OrderType()
}
