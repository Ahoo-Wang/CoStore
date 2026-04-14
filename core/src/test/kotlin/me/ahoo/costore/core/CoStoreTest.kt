package me.ahoo.costore.core

import me.ahoo.test.asserts.assert
import org.junit.jupiter.api.Test

class CoStoreTest {

    @Test
    fun `should have correct BRAND`() {
        CoStore.BRAND.assert().isEqualTo("costore")
    }

    @Test
    fun `should have correct BRAND_PREFIX`() {
        CoStore.BRAND_PREFIX.assert().isEqualTo("costore.")
    }

    @Test
    fun `should have VERSION`() {
        CoStore.VERSION // Can be empty string when running in test environment without manifest
    }
}
