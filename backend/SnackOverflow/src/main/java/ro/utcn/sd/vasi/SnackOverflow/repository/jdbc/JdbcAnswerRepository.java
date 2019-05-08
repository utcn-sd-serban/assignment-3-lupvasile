package ro.utcn.sd.vasi.SnackOverflow.repository.jdbc;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import ro.utcn.sd.vasi.SnackOverflow.model.Answer;
import ro.utcn.sd.vasi.SnackOverflow.repository.api.AnswerRepository;
import ro.utcn.sd.vasi.SnackOverflow.repository.jdbc.AnswerMapper;
import ro.utcn.sd.vasi.SnackOverflow.repository.jdbc.JdbcGeneralRepository;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;

public class JdbcAnswerRepository extends JdbcGeneralRepository<Answer> implements AnswerRepository {

    public JdbcAnswerRepository(JdbcTemplate template) {
        super(template, Answer.class, new AnswerMapper());
    }

    @Override
    public List<Answer> findAllbyQuestionId(int questionId) {
        return template.query("SELECT * FROM " + tableName + " WHERE question = ?", rowMapper, questionId);
    }
}
