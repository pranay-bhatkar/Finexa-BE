package com.expense_tracker.utility.mapper;

import com.expense_tracker.dto.notification.NotificationResponseDTO;
import com.expense_tracker.model.notification.Notification;

public class NotificationMapper {

    public static NotificationResponseDTO toDTO(Notification notification) {
        return NotificationResponseDTO.builder()
                .id(notification.getId())
                .title(notification.getTitle())
                .message(notification.getMessage())
                .read(notification.isRead())
                .createdAt(notification.getCreatedAt() != null ? notification.getCreatedAt(): null)
                .build();
    }
}