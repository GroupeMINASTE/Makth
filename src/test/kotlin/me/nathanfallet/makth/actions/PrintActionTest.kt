package me.nathanfallet.makth.actions

import me.nathanfallet.makth.extensions.StringValue
import me.nathanfallet.makth.interfaces.Action
import me.nathanfallet.makth.numbers.Integer
import me.nathanfallet.makth.resolvables.Context
import me.nathanfallet.makth.resolvables.Variable
import org.junit.Assert.assertEquals
import org.junit.Assert.assertThrows
import org.junit.Test

class PrintActionTest {

    private val context = Context()

    private val contextWithString = Context(
        mapOf(),
        listOf(
            StringValue("Hello world!"), StringValue("\n")
        )
    )

    private val contextWithX = Context(
        mapOf(
            "x" to Integer.instantiate(2)
        )
    )

    private val contextWithXAndString = Context(
        mapOf(
            "x" to Integer.instantiate(2)
        ),
        listOf(
            StringValue("x = "), Integer.instantiate(2), StringValue("\n")
        )
    )

    @Test
    fun toRawString() {
        assertEquals(
            "print(\"x = \", x)",
            PrintAction(listOf(StringValue("x = "), Variable("x"))).toAlgorithmString()
        )
    }

    @Test
    fun printString() {
        assertEquals(
            contextWithString,
            context.execute(PrintAction(listOf(StringValue("Hello world!"))))
        )
    }

    @Test
    fun printStringWithVariable() {
        assertEquals(
            contextWithXAndString,
            contextWithX.execute(PrintAction(listOf(StringValue("x = "), Variable("x"))))
        )
    }

    @Test
    fun printStringWithVariableWithoutContext() {
        assertThrows(Action.UnknownVariablesException::class.java) {
            context.execute(PrintAction(listOf(StringValue("x = "), Variable("x"))))
        }
    }

}