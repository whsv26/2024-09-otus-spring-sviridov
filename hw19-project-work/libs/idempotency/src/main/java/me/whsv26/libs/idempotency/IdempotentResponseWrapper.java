package me.whsv26.libs.idempotency;

import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpServletResponseWrapper;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintWriter;

class IdempotentResponseWrapper extends HttpServletResponseWrapper {

    private final ByteArrayOutputStream capture;

    private ServletOutputStream output;

    private PrintWriter writer;

    public IdempotentResponseWrapper(HttpServletResponse response) {
        super(response);
        this.capture = new ByteArrayOutputStream();
    }

    @Override
    public ServletOutputStream getOutputStream() {
        if (writer != null) {
            throw new IllegalStateException("getWriter() already called on this response");
        }
        if (output == null) {
            output = new DelegatingServletOutputStream(capture);
        }
        return output;
    }

    @Override
    public PrintWriter getWriter() {
        if (output != null) {
            throw new IllegalStateException("getOutputStream() already called on this response");
        }
        if (writer == null) {
            writer = new PrintWriter(capture);
        }
        return writer;
    }

    public String getCaptureAsString() throws IOException {
        return new String(getCaptureAsBytes(), getCharacterEncoding());
    }

    private byte[] getCaptureAsBytes() throws IOException {
        if (writer != null) {
            writer.flush();
        } else if (output != null) {
            output.flush();
        }
        return capture.toByteArray();
    }
}