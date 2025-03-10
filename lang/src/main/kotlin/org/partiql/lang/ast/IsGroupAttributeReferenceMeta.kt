/*
 * Copyright 2022 Amazon.com, Inc. or its affiliates.  All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License").
 *  You may not use this file except in compliance with the License.
 * A copy of the License is located at:
 *
 *      http://aws.amazon.com/apache2.0/
 *
 *  or in the "license" file accompanying this file. This file is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific
 *  language governing permissions and limitations under the License.
 */
package org.partiql.lang.ast

/**
 * Meta attached to an identifier when replacing the identifier with a reference to a group key variable declaration.
 */
internal class IsGroupAttributeReferenceMeta private constructor() : Meta {
    override val tag = TAG

    companion object {
        const val TAG = "\$is_group_attr_reference"

        val instance = IsGroupAttributeReferenceMeta()
    }
}
