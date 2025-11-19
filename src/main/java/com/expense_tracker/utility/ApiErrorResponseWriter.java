package com.expense_tracker.utility;

 // adjust if ApiResponse is in a different package
import com.expense_tracker.response.ApiResponse;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

public final class ApiErrorResponseWriter {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    private ApiErrorResponseWriter() {}

    public static void write(HttpServletResponse response,
                             String message,
                             int statusCode) throws IOException {

        ApiResponse<?> api = new ApiResponse<>(
                "error",
                message,
                null,
                statusCode
        );

        response.setContentType("application/json");
        response.setStatus(statusCode);
        response.getWriter().write(MAPPER.writeValueAsString(api));
    }
}