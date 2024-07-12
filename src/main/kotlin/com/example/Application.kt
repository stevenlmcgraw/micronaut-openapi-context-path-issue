package com.example

import io.micronaut.runtime.Micronaut.run
import io.swagger.v3.oas.annotations.*
import io.swagger.v3.oas.annotations.info.*
import io.swagger.v3.oas.annotations.tags.Tag

@OpenAPIDefinition(
    info = Info(
            title = "micronaut-openapi-context-path-issue",
            version = "1.0.0"
    ),
    tags = [
        Tag(name = "example", description = "Example operations")
    ]
)
object Api

fun main(args: Array<String>) {
	run(*args)
}

