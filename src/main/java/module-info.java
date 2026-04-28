module drinkshop {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.base;

    requires org.controlsfx.controls;

    opens drinkshop.ui to javafx.fxml;
    exports drinkshop.ui;

    opens drinkshop.domain to  javafx.base;
    exports drinkshop.domain;

    exports drinkshop.repository;
    exports drinkshop.service;
    exports drinkshop.service.validator;

    opens drinkshop.service;
    opens drinkshop.repository;
    opens drinkshop.service.validator;
}