/*
 * Bamboo - A Program Analysis Framework for Java
 *
 * Copyright (C) 2020 Tian Tan <tiantan@nju.edu.cn>
 * Copyright (C) 2020 Yue Li <yueli@nju.edu.cn>
 * All rights reserved.
 *
 * This software is designed for the "Static Program Analysis" course at
 * Nanjing University, and it supports a subset of Java features.
 * Bamboo is only for educational and academic purposes, and any form of
 * commercial use is disallowed.
 */

package bamboo.pta.analysis.solver;

import bamboo.pta.analysis.data.Pointer;
import bamboo.pta.element.Type;

import java.util.Objects;

class PointerFlowEdge {

    enum Kind {
        LOCAL_ASSIGN,
        // CAST,

        INSTANCE_LOAD,
        INSTANCE_STORE,

        PARAMETER_PASSING,
        RETURN,
    }

    private final Kind kind;

    private final Pointer from;

    private final Pointer to;

    /**
     * Type for cast assignment.
     */
    private final Type type;

    public PointerFlowEdge(Kind kind, Pointer from, Pointer to, Type type) {
        this.kind = kind;
        this.from = from;
        this.to = to;
        this.type = type;
    }

    public Kind getKind() {
        return kind;
    }

    public Pointer getFrom() {
        return from;
    }

    public Pointer getTo() {
        return to;
    }

    public Type getType() {
        return type;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PointerFlowEdge that = (PointerFlowEdge) o;
        return kind == that.kind &&
                from.equals(that.from) &&
                to.equals(that.to) &&
                Objects.equals(type, that.type);
    }

    @Override
    public int hashCode() {
        return Objects.hash(kind, from, to, type);
    }

    @Override
    public String toString() {
        return "[" + kind + "]" + from + " -> " + to;
    }
}