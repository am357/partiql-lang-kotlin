package org.partiql.lang.eval.physical.window

import org.partiql.annotations.ExperimentalWindowFunctions
import org.partiql.lang.domains.PartiqlPhysical
import org.partiql.lang.eval.ExprValue
import org.partiql.lang.eval.physical.EvaluatorState
import org.partiql.lang.eval.physical.operators.ValueExpression

@ExperimentalWindowFunctions
interface WindowFunction {

    val name: String

    /**
     * The reset function should be called before enter a new partition ( including the first one).
     *
     * For now, a partition is represented by list<Array<ExprValue>>.
     * We could potentially benefit from further abstraction of partition.
     */
    fun reset(partition: List<Array<ExprValue>>)

    /**
     * Process a row by outputting the result of the window function.
     */
    fun processRow(state: EvaluatorState, arguments: List<ValueExpression>, windowVarDecl: PartiqlPhysical.VarDecl)
}
