package za.ac.cput.campusfacilitybooking.service;

import za.ac.cput.campusfacilitybooking.domain.Login;

public interface LoginService extends IService<Login, String> {

    Login create(Login login);

    Login read(String loginId);

    Login update(Login login);

    boolean delete(String loginId);

    Login authenticate(String username, String password);
}