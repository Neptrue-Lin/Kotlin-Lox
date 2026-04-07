package org.neptrueworks.lox.parsing

public data class ArgumentList(val args: List<SyntaxExpression>);

public inline val ArgumentList.arity get() = this.args.size;