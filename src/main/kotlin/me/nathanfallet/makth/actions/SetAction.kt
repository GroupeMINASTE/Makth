package me.nathanfallet.makth.actions

import me.nathanfallet.makth.extensions.StringValue
import me.nathanfallet.makth.interfaces.Action
import me.nathanfallet.makth.interfaces.Value
import me.nathanfallet.makth.lexers.AlgorithmLexer.IncorrectArgumentCountException
import me.nathanfallet.makth.lexers.AlgorithmLexer.IncorrectArgumentTypeException
import me.nathanfallet.makth.resolvables.Context
import me.nathanfallet.makth.resolvables.Variable

data class SetAction(val identifier: String, val value: Value) : Action {

    companion object {

        fun handler(args: List<Value>): Action {
            if (args.count() != 2) {
                throw IncorrectArgumentCountException("set", args.count(), 2)
            }
            val identifier =
                    when (val identifierValue = args[0]) {
                        is Variable -> identifierValue.name
                        is StringValue -> identifierValue.value
                        else ->
                                throw IncorrectArgumentTypeException(
                                        "set",
                                        identifierValue,
                                        Variable::class
                                )
                    }
            return SetAction(identifier, args[1])
        }
    }

    @Throws(Action.ExecutionException::class)
    override fun execute(context: Context): Context {
        // First, compute the value with the given context
        val valueToSet = value.compute(context)

        // Check if there are missing variables
        val missingVariables = valueToSet.extractVariables()
        if (missingVariables.isNotEmpty()) {
            throw Action.UnknownVariablesException(this, context, missingVariables)
        }

        // Return the new context
        return Context(context.data + mapOf(identifier to valueToSet), context.outputs)
    }

    override fun toAlgorithmString(): String {
        val builder = StringBuilder()
        builder.append("set(")
        builder.append(identifier)
        builder.append(", ")
        builder.append(value.toAlgorithmString())
        builder.append(")")
        return builder.toString()
    }
}
