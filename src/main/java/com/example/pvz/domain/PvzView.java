package com.example.pvz.domain;

import java.util.List;

public record PvzView(PvzDto pvz, List<ReceptionView> receptions) {
    public record ReceptionView(Reception reception, List<Product> products) {}
}