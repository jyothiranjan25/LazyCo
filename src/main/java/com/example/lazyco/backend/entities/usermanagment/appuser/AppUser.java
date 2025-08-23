package com.example.lazyco.backend.entities.usermanagment.appuser;

import com.example.lazyco.backend.core.abstracts.AbstractModelBase;
import com.example.lazyco.backend.core.databaseconf.schema.AppUserSchema;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = AppUserSchema.TABLE_NAME)
public class AppUser extends AbstractModelBase {

    @Column(name = AppUserSchema.USERNAME)
    private String username;

    @Column(name = AppUserSchema.PASSWORD)
    private String password;

    @Column(name = AppUserSchema.EMAIL)
    private String email;

    @Column(name = AppUserSchema.FIRST_NAME)
    private String firstName;

    @Column(name = AppUserSchema.LAST_NAME)
    private String lastName;
}
