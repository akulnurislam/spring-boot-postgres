package com.akul.dsp.service;

import com.akul.dsp.dto.NameDTO;
import com.akul.dsp.dto.RequestDTO;
import com.akul.dsp.exception.NotFoundException;
import com.akul.dsp.model.User;
import com.akul.dsp.repository.UserRepository;
import com.akul.dsp.service.impl.UserServiceImpl;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import java.util.Optional;

import static java.lang.String.format;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class UserServiceTest {

    private final UserRepository userRepository = Mockito.mock(UserRepository.class);
    private final UserService userService = new UserServiceImpl(userRepository);

    @Test
    public void findOneByPhoneNumberTest() {
        String phoneNumber = "081288885555";

        User user = new User();
        user.setId("im-uuid");
        user.setPhoneNumber(phoneNumber);
        user.setName("im-name");
        user.setPassword("im-password");
        user.setSalt("im-salt");

        when(userRepository.findByPhoneNumber(phoneNumber)).thenReturn(Optional.of(user));

        User found = userService.findOneByPhoneNumber(phoneNumber);

        verify(userRepository, times(1)).findByPhoneNumber(phoneNumber);
        assertEquals(user, found);
    }

    @Test
    public void findOneByPhoneNumberNotFoundTest() {
        String phoneNumber = "081288885555";
        String message = format("user with phone number: %s not found", phoneNumber);

        when(userRepository.findByPhoneNumber(phoneNumber)).thenReturn(Optional.empty());

        Exception exception = assertThrows(NotFoundException.class, () -> {
            userService.findOneByPhoneNumber(phoneNumber);
        });

        verify(userRepository, times(1)).findByPhoneNumber(phoneNumber);
        assertEquals(message, exception.getMessage());
    }

    @Test
    public void createTest() {
        RequestDTO dto = new RequestDTO();
        dto.setPhoneNumber("081288885555");
        dto.setName("im-name");
        dto.setPassword("im-password");
        userService.create(dto);

        ArgumentCaptor<User> captor = ArgumentCaptor.forClass(User.class);
        verify(userRepository).save(captor.capture());

        User user = captor.getValue();

        verify(userRepository, times(1)).save(user);
        assertEquals(dto.getPhoneNumber(), user.getPhoneNumber());
        assertEquals(dto.getName(), user.getName());
        assertNotEquals(dto.getPassword(), user.getPassword());
        assertNotNull(user.getSalt());
    }

    @Test void updateByPhoneNumberTest() {
        String phoneNumber = "081288885555";
        String updatedName = "im-updated-name";

        User found = new User();
        found.setId("im-uuid");
        found.setPhoneNumber(phoneNumber);
        found.setName("im-name");
        found.setPassword("im-password");
        found.setSalt("im-salt");

        when(userRepository.findByPhoneNumber(phoneNumber)).thenReturn(Optional.of(found));

        User user = new User();
        user.setId(found.getId());
        user.setPhoneNumber(found.getPhoneNumber());
        user.setName(updatedName);
        user.setPassword(found.getPassword());
        user.setSalt(found.getSalt());

        when(userRepository.save(user)).thenReturn(user);

        NameDTO dto = new NameDTO();
        dto.setName(updatedName);
        User updatedUser = userService.updateByPhoneNumber(phoneNumber, dto);

        verify(userRepository, times(1)).findByPhoneNumber(phoneNumber);
        verify(userRepository, times(1)).save(user);
        assertEquals(user.getId(), updatedUser.getId());
        assertEquals(user.getPhoneNumber(), updatedUser.getPhoneNumber());
        assertEquals(user.getName(), updatedUser.getName());
        assertEquals(user.getPassword(), updatedUser.getPassword());
        assertEquals(user.getSalt(), updatedUser.getSalt());
    }

    @Test void updateByPhoneNumberNotFoundTest() {
        String phoneNumber = "081288885555";
        String updatedName = "im-updated-name";
        String message = format("user with phone number: %s not found", phoneNumber);

        when(userRepository.findByPhoneNumber(phoneNumber)).thenReturn(Optional.empty());

        Exception exception = assertThrows(NotFoundException.class, () -> {
            NameDTO dto = new NameDTO();
            dto.setName(updatedName);
            userService.updateByPhoneNumber(phoneNumber, dto);
        });

        verify(userRepository, times(1)).findByPhoneNumber(phoneNumber);
        assertEquals(message, exception.getMessage());
    }
}
