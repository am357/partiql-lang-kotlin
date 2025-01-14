package org.partiql.sprout.test.generated

import com.amazon.ionelement.api.TimestampElement
import org.partiql.sprout.test.generated.visitor.SproutTestVisitor
import kotlin.Boolean
import kotlin.Float
import kotlin.Int
import kotlin.String
import kotlin.collections.List
import kotlin.collections.Map
import kotlin.collections.Set

public abstract class SproutTestNode {
    public open val children: List<SproutTestNode> = emptyList()

    public abstract fun <R, C> accept(visitor: SproutTestVisitor<R, C>, ctx: C): R
}

public data class Node(
    public val a: Boolean,
    public val b: Int,
    public val c: String,
    public val d: Float?,
    public val e: TimestampElement
) : SproutTestNode() {
    public override fun <R, C> accept(visitor: SproutTestVisitor<R, C>, ctx: C): R =
        visitor.visitNode(this, ctx)
}

public sealed class Collection : SproutTestNode() {
    public override fun <R, C> accept(visitor: SproutTestVisitor<R, C>, ctx: C): R = when (this) {
        is MySet -> visitor.visitCollectionMySet(this, ctx)
        is MyList -> visitor.visitCollectionMyList(this, ctx)
        is MyMap -> visitor.visitCollectionMyMap(this, ctx)
    }

    public data class MySet(
        public val values: Set<Int>
    ) : Collection() {
        public override fun <R, C> accept(visitor: SproutTestVisitor<R, C>, ctx: C): R =
            visitor.visitCollectionMySet(this, ctx)
    }

    public data class MyList(
        public val values: List<Int>
    ) : Collection() {
        public override fun <R, C> accept(visitor: SproutTestVisitor<R, C>, ctx: C): R =
            visitor.visitCollectionMyList(this, ctx)
    }

    public data class MyMap(
        public val values: Map<String, Int>
    ) : Collection() {
        public override fun <R, C> accept(visitor: SproutTestVisitor<R, C>, ctx: C): R =
            visitor.visitCollectionMyMap(this, ctx)
    }
}

public data class Inlines(
    public val `enum`: Bar?,
    public val product: Foo,
    public val sum: Sum
) : SproutTestNode() {
    public override val children: List<SproutTestNode> by lazy {
        val kids = mutableListOf<SproutTestNode?>()
        kids.add(product)
        kids.add(sum)
        kids.filterNotNull()
    }

    public override fun <R, C> accept(visitor: SproutTestVisitor<R, C>, ctx: C): R =
        visitor.visitInlines(this, ctx)

    public enum class Bar {
        A,
        B,
        C,
    }

    public data class Foo(
        public val x: Int,
        public val y: Bar
    ) : SproutTestNode() {
        public override fun <R, C> accept(visitor: SproutTestVisitor<R, C>, ctx: C): R =
            visitor.visitInlinesFoo(this, ctx)
    }

    public sealed class Sum : SproutTestNode() {
        public override fun <R, C> accept(visitor: SproutTestVisitor<R, C>, ctx: C): R = when (this) {
            is U -> visitor.visitInlinesSumU(this, ctx)
            is V -> visitor.visitInlinesSumV(this, ctx)
        }

        public data class U(
            public val foo: String
        ) : Sum() {
            public override fun <R, C> accept(visitor: SproutTestVisitor<R, C>, ctx: C): R =
                visitor.visitInlinesSumU(this, ctx)
        }

        public data class V(
            public val bar: String
        ) : Sum() {
            public override fun <R, C> accept(visitor: SproutTestVisitor<R, C>, ctx: C): R =
                visitor.visitInlinesSumV(this, ctx)
        }
    }
}
