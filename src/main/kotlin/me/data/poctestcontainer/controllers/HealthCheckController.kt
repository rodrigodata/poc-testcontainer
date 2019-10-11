package me.data.poctestcontainer.controllers

import me.data.poctestcontainer.models.HealthCheckStatus
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController


@RestController
@RequestMapping("healthcheck")
class HealthCheckController {

    @GetMapping("")
    fun get() : HealthCheckStatus = HealthCheckStatus("disponivel", 200)
}