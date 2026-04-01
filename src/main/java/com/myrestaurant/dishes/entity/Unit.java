package com.myrestaurant.dishes.entity;

public enum Unit {
    PCS, KG, L;

    // Permet de parser "Kg", "kg", "KG" sans erreur
    public static Unit fromString(String value) {
        return Unit.valueOf(value.toUpperCase());
    }
}
