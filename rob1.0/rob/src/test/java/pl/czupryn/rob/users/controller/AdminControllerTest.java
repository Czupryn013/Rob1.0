package pl.czupryn.rob.users.controller;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import pl.czupryn.rob.users.model.User;
import pl.czupryn.rob.users.model.UserDto;
import pl.czupryn.rob.users.repository.UserRepo;
import pl.czupryn.rob.users.service.UserService;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class AdminControllerTest {

    @Autowired
    private AdminController adminController;

    @Autowired
    private UserRepo userRepo;

    @MockBean
    private UserService userService;

    @Test
    public void shouldReturnNoContentWhenUserDbIsEmpty(){
        Mockito.when(userService.findAllUsers()).thenReturn(null);
        AdminController adminController = new AdminController(userService, userRepo);
        ResponseEntity<List<UserDto>> all = adminController.getAll();
        Assertions.assertThat(all).isEqualTo(new ResponseEntity<>(null, HttpStatus.NO_CONTENT));
    }

    @Test
    public void shouldReturnUserWhenServiceReturnContentById() {
        Long mockedUserId = 1L;
        User returnUser = new User("kot", "kot123");
        UserDto userDto = UserDto.from(returnUser);
        Mockito.when(userService.findUserById(mockedUserId)).thenReturn(returnUser);
        AdminController adminController =  new AdminController(userService, userRepo);
        ResponseEntity<UserDto> byId = adminController.getById(mockedUserId);
        Assertions.assertThat(byId).isEqualTo(new ResponseEntity<>(userDto, HttpStatus.OK));

    }

}