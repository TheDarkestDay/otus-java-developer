package ru.otus.jdbc.dao;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.otus.AbstractHibernateTest;
import ru.otus.core.model.Address;
import ru.otus.core.model.Phone;
import ru.otus.core.model.User;
import ru.otus.hibernate.dao.UserDaoHibernate;
import ru.otus.hibernate.sessionmanager.SessionManagerHibernate;

import java.util.Collections;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;

@DisplayName("Dao для работы с пользователями должно ")
class UserDaoHibernateTest extends AbstractHibernateTest {

    private SessionManagerHibernate sessionManagerHibernate;
    private UserDaoHibernate userDaoHibernate;

    @BeforeEach
    @Override
    public void setUp() {
        super.setUp();
        sessionManagerHibernate = new SessionManagerHibernate(sessionFactory);
        userDaoHibernate = new UserDaoHibernate(sessionManagerHibernate);
    }

    @Test
    @DisplayName(" корректно загружать пользователя по заданному id")
    void shouldFindCorrectUserById() {
        Address addr = new Address(0, "1st ave");
        Phone personal = new Phone(0, "11-22-33");
        User expectedUser = new User(0, "John Doe");
        expectedUser.setAddress(addr);
        expectedUser.addPhone(personal);
        saveUser(expectedUser);

        assertThat(expectedUser.getId()).isGreaterThan(0);

        sessionManagerHibernate.beginSession();
        Optional<User> mayBeUser = userDaoHibernate.findById(expectedUser.getId());

        if (mayBeUser.isEmpty()) {
            throw new RuntimeException("Could not find a user");
        }

        User foundUser = mayBeUser.get();

        assertThat(foundUser.getId()).isEqualTo(expectedUser.getId());
        assertThat(foundUser.getAddress()).isEqualTo(expectedUser.getAddress());
        assertThat(foundUser.getName()).isEqualTo(expectedUser.getName());

        var expectedPhones = expectedUser.getPhones();
        var foundPhones = foundUser.getPhones();
        assertThat(foundPhones.size()).isEqualTo(expectedPhones.size());
        for (int i = 0; i < foundPhones.size(); i++) {
            assertThat(foundPhones.get(i)).isEqualTo(expectedPhones.get(i));
        }

        sessionManagerHibernate.close();
    }

    @DisplayName(" корректно сохранять пользователя")
    @Test
    void shouldCorrectSaveUser() {
        User expectedUser = new User(0, "Вася");
        sessionManagerHibernate.beginSession();
        userDaoHibernate.insertOrUpdate(expectedUser);
        long id = expectedUser.getId();
        sessionManagerHibernate.commitSession();

        assertThat(id).isGreaterThan(0);

        User actualUser = loadUser(id);
        assertThat(actualUser).isNotNull().hasFieldOrPropertyWithValue("name", expectedUser.getName());

        expectedUser = new User(id, "Не Вася");
        sessionManagerHibernate.beginSession();
        userDaoHibernate.insertOrUpdate(expectedUser);
        long newId = expectedUser.getId();
        sessionManagerHibernate.commitSession();

        assertThat(newId).isGreaterThan(0).isEqualTo(id);
        actualUser = loadUser(newId);
        assertThat(actualUser).isNotNull().hasFieldOrPropertyWithValue("name", expectedUser.getName());
    }

    @DisplayName(" возвращать менеджер сессий")
    @Test
    void getSessionManager() {
        assertThat(userDaoHibernate.getSessionManager()).isNotNull().isEqualTo(sessionManagerHibernate);
    }

    @DisplayName("should save Users without redundant updates")
    @Test
    void shouldSaveUserWithoutRedundantUpdates() {
        Address addr = new Address(0, "1st ave");
        Phone personal = new Phone(0, "11-22-33");
        User user = new User(0, "John Doe");
        user.setAddress(addr);
        user.addPhone(personal);

        sessionManagerHibernate.beginSession();
        userDaoHibernate.insertOrUpdate(user);
        sessionManagerHibernate.commitSession();
    }
}
