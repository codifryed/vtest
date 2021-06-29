package com.guyboldon.vtest

import com.github.ajalt.clikt.core.CliktCommand
import java.net.InetSocketAddress
import java.nio.channels.SocketChannel
import java.util.stream.Collectors
import kotlin.math.roundToInt

class Vtest : CliktCommand() {

    companion object {
        const val google1 = "8.8.8.8"
        const val cloudFlare = "1.1.1.1"
        const val comcast = "4.2.2.1"
    }

    override fun run() {

        echo("Starting Connection Tests...")
        val maxFailurePercent: Int =
            listOf(
                cloudFlare,
                google1,
                comcast,
            )
                .parallelStream()
                .map {
                    verify(it)
                }
                .collect(Collectors.toList())
                .maxOrNull() ?: 0

        echo("Max Failure Rate: $maxFailurePercent%")
        if (maxFailurePercent < 1)
            echo("Connection is Stable.")
        else
            echo("Connection is NOT Stable.", err = true)
    }

    private fun verify(host: String, times: Int = 20): Int {
        val results = mutableListOf<Pair<Boolean, Long>>()

        (1..times).forEach {
            val result = ping(host)
            echo(
                "Ping #$it for $host: ${if (result.first) "Success" else "Failure"} time: ${result.second}ms"
            )
            results.add(result)
            Thread.sleep(500) // add a little delay for better verification
        }
        val failTotal = results
            .sumOf { result -> if (result.first) 0.0 else 1.0 }
        val failPercentage = ((failTotal / times) * 100).roundToInt()
        val averageTime = results
            .map { it.second }
            .average()
        echo("Test for $host has fail rate of ${failPercentage}% and an average response time of ${averageTime}ms")
        return failPercentage
    }

    private fun ping(host: String): Pair<Boolean, Long> {
        val startTime = System.currentTimeMillis()
        try {
            SocketChannel
                .open()
                .use {
                    it.configureBlocking(true)
                    val result = it.connect(InetSocketAddress(host, 53))
                    return result to elapsedTime(startTime)
                }
        } catch (ex: Exception) {
            echo(ex.localizedMessage, err = true)
            return false to elapsedTime(startTime)
        }
    }

    private fun elapsedTime(startTimeMillis: Long): Long =
        (System.currentTimeMillis() - startTimeMillis)
}

fun main(args: Array<String>) = Vtest().main(args)

