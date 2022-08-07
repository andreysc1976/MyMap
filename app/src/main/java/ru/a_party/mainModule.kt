package ru.a_party

import org.koin.dsl.module
import ru.a_party.mymap.MyMarkers

val mainModule = module{
    single<MyMarkers>{MyMarkers()}
}