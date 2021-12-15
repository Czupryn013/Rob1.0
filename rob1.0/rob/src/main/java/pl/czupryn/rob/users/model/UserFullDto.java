package pl.czupryn.rob.users.model;

import lombok.Data;

import java.util.*;

@Data
public class UserFullDto {

    private Long id;
    private String username;
    private String password;
    private Role role;
    private List<UserDto> friends = new ArrayList<>();

    public static UserFullDto from(User user) {
        UserFullDto userFullDto = new UserFullDto();

        userFullDto.setId(user.getId());
        userFullDto.setUsername(user.getUsername());
        userFullDto.setPassword(user.getPassword());
        userFullDto.setRole(user.getRole());
        userFullDto.setFriends(user.getFriendsDto());
        return userFullDto;
    }


}
