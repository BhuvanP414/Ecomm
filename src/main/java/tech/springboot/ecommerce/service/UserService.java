package tech.springboot.ecommerce.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tech.springboot.ecommerce.data.dao.UserDao;
import tech.springboot.ecommerce.data.domain.UserEntity;
import tech.springboot.ecommerce.data.domain.UserRole;
import tech.springboot.ecommerce.data.dto.RegisterUserDto;
import tech.springboot.ecommerce.data.filter.FindUserFilter;
import tech.springboot.ecommerce.utils.CryptoUtils;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserDao userDao;

    public UserEntity registerUser(RegisterUserDto registerUserDto, UserRole role) throws NoSuchAlgorithmException, InvalidKeySpecException {
        UserEntity withSameEmail = userDao.withEmail(registerUserDto.getUsername());

        if (withSameEmail != null) {
            return null;
        }

        UserEntity userEntity = new UserEntity();
        userEntity.setEmail(registerUserDto.getUsername());
        userEntity.setPasswordSalt(CryptoUtils.generateSalt());
        userEntity.setPasswordHash(CryptoUtils.generateHashForPassword(registerUserDto.getPassword(), userEntity.getPasswordSalt()));
        userEntity.setRole(role);
        userEntity.setName(registerUserDto.getName());

        return userDao.insert(userEntity);
    }

    public UserEntity findByUsername(String username) {
        FindUserFilter filter = new FindUserFilter();
        filter.setUsername(Optional.of(username));

        List<UserEntity> users = userDao.get(filter);

        if (users == null || users.size() != 1) {
            return null;
        }

        return users.get(0);
    }

    public UserEntity findByUsernameAndPassword(String username, String password) throws Exception {
        UserEntity user = this.findByUsername(username);
        if (user == null) {
            return null;
        }

        String recomputedHash =  CryptoUtils.generateHashForPassword(password, user.getPasswordSalt());
        if (recomputedHash.equals(user.getPasswordHash())) {
            return user;
        }

        return null;
    }
}
