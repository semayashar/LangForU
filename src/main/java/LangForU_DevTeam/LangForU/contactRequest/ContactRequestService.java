package LangForU_DevTeam.LangForU.contactRequest;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Сервизен клас, който управлява бизнес логиката, свързана със заявките за контакт.
 * Предоставя методи за запис, извличане и изтриване на заявки.
 */
@Service
@AllArgsConstructor // Lombok: Генерира конструктор с всички полета, улеснявайки dependency injection.
public class ContactRequestService {

    /**
     * Репозитори за достъп до данните на заявките за контакт.
     * Инжектира се автоматично от Spring.
     */
    @Autowired
    private ContactRequestRepository contactRequestRepository;

    /**
     * Запазва нова заявка за контакт в базата данни.
     * Преди да я запази, задава текущия момент като време на изпращане.
     *
     * @param contactRequest Обектът {@link ContactRequest}, който трябва да бъде запазен.
     * @return Запазеният обект {@link ContactRequest} с попълнени ID и време на изпращане.
     */
    public ContactRequest save(ContactRequest contactRequest) {
        contactRequest.setSubmittedAt(LocalDateTime.now());
        return contactRequestRepository.save(contactRequest);
    }

    /**
     * Извлича всички заявки за контакт от базата данни.
     *
     * @return Списък ({@link List}) от всички {@link ContactRequest}.
     */
    public List<ContactRequest> getAllRequests() {
        return contactRequestRepository.findAll();
    }

    /**
     * Намира заявка за контакт по нейния уникален идентификатор (ID).
     *
     * @param id ID на търсената заявка.
     * @return {@link Optional}, съдържащ заявката, ако е намерена, или празен Optional, ако не е.
     */
    public Optional<ContactRequest> getRequestById(Long id) {
        return contactRequestRepository.findById(id);
    }

    /**
     * Изтрива заявка за контакт по нейния ID.
     * Операцията се изпълнява в рамките на трансакция.
     *
     * @param id ID на заявката, която трябва да бъде изтрита.
     */
    @Transactional
    public void deleteRequestById(Long id) {
        contactRequestRepository.deleteById(id);
    }
}