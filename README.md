# SwaggerUI Does Not Render Behind Reverse Proxy

It appears that adding a `contextPath` cookie via https://micronaut-projects.github.io/micronaut-openapi/latest/guide/#serverFilter or https://micronaut-projects.github.io/micronaut-openapi/latest/guide/#urlParameter is only applying the `contextPath` to fetching the OpenAPI3 spec YAML file.

Retrieval of the various `res/**` resources (e.g. `res/swagger-ui.css`, `res/swagger-ui-bundle.js`, etc.) are still being fetched from the root path `/` instead of the `contextPath` specified.

For an app behind a reverse-proxy this causes rendering of SwaggerUI to fail and give a blank page.

---

# SETUP

## environment used

- Operating System: MacOS Sonoma 14.5 (though this issue is not platform-specific)
- Micronaut platform: 4.5.0 (though has been observed in any prior release used)
- JDK: 17
- Kotlin: 1.9.23 (though has been observed in any prior release used)

## reverse-proxy

An `nginx` server configured with a simple reverse-proxy is required to reproduce the issue.

The `example-reverse-proxy.conf` file in this repository contains a sample configuration for `nginx`.

On MacOS the shortest path to running the reverse-proxy is installing `nginx` via Homebrew:

```brew install nginx```

Find the `nginx.conf` file:

```nginx -t```

Drop the contents of `example-reverse-proxy.conf` into the `nginx.conf` file.

```brew services start nginx```

## application

Adjust the `app.swagger-context-path` property in `application.yml` according to the scenario being tested (outlined below).

Perform the Gradle build.

Execute the `./gradlew run` command in the application directory.

---

# Reproducing the Issue

## No Reverse-Proxy With Empty `contextPath` Cookie Value (happy path)

Ensure the `app.swagger-context-path` property in `application.yml` is an empty string.

Start the app.

Go to `localhost:8080/docs/swagger-ui` in a browser; SwaggerUI renders properly.

## No Reverse-Proxy With Non-Empty `contextPath` Cookie Value

Ensure the `app.swagger-context-path` property in `application.yml` is set to `/dev/example`.

Start the app.

Go to `localhost:8080/docs/swagger-ui` in a browser; SwaggerUI does not render properly.

An error page is displayed with message:

```
Fetch error
Not Found /dev/example/docs/swagger/micronaut-openapi-context-path-issue-1.0.0.yml
```

^^^the `contextPath` cookie value is prepended to the OpenAPI3 spec YAML file path, but not to the `res/**` resources mapped to `/docs/swagger-ui-res/**`.

The `contextPath` cookie is present in DevTools for the interactions with the `/docs/swagger-ui-res/**` endpoints.

## Reverse-Proxy with Non-Empty `contextPath` Cookie Value

Ensure the `app.swagger-context-path` property in `application.yml` is set to `/dev/example`.

Ensure the reverse-proxy is running.

Start the app.

Go to `localhost:8081/dev/example/docs/swagger-ui` in a browser; SwaggerUI does not render properly.

A blank page is displayed.

The `contextPath` cookie is present in DevTools for the interactions with the `/docs/swagger-ui-res/**` endpoints.

However, the URL used for these resources does not include the `contextPath` cookie value.

For example, in DevTools it is observed that the URL for the `swagger-ui-bundle.js`  resource is `http://localhost:8081/docs/swagger-ui-res/swagger-ui-bundle.js` instead of `http://localhost:8081/dev/example/docs/swagger-ui-res/swagger-ui-bundle.js`.