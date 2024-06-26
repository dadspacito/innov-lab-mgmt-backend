/*
// is created like other entity classses in entity folder
// tem id, sender_id, receiver_id, message, created_at, read, content, valid

package entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.io.Serializable;

@Entity
@Table(name="Message")
@NamedQueries(
        {
                // find messages between two users
                @NamedQuery(name = "PersonalMessageEntity.findMessagesBetweenUsers", query = "SELECT m FROM PersonalMessageEntity m WHERE (m.sender_id = :sender_id AND m.receiver_id = :receiver_id) OR (m.sender_id = :receiver_id AND m.receiver_id = :sender_id)"),
                @NamedQuery(name = "PersonalMessageEntity.findMessageById", query = "SELECT m FROM PersonalMessageEntity m WHERE m.id = :id"),
                @NamedQuery(name = "PersonalMessageEntity.findAllMessages", query = "SELECT m FROM PersonalMessageEntity m")
        }
)

public class PersonalMessageEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, unique = true, updatable = false)
    private int id;

    @Column(name = "sender_id", nullable = false, unique = false, updatable = true)
    private int sender_id;

    @Column(name = "receiver_id", nullable = false, unique = false, updatable = true)
    private int receiver_id;

    @Column(name = "content", nullable = false, unique = false, updatable = true)
    private String content;

    @Column(name = "created_at", nullable = false, unique = false, updatable = true)
    private LocalDateTime created_at;

    @Column(name = "read", nullable = false, unique = false, updatable = true)
    private boolean read;

    @Column(name = "content", nullable = false, unique = false, updatable = true)
    private String content;

    @Column(name = "valid", nullable = false, unique = false, updatable = true)
    private boolean valid;

    public PersonalMessageEntity() {
    }

    public PersonalMessageEntity(int sender_id, int receiver_id, String message, LocalDateTime created_at, boolean read, String content, boolean valid) {
        this.sender_id = sender_id;
        this.receiver_id = receiver_id;
        this.content = message;
        this.created_at = created_at;
        this.read = read;
        this.content = content;
        this.valid = valid;
    }

    public long getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getSender_id() {
        return sender_id;
    }

    public void setSender_id(int sender_id) {
        this.sender_id = sender_id;
    }

    public int getReceiver_id() {
        return receiver_id;
    }

    public void setReceiver_id(int receiver_id) {
        this.receiver_id = receiver_id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String message) {
        this.content = message;
    }

    public LocalDateTime getCreated_at() {
        return created_at;
    }

    public void setCreated_at(LocalDateTime created_at) {
        this.created_at = created_at;
    }

}
*/
