// File managed by WebFX (DO NOT EDIT MANUALLY)

module MilkyWayEscape.application {

    // Direct dependencies modules
    requires javafx.base;
    requires javafx.graphics;
    requires javafx.media;
    requires webfx.platform.resource;
    requires webfx.platform.scheduler;

    // Exported packages
    exports com.orangomango.milkywayescape;
    exports com.orangomango.milkywayescape.core;
    exports com.orangomango.milkywayescape.ui;

    // Resources packages
    opens audio;
    opens files;
    opens font;
    opens image;

    // Provided services
    provides javafx.application.Application with com.orangomango.milkywayescape.MainApplication;

}