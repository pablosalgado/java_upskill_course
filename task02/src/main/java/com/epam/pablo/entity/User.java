package com.epam.pablo.entity;

/**
 * Created by maksym_govorischev on 14/03/14.
 */
public interface User extends Entity {
    /**
     * User Id. UNIQUE.
     * @return User Id.
     */
    String getName();
    void setName(String name);

    /**
     * User email. UNIQUE.
     * @return User email.
     */
    String getEmail();
    void setEmail(String email);
}
