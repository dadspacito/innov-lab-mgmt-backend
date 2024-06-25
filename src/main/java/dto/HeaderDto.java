package dto;

public class HeaderDto {

    // dto com nickname e foto de perfil
    private String nickname;
    private String avatar;

    public HeaderDto() {
    }

    public HeaderDto(String nickname, String avatar) {
        this.nickname = nickname;
        this.avatar = avatar;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }


}
