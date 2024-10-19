package com.dosepack.auth.schema;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class CurrentUserInfo {
    private String id;
    private String username;
    private String email;
}
