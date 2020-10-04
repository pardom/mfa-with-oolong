package oolong.counter.data

typealias GetCount = () -> Int

typealias GetCounts = () -> Map<Int, Int>

typealias PutCount = (Int) -> Unit

typealias PutCounts = (Map<Int, Int>) -> Unit
