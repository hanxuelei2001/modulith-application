package com.example.modulith.services.organization;

import org.jmolecules.event.annotation.Externalized;

// 如果需要主动调用其他模块的接口，可以使用这种方式
// 发送事件
// DTO 相当于是其他模块请求自己，或者外部请求自己的 request 和 response
// Event 相当于当前模块需要主动请求其他模块。如果后续需要拆分微服务，将其转化为 MQ 消息即可
@Externalized("organization-add::#{#this.getId()}")
public class OrganizationAddEvent {
    Long Id;

    public OrganizationAddEvent(Long id) {
        Id = id;
    }

    public Long getId() {
        return Id;
    }

    public void setId(Long id) {
        Id = id;
    }
}
