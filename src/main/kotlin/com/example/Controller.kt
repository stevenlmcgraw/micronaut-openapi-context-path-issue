package com.example

import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse


@Controller("/api/v1/example")
class Controller {

    @Operation(
        operationId = "get-example-data",
        summary = "Get example data",
        description = "Get example data",
        tags = ["example"],
        responses = [
            ApiResponse(
                responseCode = "200",
                description = "get example data",
                content = [
                    Content(
                        mediaType = "application/json",
                        schema = Schema(implementation = ExampleData::class)
                    )
                ]
            )
        ]
    )
    @Get
    fun example(): ExampleData {
        return ExampleData(1, "howdy")
    }
}
