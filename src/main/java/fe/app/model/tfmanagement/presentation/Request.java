package fe.app.model.tfmanagement.presentation;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.Objects;

public class Request<T> {

    @Expose
    @SerializedName("method")
    private String method;

    @Expose
    @SerializedName("argument")
    private T argument;

    public Request() {
    }

    public Request(String method, T argument) {
        this.method = method;
        this.argument = argument;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Request<?> request = (Request<?>) o;
        return Objects.equals(method, request.method) && Objects.equals(argument, request.argument);
    }

    @Override
    public int hashCode() {
        return Objects.hash(method, argument);
    }

    public String getMethod() {
        return method;
    }

    public T getArgument() {
        return argument;
    }

}
