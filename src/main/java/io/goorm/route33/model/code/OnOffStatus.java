package io.goorm.route33.model.code;

public enum OnOffStatus implements EnumInterface<Integer>, CodeInterface {
    OFF(0),
    ON(1),
    ;

    private final int type;

    OnOffStatus(int type) {
        this.type = type;
    }

    @Override
    public CodeClass getCodeClass() {
        return CodeClass.ON_OFF_STATUS;
    }

    @Override
    public String getCodeValue() {
        return Integer.valueOf(type).toString();
    }

    @Override
    public Integer value() {
        return this.type;
    }
}
