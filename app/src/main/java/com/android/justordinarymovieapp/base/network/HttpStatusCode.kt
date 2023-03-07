package com.android.justordinarymovieapp.base.network

enum class HttpStatusCode(val code: Int, val statusName: String) {
    Unknown(-1, "Unknown Error"),

    BadRequest(400, "Bad Request"),
    Unauthorized(401, "Unauthorized"),
    Forbidden(403, "Forbidden"),
    NotFound(404, "Not Found"),
    MethodNotAllowed(405, "Method Not Allowed"),
    NotAcceptable(406, "Not Acceptable"),
    RequestTimeout(408, "Request Timeout"),

    InternalServerError(500, "Internal Server Error"),
    NotImplemented(501, "Not Implemented"),
    BadGateway(502, "Bad Gateway"),
    ServiceUnavailable(503, "Service Unavailable"),
    GatewayTimeout(504, "Gateway Timeout"),
    HttpVersionNotSupported(505, "Http Version Not Supported")
}