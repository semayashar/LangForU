package LangForU_DevTeam.LangForU.subscriber;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

/**
 * Репозитори интерфейс за управление на {@link Subscriber} ентитети.
 * Предоставя стандартни CRUD операции и дефинира персонализирани методи
 * за търсене на абонати.
 */
public interface SubscriberRepository extends JpaRepository<Subscriber, Long> {

    /**
     * Намира абонат по неговия имейл адрес.
     * Тъй като се очаква имейлът да бъде уникален, методът връща {@link Optional}.
     *
     * @param email Имейл адресът, по който се търси.
     * @return {@link Optional}, съдържащ абоната, ако е намерен, или празен, ако не е.
     */
    Optional<Subscriber> findByEmail(String email);

    /**
     * Връща списък с всички абонати.
     * Забележка: Този метод е стандартен за JpaRepository и не е нужно да се декларира повторно,
     * освен ако няма специфична причина за това.
     *
     * @return Списък ({@link List}) от всички {@link Subscriber}.
     */
    @Override
    List<Subscriber> findAll();
}