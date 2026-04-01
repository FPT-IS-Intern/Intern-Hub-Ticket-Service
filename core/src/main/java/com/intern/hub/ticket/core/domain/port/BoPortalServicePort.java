package com.intern.hub.ticket.core.domain.port;

import com.intern.hub.ticket.core.domain.model.BranchModel;

import java.util.List;

public interface BoPortalServicePort {

    /**
     * Lấy danh sách tất cả branches (công ty/chi nhánh) từ BoPortal Service.
     *
     * @return danh sách BranchModel, hoặc empty list nếu không có kết quả / BoPortal không trả lời
     */
    List<BranchModel> getAllBranches();
}
