package org.neptrueworks.lox.diagnosis

public final inline class ColumnNumber(val column: UInt) {
    companion object;
    public operator fun inc() = ColumnNumber(this.column + 1u);
}
public val ColumnNumber.Companion.Start
    get() = ColumnNumber(1u)