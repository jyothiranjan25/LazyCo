package com.example.lazyco.backend.entities.usermanagment.appuser;

import com.example.lazyco.backend.core.abstracts.AbstractDTO;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AppUserDTO extends AbstractDTO<AppUserDTO> {
    private String username;
    private String password;
    private String email;
    private String firstName;
    private String lastName;
}
