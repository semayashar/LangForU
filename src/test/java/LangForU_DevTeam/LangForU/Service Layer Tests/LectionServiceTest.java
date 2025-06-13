package LangForU_DevTeam.LangForU.lections;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Модулни тестове за класа LectionService.
 * Тестват основната CRUD функционалност и делегирането на извиквания към LectionRepository.
 */
@ExtendWith(MockitoExtension.class)
class LectionServiceTest {

    @Mock
    private LectionRepository lectionRepository;

    @InjectMocks
    private LectionService lectionService;

    private Lection testLection;

    @BeforeEach
    void setUp() {
        testLection = new Lection();
        testLection.setId(1L);
        testLection.setName("Test Lection");
        testLection.setTheme("Testing");
        testLection.setReleaseDate(LocalDate.now());
    }

    @Test
    void findById_whenLectionExists_shouldReturnLection() {
        // Arrange
        when(lectionRepository.findById(1L)).thenReturn(Optional.of(testLection));

        // Act
        Lection found = lectionService.findById(1L);

        // Assert
        assertNotNull(found);
        assertEquals(testLection.getName(), found.getName());
        verify(lectionRepository).findById(1L);
    }

    @Test
    void findById_whenLectionDoesNotExist_shouldThrowRuntimeException() {
        // Arrange
        when(lectionRepository.findById(99L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(RuntimeException.class, () -> {
            lectionService.findById(99L);
        });
    }

    @Test
    void findByReleaseDate_whenCalled_shouldInvokeRepositoryMethod() {
        // Arrange
        LocalDate today = LocalDate.now();
        when(lectionRepository.findAllByReleaseDate(today)).thenReturn(List.of(testLection));

        // Act
        List<Lection> lections = lectionService.findByReleaseDate(today);

        // Assert
        assertNotNull(lections);
        assertFalse(lections.isEmpty());
        verify(lectionRepository).findAllByReleaseDate(today);
    }

    @Test
    void save_whenCalled_shouldInvokeRepositorySave() {
        // Arrange
        // Няма нужда от when(), тъй като save връща void

        // Act
        lectionService.save(testLection);

        // Assert
        verify(lectionRepository).save(testLection);
    }

    @Test
    void getLectionsByCourseId_whenCalled_shouldInvokeRepositoryMethod() {
        // Arrange
        Long courseId = 5L;
        when(lectionRepository.findByCourseId(courseId)).thenReturn(List.of(testLection));

        // Act
        List<Lection> lections = lectionService.getLectionsByCourseId(courseId);

        // Assert
        assertNotNull(lections);
        assertFalse(lections.isEmpty());
        verify(lectionRepository).findByCourseId(courseId);
    }

    @Test
    void deleteLectionById_whenCalled_shouldInvokeRepositoryDelete() {
        // Arrange
        Long lectionId = 1L;
        doNothing().when(lectionRepository).deleteById(lectionId);

        // Act
        lectionService.deleteLectionById(lectionId);

        // Assert
        verify(lectionRepository).deleteById(lectionId);
    }

    @Test
    void getAllLections_whenCalled_shouldInvokeRepositoryFindAll() {
        // Arrange
        when(lectionRepository.findAll()).thenReturn(List.of(testLection));

        // Act
        List<Lection> lections = lectionService.getAllLections();

        // Assert
        assertNotNull(lections);
        assertFalse(lections.isEmpty());
        verify(lectionRepository).findAll();
    }
}