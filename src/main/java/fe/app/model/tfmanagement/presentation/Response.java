package fe.app.model.tfmanagement.presentation;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.Objects;
public class Response<T> {
    @Expose
    @SerializedName("status")
    private ServerStatus status;

    @Expose
    @SerializedName("message")
    private String message;

    @Expose
    @SerializedName("result")
    private T result;

    public Response() {
    }

    public Response(ServerStatus status, String message) {
        this(status, message, null);
    }

    public Response(ServerStatus status, String message, T result) {
        this.status = status;
        this.message = message;
        this.result = result;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Response<?> response = (Response<?>) o;
        return status == response.status && Objects.equals(message, response.message) && Objects.equals(result, response.result);
    }

    @Override
    public int hashCode() {
        return Objects.hash(status, message, result);
    }

    public ServerStatus getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }

    public T getResult() {
        return result;
    }
}

