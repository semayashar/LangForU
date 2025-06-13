package LangForU_DevTeam.LangForU.blog;

import LangForU_DevTeam.LangForU.subscriber.Subscriber;
import LangForU_DevTeam.LangForU.subscriber.SubscriberRepository;
import LangForU_DevTeam.LangForU.subscriber.SubscriberService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Модулни тестове за класа BlogService.
 * Тества бизнес логиката за управление на блог публикации,
 * изолирано от външни зависимости чрез Mockito.
 */
@ExtendWith(MockitoExtension.class)
class BlogServiceTest {

    //<editor-fold desc="Mocks and InjectMocks">
    @Mock
    private BlogRepository blogRepository;

    @Mock
    private SubscriberService subscriberService;

    @Mock
    private SubscriberRepository subscriberRepository;

    @InjectMocks
    private BlogService blogService;
    //</editor-fold>

    private Blog testBlog;
    private List<Subscriber> subscribers;

    @BeforeEach
    void setUp() {
        // Подготвяме тестови данни, които се ползват в множество тестове.
        testBlog = new Blog();
        testBlog.setId(1L);
        testBlog.setName("Test Blog Title");
        testBlog.setBlogText("Some text here.");
        testBlog.setDate(LocalDate.now());

        // *** КОРЕКЦИЯ ***
        // Създаваме абонатите с конструктора по подразбиране и сетъри,
        // за да избегнем потенциални проблеми.
        Subscriber sub1 = new Subscriber();
        sub1.setId(1L);
        sub1.setEmail("test1@example.com");

        Subscriber sub2 = new Subscriber();
        sub2.setId(2L);
        sub2.setEmail("test2@example.com");

        subscribers = List.of(sub1, sub2);
    }

    @Test
    void findBlogById_whenBlogExists_shouldReturnBlog() {
        // Arrange (Подготовка)
        when(blogRepository.findById(1L)).thenReturn(Optional.of(testBlog));

        // Act (Действие)
        Blog foundBlog = blogService.findBlogById(1L);

        // Assert (Проверка)
        assertNotNull(foundBlog);
        assertEquals(1L, foundBlog.getId());
        verify(blogRepository).findById(1L);
    }

    @Test
    void findBlogById_whenBlogDoesNotExist_shouldReturnNull() {
        // Arrange
        when(blogRepository.findById(99L)).thenReturn(Optional.empty());

        // Act
        Blog foundBlog = blogService.findBlogById(99L);

        // Assert
        assertNull(foundBlog);
    }

    @Test
    void saveAndNotifySubscribers_whenCalled_shouldSaveBlogAndNotifyAllSubscribers() {
        // Arrange
        when(blogRepository.save(any(Blog.class))).thenReturn(testBlog);
        when(subscriberRepository.findAll()).thenReturn(subscribers);
        doNothing().when(subscriberService).notifySubscriber(any(Subscriber.class), any(Blog.class));

        // Act
        Blog savedBlog = blogService.saveAndNotifySubscribers(testBlog);

        // Assert
        assertNotNull(savedBlog);
        assertEquals(testBlog.getId(), savedBlog.getId());

        verify(blogRepository).save(testBlog);
        verify(subscriberRepository).findAll();
        verify(subscriberService, times(2)).notifySubscriber(any(Subscriber.class), eq(testBlog));
    }

    @Test
    void saveAndNotifySubscribers_whenOneNotificationFails_shouldContinueWithOthers() {
        // Arrange
        Subscriber goodSubscriber = subscribers.get(0);
        Subscriber badSubscriber = subscribers.get(1);

        when(blogRepository.save(any(Blog.class))).thenReturn(testBlog);
        when(subscriberRepository.findAll()).thenReturn(subscribers);

        doNothing().when(subscriberService).notifySubscriber(eq(goodSubscriber), any(Blog.class));
        doThrow(new RuntimeException("Email service failed")).when(subscriberService).notifySubscriber(eq(badSubscriber), any(Blog.class));

        // Act
        blogService.saveAndNotifySubscribers(testBlog);

        // Assert
        verify(subscriberService, times(1)).notifySubscriber(eq(goodSubscriber), any(Blog.class));
        verify(subscriberService, times(1)).notifySubscriber(eq(badSubscriber), any(Blog.class));
    }


    @Test
    void findBlogsPaginated_whenCalled_shouldRequestCorrectPageFromRepository() {
        // Arrange
        ArgumentCaptor<Pageable> pageableCaptor = ArgumentCaptor.forClass(Pageable.class);
        Page<Blog> expectedPage = new PageImpl<>(List.of(testBlog));
        when(blogRepository.findAll(pageableCaptor.capture())).thenReturn(expectedPage);

        int currentPage = 0;
        int pageSize = 5;

        // Act
        Page<Blog> resultPage = blogService.findBlogsPaginated(currentPage, pageSize);

        // Assert
        assertNotNull(resultPage);
        assertEquals(1, resultPage.getTotalElements());

        Pageable capturedPageable = pageableCaptor.getValue();
        assertEquals(currentPage, capturedPageable.getPageNumber());
        assertEquals(pageSize, capturedPageable.getPageSize());
        assertEquals(Sort.by("date").descending(), capturedPageable.getSort());
    }

    @Test
    void delete_whenCalled_shouldCallRepositoryDelete() {
        // Arrange
        doNothing().when(blogRepository).delete(any(Blog.class));

        // Act
        blogService.delete(testBlog);

        // Assert
        verify(blogRepository, times(1)).delete(testBlog);
    }

    @Test
    void getTop5Categories_whenRepositoryReturnsData_shouldTransformToListOfStrings() {
        // Arrange
        Object[] row1 = {"Technology", 10L};
        Object[] row2 = {"Lifestyle", 8L};
        List<Object[]> repoResult = List.of(row1, row2);
        when(blogRepository.findTop5Categories()).thenReturn(repoResult);

        // Act
        List<String> categories = blogService.getTop5Categories();

        // Assert
        assertNotNull(categories);
        assertEquals(2, categories.size());
        assertTrue(categories.contains("Technology"));
        assertTrue(categories.contains("Lifestyle"));
        verify(blogRepository).findTop5Categories();
    }
}