package br.com.erudio.integrationtests.unittests

import br.com.erudio.exceptions.RequiredObjectisNullException
import br.com.erudio.repository.BookRepository
import br.com.erudio.services.BookService
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations
import org.mockito.junit.jupiter.MockitoExtension
import java.util.*
import br.com.erudio.integrationtests.mocks.MockBook
@ExtendWith(MockitoExtension::class)
internal class BookServiceTest {

    private lateinit var inputObject: MockBook

    @InjectMocks
    private lateinit var service: BookService

    @Mock
    private lateinit var repository: BookRepository

    @BeforeEach
    fun setUpMock() {
        inputObject = MockBook()
        MockitoAnnotations.openMocks(this)
    }

    @Test
    fun findById() {
        val book = inputObject.mockEntity(1)
        book.id = 1
        `when`(repository.findById(1)).thenReturn(Optional.of(book))

        val result = service.findById(1)

        assertNotNull(result)
        assertNotNull(result.key)
        assertNotNull(result.links)
        assertTrue(result.links.toString().contains("</api/book/v1/1>;rel=\"self\""))
        assertEquals("Some Title1", result.title)
        assertEquals("Some Author1", result.author)
        assertEquals(25.0, result.price)
    }

    @Test
    fun create() {
        val entity = inputObject.mockEntity(1)

        val persisted = entity.copy()
        persisted.id = 1

        `when`(repository.save(entity)).thenReturn(persisted)

        val vo = inputObject.mockVO(1)
        val result = service.create(vo)

        assertNotNull(result)
        assertNotNull(result.key)
        assertNotNull(result.links)
        assertTrue(result.links.toString().contains("</api/book/v1/1>;rel=\"self\""))
        assertEquals("Some Title1", result.title)
        assertEquals("Some Author1", result.author)
        assertEquals(25.0, result.price)
    }

    @Test
    fun createWithNullBook() {
        val exception: Exception = assertThrows(
            RequiredObjectisNullException::class.java
        ) {service.create(null)}

        val expectedMessage = "It is not allowed to persist a null object!"
        val actualMessage = exception.message
        assertTrue(actualMessage!!.contains(expectedMessage))
    }

    @Test
    fun update() {
        val entity = inputObject.mockEntity(1)

        val persisted = entity.copy()
        persisted.id = 1

        `when`(repository.findById(1)).thenReturn(Optional.of(entity))
        `when`(repository.save(entity)).thenReturn(persisted)

        val vo = inputObject.mockVO(1)
        val result = service.update(vo)

        assertNotNull(result)
        assertNotNull(result.key)
        assertNotNull(result.links)
        assertTrue(result.links.toString().contains("</api/book/v1/1>;rel=\"self\""))
        assertEquals("Some Title1", result.title)
        assertEquals("Some Author1", result.author)
        assertEquals(25.0, result.price)
    }

    @Test
    fun updateWithNullBook() {
        val exception: Exception = assertThrows(
            RequiredObjectisNullException::class.java
        ) {service.update(null)}

        val expectedMessage = "It is not allowed to persist a null object!"
        val actualMessage = exception.message
        assertTrue(actualMessage!!.contains(expectedMessage))
    }

    @Test
    fun delete() {
        val entity = inputObject.mockEntity(1)
        `when`(repository.findById(1)).thenReturn(Optional.of(entity))
        service.delete(1)
    }
}