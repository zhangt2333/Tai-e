package sa.pta.jimple;

import sa.pta.element.Method;
import sa.pta.element.Variable;
import sa.pta.statement.Statement;
import soot.SootMethod;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

class JimpleMethod implements Method {

    private SootMethod method;

    private JimpleType classType;

    private JimpleVariable thisVar;

    private List<Variable> parameters;

    private Set<Variable> returnVars;

    private Set<Statement> statements;

    JimpleMethod(SootMethod method, JimpleType classType) {
        this.method = method;
        this.classType = classType;
    }

    void setThisVar(JimpleVariable thisVar) {
        this.thisVar = thisVar;
    }

    void setParameters(List<Variable> parameters) {
        this.parameters = parameters;
    }

    void setReturnVars(Set<Variable> returnVars) {
        this.returnVars = returnVars;
    }

    void addStatement(Statement statement) {
        if (statements == null) {
            statements = new HashSet<>();
        }
        statements.add(statement);
    }

    SootMethod getSootMethod() {
        return method;
    }

    @Override
    public boolean isInstance() {
        return !method.isStatic();
    }

    @Override
    public boolean isStatic() {
        return method.isStatic();
    }

    @Override
    public boolean isNative() {
        return method.isNative();
    }

    @Override
    public JimpleType getClassType() {
        return classType;
    }

    @Override
    public String getName() {
        return method.getName();
    }

    @Override
    public JimpleVariable getThis() {
        return thisVar;
    }

    @Override
    public List<Variable> getParameters() {
        return parameters;
    }

    @Override
    public Set<Variable> getReturnVariables() {
        return returnVars;
    }

    @Override
    public Set<Statement> getStatements() {
        return statements;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        JimpleMethod that = (JimpleMethod) o;
        return method.equals(that.method);
    }

    @Override
    public int hashCode() {
        return method.hashCode();
    }
}