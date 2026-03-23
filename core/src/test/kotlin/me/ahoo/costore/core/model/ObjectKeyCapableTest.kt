package me.ahoo.costore.core.model

import me.ahoo.test.asserts.assert
import org.junit.jupiter.api.Test

class ObjectKeyCapableTest {
    @Test
    fun `should create ObjectKeyCapable instance`() {
        val objectKey = "test/key"
        val instance =
            object : ObjectKeyCapable {
                override val key: ObjectKey = objectKey
            }
        instance.key.assert().isEqualTo(objectKey)
    }

    @Test
    fun `ObjectKey typealias should be String`() {
        val objectKey: ObjectKey = "my/object/key"
        objectKey.assert().isEqualTo("my/object/key")
    }
}
