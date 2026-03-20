package org.neptrueworks.lox.diagnosis

public final inline class LineNumber(val line: UInt) {
    companion object;
    public operator fun inc() = LineNumber(this.line + 1u);
}
public val LineNumber.Companion.Start 
    get() = LineNumber(1u)