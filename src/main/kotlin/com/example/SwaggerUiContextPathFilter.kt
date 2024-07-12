package com.example

import io.micronaut.context.annotation.Requires
import io.micronaut.context.annotation.Value
import io.micronaut.http.HttpMethod
import io.micronaut.http.MutableHttpResponse
import io.micronaut.http.annotation.ResponseFilter
import io.micronaut.http.annotation.ServerFilter
import io.micronaut.http.cookie.Cookie
import java.time.Duration

@ServerFilter(methods = [HttpMethod.GET, HttpMethod.HEAD], patterns = ["/docs/swagger-ui*"])
@Requires("\${app.swagger-context-path}")
class SwaggerUiContextPathFilter(
    @Value("\${app.swagger-context-path}") private val contextPath: String
) {

    @ResponseFilter
    fun filterResponse(response: MutableHttpResponse<*>) =
        response.cookie(Cookie.of("contextPath", contextPath).maxAge(Duration.ofMinutes(2)))
}
