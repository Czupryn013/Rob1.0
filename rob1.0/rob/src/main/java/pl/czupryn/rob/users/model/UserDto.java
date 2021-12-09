package pl.czupryn.rob.users.model;


import lombok.Data;

@Data
public class UserDto {
    private Long id;
    private String username;
    private Role role;

    public static UserDto from(User user) {
        UserDto userDto = new UserDto();
        userDto.setUsername(user.getUsername());
        userDto.setId(user.getId());
        userDto.setRole(user.getRole());
        return userDto;
    }

}
