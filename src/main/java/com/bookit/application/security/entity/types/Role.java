package com.bookit.application.security.entity.types;

public enum Role {
    REGULAR_USER("regularUser"),
    THEATRE_OWNER("theatreOwner"),
    ADMIN("admin");

    private final String code;

    Role(String code) {
        this.code = code;
    }

    public String code() {
        return code;
    }

    public static Role getRoleEnum(String possibleRole){
        for(Role role: Role.values()){
            if(role.code().equals(possibleRole)){
                return role;
            }
        }
        return null;
    }
}
