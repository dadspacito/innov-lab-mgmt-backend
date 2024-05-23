package entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.io.Serializable;

@Entity
@Table(name="SessionToken")
@NamedQueries(
        {
                @NamedQuery(name = "SessionToken.findUserByToken",
                        query = "SELECT DISTINCT s.user FROM SessionTokenEntity s WHERE s.token = :token"),
                @NamedQuery(name = "SessionToken.deleteAllInvalidSessionTokens",
                        query = "DELETE FROM SessionTokenEntity s WHERE s.tokenTimeout < CURRENT_TIMESTAMP"),
                @NamedQuery(name = "SessionToken.deleteSessionToken",
                        query = "DELETE FROM SessionTokenEntity s WHERE s.token = :token"),
                @NamedQuery(name = "SessionToken.deleteAllSessionTokens",
                        query = "DELETE FROM SessionTokenEntity s WHERE s.user = :user"),
                @NamedQuery(name = "SessionToken.findSessionTokenByToken",
                        query = "SELECT s FROM SessionTokenEntity s WHERE s.token = :token"),

                @NamedQuery(name = "SessionToken.updateTokenSessionTimeout",
                      query = "UPDATE SessionTokenEntity s " +
                              "SET s.tokenTimeout = FUNCTION('DATE_ADD', CURRENT_TIMESTAMP, CONCAT(:timeout, ' MINUTE')) " +
                              "WHERE s.token = :token"),


                @NamedQuery(name = "SessionToken.resetTokenSessionTimeout",
                        query = "UPDATE SessionTokenEntity s " +
                                "SET s.tokenTimeout = :newsessiontimeout WHERE s.token = :token")


        }

)
public class SessionTokenEntity implements Serializable {


    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id", nullable = false, unique = true, updatable = false)
    private int id;

    @Column(name="token", nullable = true, unique = false, updatable = true)
    private String token;

    @Column(name="token_timeout", nullable = false, unique = false, updatable = true)
    private LocalDateTime tokenTimeout;

    @ManyToOne
    @JoinColumn(name="user_id", nullable = false)
    private UserEntity user;


    public SessionTokenEntity() {
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public LocalDateTime getTokenTimeout() {
        return tokenTimeout;
    }

    public void setTokenTimeout(LocalDateTime tokenTimeout) {
        this.tokenTimeout = tokenTimeout;
    }

    public UserEntity getUser() {
        return user;
    }

    public void setUser(UserEntity user) {
        this.user = user;
    }
}
