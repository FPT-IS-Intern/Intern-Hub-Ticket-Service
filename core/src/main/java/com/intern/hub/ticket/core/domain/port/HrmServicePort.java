package com.intern.hub.ticket.core.domain.port;

import java.util.List;
import java.util.Map;

import com.intern.hub.ticket.core.domain.model.HrmUserSearchResponse;

public interface HrmServicePort {

    /**
     * Tìm kiếm userIds theo keyword (fullName hoặc email).
     * Dùng cho filter nhanh — không phân trang.
     *
     * @param nameOrEmail keyword tìm kiếm
     * @return danh sách userIds, hoặc empty list nếu không có kết quả / HRM không trả lời
     */
    List<Long> searchUsers(String nameOrEmail);

    /**
     * Batch-fetch thông tin user (id, fullName, email) theo danh sách userIds.
     * Dùng để enrich tickets với thông tin user từ HRM khi hiển thị trang quản lý.
     *
     * @param userIds danh sách userIds cần lấy thông tin
     * @return Map&lt;userId, HrmUserSearchResponse&gt;, entries có thể rỗng nếu userId không tồn tại
     */
    Map<Long, HrmUserSearchResponse> getUsersByIds(List<Long> userIds);
}
