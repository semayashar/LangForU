package LangForU_DevTeam.LangForU.contactRequest;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Модулни тестове за класа ContactRequestService, базирани на финалната версия на кода.
 */
@ExtendWith(MockitoExtension.class)
class ContactRequestServiceTest {

    @Mock
    private ContactRequestRepository contactRequestRepository;

    @InjectMocks
    private ContactRequestService contactRequestService;

    private ContactRequest testContactRequest;

    @BeforeEach
    void setUp() {
        // Този обект ще се използва като върната стойност от mock-а
        testContactRequest = new ContactRequest(
                1L,
                "Test Name",
                "test@example.com",
                "Test Subject",
                "This is a test message.",
                LocalDateTime.now()
        );
    }

    @Test
    void save_whenCalled_shouldSetSubmittedAtAndCallRepositorySave() {
        // Arrange
        // Създаваме нов обект без зададено submittedAt, както би дошъл от контролера
        ContactRequest requestToSave = new ContactRequest("New Name", "new@example.com", "New Subject", "New Message");

        // Конфигурираме mock-а да върне нашия тестов обект, когато save бъде извикан
        when(contactRequestRepository.save(any(ContactRequest.class))).thenReturn(testContactRequest);

        // Act
        contactRequestService.save(requestToSave);

        // Assert
        // "Хващаме" обекта, който е подаден на contactRequestRepository.save(), за да го инспектираме
        ArgumentCaptor<ContactRequest> requestCaptor = ArgumentCaptor.forClass(ContactRequest.class);
        verify(contactRequestRepository).save(requestCaptor.capture());

        ContactRequest capturedRequest = requestCaptor.getValue();

        // Проверяваме ключовата логика в сервизния метод: дали submittedAt е зададен
        assertNotNull(capturedRequest.getSubmittedAt());
        // Проверяваме дали останалите данни са си същите
        assertEquals("New Name", capturedRequest.getName());
    }

    @Test
    void getAllRequests_whenCalled_shouldReturnListOfRequests() {
        // Arrange
        when(contactRequestRepository.findAll()).thenReturn(List.of(testContactRequest));

        // Act
        List<ContactRequest> requests = contactRequestService.getAllRequests();

        // Assert
        assertNotNull(requests);
        assertEquals(1, requests.size());
        verify(contactRequestRepository).findAll();
    }

    @Test
    void getRequestById_whenRequestExists_shouldReturnOptionalOfRequest() {
        // Arrange
        when(contactRequestRepository.findById(1L)).thenReturn(Optional.of(testContactRequest));

        // Act
        Optional<ContactRequest> foundRequestOptional = contactRequestService.getRequestById(1L);

        // Assert
        assertTrue(foundRequestOptional.isPresent());
        assertEquals(1L, foundRequestOptional.get().getId());
    }

    @Test
    void getRequestById_whenRequestDoesNotExist_shouldReturnEmptyOptional() {
        // Arrange
        when(contactRequestRepository.findById(99L)).thenReturn(Optional.empty());

        // Act
        Optional<ContactRequest> foundRequestOptional = contactRequestService.getRequestById(99L);

        // Assert
        assertTrue(foundRequestOptional.isEmpty());
    }

    @Test
    void deleteRequestById_whenCalled_shouldInvokeRepositoryDelete() {
        // Arrange
        Long requestId = 1L;
        doNothing().when(contactRequestRepository).deleteById(requestId);

        // Act
        contactRequestService.deleteRequestById(requestId);

        // Assert
        verify(contactRequestRepository, times(1)).deleteById(requestId);
    }
}