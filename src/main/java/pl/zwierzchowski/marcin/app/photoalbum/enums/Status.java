package pl.zwierzchowski.marcin.app.photoalbum.enums;

public enum Status {
    SUBMITTED("Submitted"),
    IN_REVIEW("In review"),
    NEEDS_UPDATE("Needs update"),
    COMPLETED("Completed");

    private String description;

    Status(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

}
