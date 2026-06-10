package com.betterchoice.core.crud;

import com.betterchoice.core.dto.PageResponse;
import org.springframework.data.domain.Pageable;

public interface CrudService<REQ, RES, ID> {

    RES create(REQ request);

    RES getById(ID id);

    RES update(ID id, REQ request);

    void delete(ID id);

    PageResponse<RES> list(Pageable pageable);
}
