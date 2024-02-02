package org.project.models.todo;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.PathBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.apache.tomcat.jni.FileInfo;
import org.modelmapper.ModelMapper;
import org.project.api.controllers.todo.TodoDataSearch;
import org.project.api.controllers.todo.TodoForm;
import org.project.commons.ListData;
import org.project.commons.Pagination;
import org.project.commons.Utils;
import org.project.configs.jwt.CustomJwtFilter;
import org.project.entities.Member;
import org.project.entities.QTodoData;
import org.project.entities.TodoData;

import org.project.repositories.TodoDataRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Objects;

import static org.springframework.data.domain.Sort.Order.desc;

@Service
@RequiredArgsConstructor
public class TodoInfoService {

    private final TodoDataRepository todoDataRepository;

    private final HttpServletRequest request;
    private final EntityManager em;

    private final CustomJwtFilter customJwtFilter;

    public TodoData get(Long seq) {

        TodoData data = todoDataRepository.findById(seq).orElseThrow(TodoDataNotFoundException::new);

        return data;
    }

    public TodoForm getForm(Long seq) {
        TodoData data = get(seq);
        TodoForm form = new ModelMapper().map(data, TodoForm.class);
        form.setMode("update");

        return form;
    }

    public ListData<TodoData> getList(TodoDataSearch search) {
        QTodoData todoData = QTodoData.todoData;
        int page = Utils.getNumber(search.getPage(), 1);
        int limit = Utils.getNumber(search.getLimit(), 20);
        int offset = (page - 1) * limit;

        String sopt = Objects.requireNonNullElse(search.getSopt(), "subject_content"); // 검색 옵션
        String skey = search.getSkey(); // 검색 키워드

        BooleanBuilder andBuilder = new BooleanBuilder();


        // 키워드 검색 처리
        if (StringUtils.hasText(skey)) {
            skey = skey.trim();

             if (sopt.equals("content")) { // 내용 검색
                andBuilder.and(todoData.content.contains(skey));

            }
        }

        PathBuilder pathBuilder = new PathBuilder(TodoData.class, "todoData");
        List<TodoData> items = new JPAQueryFactory(em)
                .selectFrom(todoData)
                .leftJoin(todoData.todo)
                .where(andBuilder)
                .offset(offset)
                .limit(limit)
                .fetchJoin()
                .orderBy(
                        new OrderSpecifier(Order.valueOf("DESC"),
                                pathBuilder.get("createdAt")))
                .fetch();

        int total = (int) todoDataRepository.count(andBuilder);

        Pagination pagination = new Pagination(page, total, 10, limit, request);


        ListData<TodoData> data = new ListData<>();
        data.setContent(items);
        data.setPagination(pagination);

        return data;
    }


        public List<TodoData> getList(String tId, int num) {

            QTodoData todoData = QTodoData.todoData;
            num = Utils.getNumber(num, 10);
            Pageable pageable = PageRequest.of(0, num, Sort.by(desc("createdAt")));
            Page<TodoData> data = todoDataRepository.findAll(todoData.todo.tId.eq(tId), pageable);

            return data.getContent();
        }

}