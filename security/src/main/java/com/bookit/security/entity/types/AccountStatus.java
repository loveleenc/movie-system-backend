package com.bookit.security.entity.types;


public enum AccountStatus {
    ACTIVE("active"),
    INACTIVE("inactive"),
    DISABLED("disabled");

    private final String code;
    AccountStatus(String code){
        this.code = code;
    }

    public String code() {
        return code;
    }


    public static AccountStatus getAccountStatusEnum(String possibleStatus){
        for(AccountStatus status: AccountStatus.values()){
            if(status.code().equals(possibleStatus)){
                return status;
            }
        }
        return null;
    }
}
