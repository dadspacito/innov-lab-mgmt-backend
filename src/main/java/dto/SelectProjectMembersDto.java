package dto;

/**
 * esta classe existe para se poder ver os membros quando se está a criar um prjeto.Nao confundir com project members
 * que é um DTO diferente. Esta é a maneira como se visualiza a lista dos users que se podem adicionar ao projeto
 * o retorno destes elementos parte do principio que o utilizador pode ser escolhido para projetos
 */
public class SelectProjectMembersDto {
    int id;
    private String name;
    private String nickname;
    private String email;

    public SelectProjectMembersDto() {
    }
    public SelectProjectMembersDto(String name, String nickname, String email) {
        this.name = name;
        this.nickname = nickname;
        this.email = email;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }



    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
