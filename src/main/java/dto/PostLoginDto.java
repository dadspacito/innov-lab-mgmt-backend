package dto;



public class PostLoginDto {

    private String token;
    private int timeout;

    // adicionar foto e nome para header?
    public PostLoginDto() {
    }

    public PostLoginDto(String token, int timeout) {
        this.token = token;
        this.timeout = timeout;
    }

    // getters and setters all
    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public int getTimeout() {
        return timeout;
    }

    public void setTimeout(int timeout) {
        this.timeout = timeout;
    }

    @Override
    public String toString() {
        return "{" +
                "token='" + token + '\'' +
                ", timeout=" + timeout +
                '}';
    }
}
