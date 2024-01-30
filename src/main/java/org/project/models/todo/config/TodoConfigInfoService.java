package org.project.models.todo.config;

import com.querydsl.core.BooleanBuilder;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.project.api.controllers.admin.TodoConfigForm;
import org.project.api.controllers.admin.TodoSearch;
import org.project.commons.ListData;
import org.project.commons.Pagination;
import org.project.commons.Utils;
import org.project.commons.contansts.TodoAuthority;
import org.project.commons.exceptions.BadRequestException;
import org.project.configs.jwt.CustomJwtFilter;
import org.project.entities.QTodo;
import org.project.entities.Todo;
import org.project.repositories.TodoRepository;
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
public class TodoConfigInfoService {

    private final TodoRepository repository;
    private final HttpServletRequest request;
    private final CustomJwtFilter filter;

    public Todo get(String tId) {
        Todo data = repository.findById(tId).orElseThrow(TodoNotFoundException::new);

        return data;
    }

    public Todo get(String tId, boolean checkAuthority) {
        Todo data = get(tId);
        if (!checkAuthority) {
            return data;
        }

        // 글쓰기 권한 체크
        TodoAuthority authority = data.getAuthority();
        if (authority != TodoAuthority.MEMBER) {
            if (!filter.isUserLoggedIn()) {
                throw new BadRequestException(Utils.getMessage("Not_authorization", "validation"));
            }
        }

        return data;
    }

    public TodoConfigForm getForm(String bId) {
        Todo todo = get(bId);

        TodoConfigForm form = new ModelMapper().map(todo, TodoConfigForm.class);
        form.setAuthority(todo.getAuthority());
        form.setMode("edit");

        return form;
    }

    public ListData<Todo> getList(TodoSearch search) {
        BooleanBuilder andBuilder = new BooleanBuilder();

        int page = Utils.getNumber(search.getPage(), 1);
        int limit = Utils.getNumber(search.getLimit(), 20);

        /* 검색 처리 S */
        QTodo todo = QTodo.todo;
        // 키워드 검색
        String sopt = Objects.requireNonNullElse(search.getSopt(), "ALL");
        String skey = search.getSkey();
        if (StringUtils.hasText(skey)) {
            skey = skey.trim();

            if (sopt.equals("bId")) { // 게시판 아이디 
                andBuilder.and(todo.tId.contains(skey));
            } else if (sopt.equals("bName")) { // 게시판 이름
                andBuilder.and(todo.tname.contains(skey));

            } else { // 통합 검색 
                BooleanBuilder orBuilder = new BooleanBuilder();
                orBuilder.or(todo.tId.contains(skey))
                        .or(todo.tname.contains(skey));

                andBuilder.and(orBuilder);
            }
        }

        // 사용여부
        List<Boolean> active = search.getActive();
        if (active != null && !active.isEmpty()) {
            andBuilder.and(todo.active.in(active));
        }

        // 글쓰기 권한
        List<TodoAuthority> authorities = search.getAuthority() == null ? null : search.getAuthority().stream().map(TodoAuthority::valueOf).toList();

        if (authorities != null && !authorities.isEmpty()) {
            andBuilder.and(todo.authority.in(authorities));
        }
        /* 검색 처리 E */

        Pageable pageable = PageRequest.of(page - 1, limit, Sort.by(desc("createdAt")));

        Page<Todo> data = repository.findAll(andBuilder, pageable);


        Pagination pagination = new Pagination(page, (int)data.getTotalElements(), 10, limit, request);

        ListData<Todo> listData = new ListData<>();
        listData.setContent(data.getContent());
        listData.setPagination(pagination);

        return listData;
    }
}
