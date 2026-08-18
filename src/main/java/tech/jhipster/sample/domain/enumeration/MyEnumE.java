package tech.jhipster.sample.domain.enumeration;

/**
 * The MyEnumE enumeration.
 */
public enum MyEnumE {
    AAA("aaa_aaa"),
    BBB;

    private String value;

    MyEnumE() {}

    MyEnumE(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
