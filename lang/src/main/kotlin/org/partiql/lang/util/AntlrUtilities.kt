/*
 * Copyright 2022 Amazon.com, Inc. or its affiliates.  All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License").
 * You may not use this file except in compliance with the License.
 * A copy of the License is located at:
 *
 *      http://aws.amazon.com/apache2.0/
 *
 * or in the "license" file accompanying this file. This file is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific
 * language governing permissions and limitations under the License.
 */

package org.partiql.lang.util

import com.amazon.ion.IonSystem
import com.amazon.ion.IonValue
import com.amazon.ion.system.IonSystemBuilder
import org.antlr.v4.runtime.Token
import org.antlr.v4.runtime.tree.TerminalNode
import org.partiql.lang.errors.ErrorCode
import org.partiql.lang.errors.Property
import org.partiql.lang.errors.PropertyValueMap
import org.partiql.lang.syntax.ALL_OPERATORS
import org.partiql.lang.syntax.KEYWORDS
import org.partiql.lang.syntax.MULTI_LEXEME_TOKEN_MAP
import org.partiql.lang.syntax.ParserException
import org.partiql.lang.syntax.TYPE_ALIASES
import org.partiql.lang.syntax.antlr.PartiQLParser
import java.math.BigInteger

internal fun TerminalNode?.error(
    message: String,
    errorCode: ErrorCode,
    errorContext: PropertyValueMap = PropertyValueMap(),
    cause: Throwable? = null,
    ion: IonSystem = IonSystemBuilder.standard().build()
) = when (this) {
    null -> ParserException(errorCode = errorCode, errorContext = errorContext, cause = cause)
    else -> this.symbol.error(message, errorCode, errorContext, cause, ion)
}

internal fun Token?.error(
    message: String,
    errorCode: ErrorCode,
    errorContext: PropertyValueMap = PropertyValueMap(),
    cause: Throwable? = null,
    ion: IonSystem = IonSystemBuilder.standard().build()
) = when (this) {
    null -> ParserException(errorCode = errorCode, errorContext = errorContext, cause = cause)
    else -> {
        errorContext[Property.LINE_NUMBER] = this.line.toLong()
        errorContext[Property.COLUMN_NUMBER] = this.charPositionInLine.toLong() + 1
        errorContext[Property.TOKEN_DESCRIPTION] = this.type.getAntlrDisplayString()
        errorContext[Property.TOKEN_VALUE] = getIonValue(ion, this)
        ParserException(message, errorCode, errorContext, cause)
    }
}

/**
 * Returns the corresponding [IonValue] for a particular ANTLR Token
 */
internal fun getIonValue(ion: IonSystem, token: Token): IonValue {
    val type = token.type
    val text = token.text
    return when {
        type == PartiQLParser.EOF -> ion.newSymbol("EOF")
        ALL_OPERATORS.contains(text.toLowerCase()) -> ion.newSymbol(text.toLowerCase())
        type == PartiQLParser.ION_CLOSURE -> ion.singleValue(text.trimStart('`').trimEnd('`'))
        type == PartiQLParser.TRUE -> ion.newBool(true)
        type == PartiQLParser.FALSE -> ion.newBool(false)
        type == PartiQLParser.NULL -> ion.newNull()
        type == PartiQLParser.NULLS -> ion.newSymbol("nulls")
        type == PartiQLParser.MISSING -> ion.newNull()
        type == PartiQLParser.LITERAL_STRING -> ion.newString(text.trim('\'').replace("''", "'"))
        type == PartiQLParser.LITERAL_INTEGER -> ion.newInt(BigInteger(text, 10))
        type == PartiQLParser.LITERAL_DECIMAL -> try {
            ion.newDecimal(bigDecimalOf(text))
        } catch (e: NumberFormatException) {
            throw token.error(e.localizedMessage, ErrorCode.PARSE_EXPECTED_NUMBER, cause = e, ion = ion)
        }
        type == PartiQLParser.IDENTIFIER_QUOTED -> ion.newSymbol(text.trim('\"').replace("\"\"", "\""))
        MULTI_LEXEME_TOKEN_MAP.containsKey(text.toLowerCase().split("\\s+".toRegex())) -> {
            val pair = MULTI_LEXEME_TOKEN_MAP[text.toLowerCase().split("\\s+".toRegex())]!!
            ion.newSymbol(pair.first)
        }
        KEYWORDS.contains(text.toLowerCase()) -> ion.newSymbol(TYPE_ALIASES[text.toLowerCase()] ?: text.toLowerCase())
        else -> ion.newSymbol(text)
    }
}

internal fun Int.getAntlrDisplayString(): String = PartiQLParser.VOCABULARY.getSymbolicName(this)
