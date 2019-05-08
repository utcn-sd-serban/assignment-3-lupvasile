package ro.utcn.sd.vasi.SnackOverflow.repository.jdbc;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import ro.utcn.sd.vasi.SnackOverflow.model.Question;
import ro.utcn.sd.vasi.SnackOverflow.model.Tag;


import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.TreeSet;


public class QuestionMapper implements RowMapper<Question> {

    @Override
    public Question mapRow(ResultSet rs, int rowNum) throws SQLException {
        return new Question(rs.getInt("id"),
                rs.getInt("author"),
                rs.getString("title"),
                rs.getString("text"),
                ZonedDateTime.parse(rs.getString("creation")),
                new HashSet<Tag>(),
                0);
    }
}