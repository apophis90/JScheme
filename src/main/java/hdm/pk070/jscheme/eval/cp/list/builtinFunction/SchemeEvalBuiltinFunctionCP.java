package hdm.pk070.jscheme.eval.cp.list.builtinFunction;

import hdm.pk070.jscheme.error.SchemeError;
import hdm.pk070.jscheme.eval.cp.SchemeEvalCP;
import hdm.pk070.jscheme.obj.SchemeContinuationFunction;
import hdm.pk070.jscheme.obj.SchemeObject;
import hdm.pk070.jscheme.obj.builtin.function.SchemeBuiltinFunctionCP;
import hdm.pk070.jscheme.obj.builtin.simple.SchemeCons;
import hdm.pk070.jscheme.obj.builtin.simple.SchemeNil;
import hdm.pk070.jscheme.obj.builtin.simple.SchemeSymbol;
import hdm.pk070.jscheme.obj.continuation.SchemeContinuation;
import hdm.pk070.jscheme.table.environment.Environment;
import hdm.pk070.jscheme.table.environment.entry.EnvironmentEntry;

/**
 * Part 1 of built-in function call evaluation. It passes the unevaluated arguments to {@link SchemeEvalCP} and
 * determines the second part of built-in function evaluation to push the outcome onto the stack. As soon as all
 * arguments have been evaluated and pushed, the built-in function is invoked.
 *
 * @author patrick.kleindienst
 */
@SuppressWarnings("unchecked")
public class SchemeEvalBuiltinFunctionCP extends SchemeContinuationFunction {

    @Override
    public SchemeContinuation call(SchemeContinuation continuation) throws SchemeError {
        Object[] arguments = continuation.getArguments();

        SchemeBuiltinFunctionCP builtinFunction = (SchemeBuiltinFunctionCP) arguments[0];
        SchemeObject argumentList = (SchemeObject) arguments[1];
        Environment<SchemeSymbol, EnvironmentEntry> environment = (Environment<SchemeSymbol, EnvironmentEntry>)
                arguments[2];

        int argCount = 0;
        if (arguments.length == 4) {
            argCount = (int) arguments[3];
        }


        if (!argumentList.typeOf(SchemeNil.class)) {
            argCount++;
            SchemeObject currentArg = ((SchemeCons) argumentList).getCar();

            continuation.setArguments(builtinFunction, ((SchemeCons) argumentList).getCdr(), environment, argCount);
            continuation.setProgramCounter(new SchemeEvalBuiltinFunctionCP2());
            return SchemeContinuation.create(continuation, SchemeEvalCP.getInstance(), currentArg, environment);
        }

        continuation.setArguments(argCount);
        return builtinFunction.call(continuation);
    }
}
