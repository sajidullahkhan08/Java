package common;

import java.io.Serializable;

public class Response implements Serializable {
    private String type;
    private Object data;
    private boolean success;
    private String message;

    public Response(String type, Object data, boolean success, String message) {
        this.type = type;
        this.data = data;
        this.success = success;
        this.message = message;
    }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public Object getData() { return data; }
    public void setData(Object data) { this.data = data; }

    public boolean isSuccess() { return success; }
    public void setSuccess(boolean success) { this.success = success; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
}