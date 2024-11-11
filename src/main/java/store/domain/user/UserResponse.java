package store.domain.user;

public enum UserResponse {
    YES("Y"),
    NO("N");

    private final String value;

    UserResponse(String value) {
        this.value = value;
    }

    public static UserResponse fromString(String input) {
        for (UserResponse response : UserResponse.values()) {
            if (response.value.equalsIgnoreCase(input)) {
                return response;
            }
        }
        throw new IllegalArgumentException("[ERROR] 올바른 응답을 입력해 주세요. (Y/N)");
    }
}
