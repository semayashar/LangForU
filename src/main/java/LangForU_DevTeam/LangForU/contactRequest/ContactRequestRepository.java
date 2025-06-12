package LangForU_DevTeam.LangForU.contactRequest;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Репозитори интерфейс за управление на {@link ContactRequest} ентитети.
 * Чрез наследяването на {@link JpaRepository}, този интерфейс автоматично получава
 * пълен набор от стандартни CRUD (Create, Read, Update, Delete) операции,
 * без да е необходимо да се пишат имплементации.
 */
@Repository // Маркира интерфейса като Spring компонент за достъп до данни.
public interface ContactRequestRepository extends JpaRepository<ContactRequest, Long> {
    // Няма нужда от дефиниране на методи тук, тъй като се използват
    // вградените от JpaRepository, като save(), findById(), findAll(), deleteById() и др.
}