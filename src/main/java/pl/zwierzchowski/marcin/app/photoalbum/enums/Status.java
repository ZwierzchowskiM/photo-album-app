package pl.zwierzchowski.marcin.app.photoalbum.enums;

public enum Status {
    PENDING ("Photo is submitted for review"),
    COMPLETED("Review is completed");

    private String description;

    Status(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

}
