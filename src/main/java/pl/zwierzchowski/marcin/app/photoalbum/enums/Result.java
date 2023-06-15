package pl.zwierzchowski.marcin.app.photoalbum.enums;

public enum Result {

    ACCEPTED("Photo accepted"),
    REJECTED("Photo rejected");

    private String description;

    Result(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

}
